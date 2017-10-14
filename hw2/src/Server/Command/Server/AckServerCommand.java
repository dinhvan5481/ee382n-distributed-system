package Server.Command.Server;

import Server.Command.Client.ClientCommand;
import Server.Command.Command;
import Server.Synchronize.ServerSynchronizer;

public class AckServerCommand extends ServerCommand {

    public AckServerCommand(String[] tokens, ServerSynchronizer synchronizer, Direction cmdDirection) {
        super(tokens, synchronizer, cmdDirection);
        cmd = ServerCommand.ACK_CMD;
    }

    @Override
    public void executeReceiving() {
        synchronizer.recordAck();
        if(synchronizer.canEnterCS()) {
            Command requestedCommand = synchronizer.getMyCurrentRequest().getRequestedCmd();
            requestedCommand.executeReceivingCmd();
            if(requestedCommand.getCommandType() == CommandType.Client) {
                ((ClientCommand)requestedCommand).executeInCS();
            }
        }
    }
}
