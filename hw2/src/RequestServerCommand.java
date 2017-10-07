public class RequestServerCommand extends ServerCommand {

    public RequestServerCommand(int serverId, long clockValue) {
        super(serverId, clockValue);
        cmd = "request";
    }
}
