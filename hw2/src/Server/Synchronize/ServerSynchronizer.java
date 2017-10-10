package Server.Synchronize;

import Server.Command.Server.AckServerCommand;
import Server.Command.Server.ReleaseServerCommand;
import Server.Command.Server.RequestServerCommand;
import Server.Command.Server.ServerCommand;
import Server.Core.*;
import Server.Server;


import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ServerSynchronizer {
    private static long NUM_ITEMS_PARRALLISM_TRIGGER = 500;
    private int id;
    private ConcurrentHashMap<Integer, ServerInfo> servers;
    private LogicalClock logicalClock;
    private ConcurrentHashMap<Long, ServerRequest> requests;
    private AtomicInteger numAcks;
    private ServerRequest currentRequest;

    public ServerSynchronizer(int id, LogicalClock logicalClock) {
        this.id = id;
        servers = new ConcurrentHashMap<>();
        this.logicalClock = logicalClock;
        requests = new ConcurrentHashMap<>();
        numAcks = new AtomicInteger(0);
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

    public void broadcast(ServerCommand command) {
//        for (ServerInfo serverInfo :
//                servers.values()) {
//            if (!serverInfo.isMe(id) && serverInfo.isOnline()) {
//                sendCommand(serverInfo, command);
//            }
//        }
    }
}
