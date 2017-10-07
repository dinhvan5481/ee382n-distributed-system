public class ServerRequest {
    private int requestedServerId;
    private long clockValue;

    public ServerRequest(int serverId, long clockValue) {
        this.requestedServerId = serverId;
        this.clockValue = clockValue;
    }

    public int getRequestedServerId() {
        return requestedServerId;
    }

    public long getClockValue() {
        return clockValue;
    }
}
