package Server.Command.Server;

public class AckServerCommand extends ServerCommand {
    public AckServerCommand(int serverId, long clockValue) {
        super(serverId, clockValue);
        cmd = "ack";
    }
}
