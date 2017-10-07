public abstract class ServerCommand {
    protected String cmd;
    protected int serverId;
    protected long clockValue;

    protected ServerCommand(){}
    protected ServerCommand(int serverId, long clockValue) {
        this.cmd = "base";
        this.serverId = serverId;
        this.clockValue = clockValue;
    }

    @Override
    public String toString() {
        return cmd + " " + clockValue + " " + serverId;
    }
}
