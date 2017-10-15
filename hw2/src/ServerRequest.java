public final class ServerRequest implements Comparable<ServerRequest>{
    private int requestedServerId;
    private long clockValue;
    private Command requestedCmd;

    public ServerRequest(int serverId, long clockValue, Command requestedCmd) {
        this.requestedServerId = serverId;
        this.clockValue = clockValue;
        this.requestedCmd = requestedCmd;
    }

    public int getRequestedServerId() {
        return requestedServerId;
    }

    public long getClockValue() {
        return clockValue;
    }

    public Command getRequestedCmd() {
        return requestedCmd;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) {
            return false;
        }
        if(!ServerRequest.class.isAssignableFrom(obj.getClass())) {
            return false;
        }
        ServerRequest obj2 = (ServerRequest)obj;
        if(getClockValue() == obj2.getClockValue() && getRequestedServerId() == obj2.getRequestedServerId()) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return String.format("%d%d", clockValue, requestedServerId).hashCode();
    }

    @Override
    public int compareTo(ServerRequest o) {
        if(this.equals(o)) {
            return 0;
        }
        return (int) (clockValue - o.getClockValue());
    }

    public boolean isMine(int id) {
        return requestedServerId == id;
    }
}
