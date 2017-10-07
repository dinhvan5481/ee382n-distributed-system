public class ReleaseServerCommand extends ServerCommand {
    public ReleaseServerCommand(int serverId, long clockValue) {
        super(serverId, clockValue);
        cmd = "release";
    }
}
