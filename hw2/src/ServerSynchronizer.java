import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class ServerSynchronizer {
    private int id;
    private ConcurrentHashMap<Integer, ServerInfo> servers;
    private LogicalClock logicalClock;
    private ConcurrentLinkedQueue<ServerRequest> requests;
    private AtomicInteger numAcks;

    public ServerSynchronizer(LogicalClock logicalClock) {
        servers = new ConcurrentHashMap<>();
        this.logicalClock = logicalClock;
        requests = new ConcurrentLinkedQueue<>();
        numAcks = new AtomicInteger(0);
    }

    public void sendRequest() {
        long clockValue = logicalClock.tick();
        ServerRequest serverRequest = new ServerRequest(id, clockValue);
        requests.add(serverRequest);
        ServerCommand requestCommand = new RequestServerCommand(id, clockValue);
        broadcast(requestCommand);
    }

    public void getRequest(int serverRequestId, int senderClockValue) {
        logicalClock.syncClock(senderClockValue);
        ServerRequest request = new ServerRequest(serverRequestId, senderClockValue);
        requests.add(request);

        ServerInfo requestedServer = servers.get(serverRequestId);
        sendAck(requestedServer);
    }

    public void sendAck(ServerInfo dest) {
        long clockValue = logicalClock.tick();
        ServerCommand ackServerCommand = new AckServerCommand(id, clockValue);
        sendCommand(dest, ackServerCommand);
    }

    public void receiveAck(int s) {

    }


    private void sendCommand(ServerInfo info, ServerCommand command) {

    }

    private void broadcast(ServerCommand command) {
        for (ServerInfo serverInfo :
                servers.values()) {
            if (!serverInfo.isMe(id)) {
                sendCommand(serverInfo, command);
            }
        }
    }
}
