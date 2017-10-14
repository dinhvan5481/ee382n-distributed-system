package Server.Synchronize;

import Server.BookKeeper;
import Server.Command.Command;
import Server.Command.Server.*;
import Server.Core.*;
import Server.Utils.Logger;
import Server.Utils.ServerTCPListener;


import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ServerSynchronizer implements Runnable {
    public static enum RequestEnterCSState {
        NO_NEED,
        OTHER_IN_CS,
        INIT,
        RECEIVING_ACK,
        OK
    }

    public static long TIME_OUT = 100;

    private static long NUM_ITEMS_PARRALLISM_TRIGGER = 500;
    private int id;
    private ConcurrentHashMap<Integer, ServerInfo> servers;
    private ConcurrentHashMap<Integer, ITCPConnection> tcpCnnHashMap;
    private LogicalClock logicalClock;
    private ConcurrentHashMap<Long, ServerRequest> requests;
    private AtomicInteger numAcks;
    private ServerRequest currentRequest;
    private RequestEnterCSState requestEnterCSState;
    private BookKeeper store;

    Logger logger;

    public ServerSynchronizer(int id, LogicalClock logicalClock, BookKeeper store) {
        this.id = id;
        servers = new ConcurrentHashMap<>();
        tcpCnnHashMap = new ConcurrentHashMap<>();
        this.logicalClock = logicalClock;
        requests = new ConcurrentHashMap<>();
        numAcks = new AtomicInteger(0);
        requestEnterCSState = RequestEnterCSState.NO_NEED;
        this.store = store;

        logger = new Logger(Logger.LOG_LEVEL.DEBUG);
    }

    public void addServer(int id, ServerInfo serverInfo) {
        if(!servers.contains(id)) {
            servers.put(id, serverInfo);
        }
    }

    public int getId() {
        return id;
    }

    public ServerInfo getServerInfo(int id) {
        return servers.get(id);
    }

    public LogicalClock getLogicalClock() {
        return logicalClock;
    }

    public BookKeeper getStore() {
        return store;
    }

    public void addRequestToList(ServerRequest serverRequest) {
        requests.put(serverRequest.getClockValue(), serverRequest);
    }

    public void setMyCurrentRequest(ServerRequest request) {
        this.currentRequest = request;
    }

    public ServerRequest getMyCurrentRequest() {
        return currentRequest;
    }

    public void setMyState(ServerInfo.ServerState state) {
        servers.get(id).setServerState(state);
    }

    public ServerInfo.ServerState getMyState() {
        return servers.get(id).getServerState();
    }

    public int getMyPort() {
        return servers.get(id).getPort();
    }

    public ServerInfo getFirstServerInReadyState() {
        Optional<ServerInfo> optServerInfo = servers.values().stream()
                .filter(serverInfo -> !serverInfo.isMe(id) && serverInfo.getServerState() == ServerInfo.ServerState.READY)
                .findFirst();
        if(optServerInfo.isPresent()) {
            return optServerInfo.get();
        } else {
            return null;
        }
    }

    public void setITCPConn(int serverId, ITCPConnection itcpConnection) {
        tcpCnnHashMap.put(serverId, itcpConnection);
    }

    public void sendCommandTo(int targetServerId, ServerCommand cmd) {
        ITCPConnection itcpConnection = tcpCnnHashMap.get(targetServerId);
        itcpConnection.sendCommand(cmd);
    }

    public RequestEnterCSState getRequestEnterCSState() {
        return requestEnterCSState;
    }

    public void requestCS(ServerRequest request) {
        requests.put(request.getClockValue(), request);

            requestEnterCSState = RequestEnterCSState.INIT;
            setMyCurrentRequest(request);
            ServerCommand requestCmd = new RequestServerCommand(null, this, ServerCommand.Direction.Sending);
            broadcastCommand(requestCmd);

    }

    public void recordAck() {
        int numAck = numAcks.incrementAndGet();
        int noOfServerInReadyState = (int) servers.values().stream().filter(serverInfo -> !serverInfo.isMe(id) && serverInfo.getServerState() == ServerInfo.ServerState.READY).count();
        int minRequestClock = servers.entrySet().stream().map(entry -> entry.getKey()).min((k1, k2) -> Long.compare(k1, k2)).get();

        if(numAck >= noOfServerInReadyState && requests.get(minRequestClock).isMine(id)) {
            requestEnterCSState = RequestEnterCSState.OK;
        }
    }

    public synchronized boolean canEnterCS() {
         return requestEnterCSState == RequestEnterCSState.OK;
    }

    public void exitCS() {
        ServerCommand release = new ReleaseServerCommand(null, this, Command.Direction.Sending);
        requestEnterCSState = RequestEnterCSState.NO_NEED;
        broadcastCommand(release);
        logCurrentStatus();
    }

    public void startSyncStore() {
        ServerCommand InternalReqSyncCmd = new InternalRequestSyncServerCommand(null, this, Command.Direction.Receiving);
        ServerRequest syncRequest = new ServerRequest(id, getLogicalClock().getCurrentClock(), InternalReqSyncCmd);

        requestCS(syncRequest);
    }

    public void endSyncStore() {
        exitCS();
        setMyState(ServerInfo.ServerState.READY);
    }


    @Override
    public void run() {
        List<Thread> tasks = new LinkedList<>();

        // spin up backend server connection
        servers.values().stream().forEach(serverInfo -> {
            try {
                if(!serverInfo.isMe(id)) {
                    tcpCnnHashMap.put(serverInfo.getId(),
                            new BackEndServerConnection(this, serverInfo));
                }
            } catch (IOException e) {
                logger.log(Logger.LOG_LEVEL.INFO, toString() + ": Cannot create backend connection for server " + serverInfo.getId());
                serverInfo.setServerState(ServerInfo.ServerState.OFFLINE);
                e.printStackTrace();
                return;
            }
        });
        tcpCnnHashMap.values().stream().forEach(backEndServerConnection ->
                tasks.add(new Thread(backEndServerConnection)));

        // backend
        tasks.stream().forEach(task -> task.start());

        servers.values().stream().filter(serverInfo -> serverInfo.getServerState() != ServerInfo.ServerState.OFFLINE)
                .forEach(serverInfo -> {
                    long clockValue = logicalClock.tick();
                    JoinServerCommand joinServerCommand = new JoinServerCommand(new String[]{}, this, ServerCommand.Direction.Sending);
                    ITCPConnection backEndServerConnection = tcpCnnHashMap.get(serverInfo.getId());
                    if (backEndServerConnection != null) {
                        String cmd = joinServerCommand.buildSendingCmd();
                        logger.log(Logger.LOG_LEVEL.DEBUG, toString() + " : send join command: " + cmd);
                        backEndServerConnection.sendTCPMessage(cmd);
                    }
                });



        try {
            Thread.sleep(2 * servers.values().size() * TIME_OUT);
        } catch (InterruptedException e) {
            logger.log(Logger.LOG_LEVEL.INFO, "Error while sleeping for waiting servers start up");
            e.printStackTrace();
            return;
        }

        logicalClock.tick();
        logAllServerState();

        if(!servers.values().stream().anyMatch(serverInfo -> serverInfo.getServerState() == ServerInfo.ServerState.READY)) {
            setMyState(ServerInfo.ServerState.READY);
            logger.log(Logger.LOG_LEVEL.DEBUG, "Server " + id + " change to READY STATE - ready to spin store up");
            logLogicalClock();

            ServerTCPListener tcpListener;
            try {
                tcpListener = new ServerTCPListener(getMyPort(), store, this);
            } catch (IOException e) {
                System.out.println("Cannot initialize TCP Handler. Exit store");
                e.printStackTrace();
                return;
            }

            //Spin store up
            Thread tcpListenerThread = new Thread(tcpListener);
            tasks.add(tcpListenerThread);
            try {
                tcpListenerThread.start();
            } catch (Exception e) {
                System.out.println("Cannot run store TCP handler thread. Exit store");
                e.printStackTrace();
                return;
            }

        } else {
            logger.log(Logger.LOG_LEVEL.DEBUG, "Some server is in ready state, start sync process");
            //TODO: start sync process

        }

        tasks.stream().forEach(task -> {
            try {
                task.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public String toString() {
        return "Server " + id;
    }

    public void broadcastCommand(ServerCommand cmd) {
        servers.values().parallelStream().forEach(serverInfo -> {
            if(!serverInfo.isMe(id) && serverInfo.getServerState() != ServerInfo.ServerState.UNKNOWN && serverInfo.getServerState() != ServerInfo.ServerState.OFFLINE) {
                sendCommandTo(serverInfo.getId(), cmd);
            }
        });
    }

    private void logLogicalClock() {
        logger.log(Logger.LOG_LEVEL.DEBUG, "Server " + id + " - clock value: " + logicalClock.getCurrentClock());
    }

    private void logAllServerState() {
        servers.values().stream().forEach(serverInfo -> {
            if(!serverInfo.isMe(id)) {
                logger.log(Logger.LOG_LEVEL.DEBUG, "Server " + serverInfo.getId() + " state: " + serverInfo.getServerState().name());
            } else {
                logger.log(Logger.LOG_LEVEL.DEBUG, "Myself Server " + serverInfo.getId() + " state: " + serverInfo.getServerState().name());
            }
        });
    }

    private void logCurrentStatus() {
        logger.log(Logger.LOG_LEVEL.DEBUG, toString() + " clock value: " + getLogicalClock().getCurrentClock());
        logger.log(Logger.LOG_LEVEL.DEBUG, "Researvation info: ");
        logger.log(Logger.LOG_LEVEL.DEBUG, store.toString());
    }

}

    /*
    public void sendRequest() {
        long clockValue = logicalClock.tick();
        currentRequest = new ServerRequest(id, clockValue);
        requests.put(clockValue, currentRequest);
        ServerCommand requestCommand = new RequestServerCommand(id, clockValue);
        broadcast(requestCommand);
    }

    public void getRequest(int serverRequestId, long senderClockValue) {
        logicalClock.tick(senderClockValue);
        ServerRequest request = new ServerRequest(serverRequestId, senderClockValue);
        requests.put(senderClockValue, request);

        ServerInfo requestedServer = servers.get(serverRequestId);
        sendAck(requestedServer);
    }

    public void sendAck(ServerInfo dest) {
        long clockValue = logicalClock.tick();
        ServerCommand ackServerCommand = new AckServerCommand(id, clockValue);
        sendCommand(dest, ackServerCommand);
    }

    public void receiveAck(int serverId, long senderClockValue) {
        long clockValue = logicalClock.tick(senderClockValue);
        int currentAcks = numAcks.incrementAndGet();

        //TODO: need to determine if we can enter CS

    }

    public void sendRelease() {
        long clockValue = logicalClock.tick();
        //TODO: should we send the clock value of the release, or we can imply remove smallest clock value of request on receivers side
        ReleaseServerCommand releaseServerCommand = new ReleaseServerCommand(id, clockValue);
        releaseServerCommand.setReleaseClockValue(currentRequest.getClockValue());
        broadcast(releaseServerCommand);
        long requestClockValue = currentRequest.getClockValue();
        requests.remove(requestClockValue);
    }

    public void receiveRelease(int serverId, long senderClockValue, long releaseClockValue) {
        long clockValue = logicalClock.tick(senderClockValue);
        requests.remove(releaseClockValue);

        //TODO: need to dertermine if we can enter CS
    }
*/