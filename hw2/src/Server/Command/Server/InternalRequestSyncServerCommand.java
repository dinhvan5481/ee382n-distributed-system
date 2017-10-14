package Server.Command.Server;

import Server.Core.ServerInfo;
import Server.Synchronize.ServerSynchronizer;

public class InternalRequestSyncServerCommand extends ServerCommand {

    public InternalRequestSyncServerCommand(String[] tokens, ServerSynchronizer synchronizer, Direction cmdDirection) {
        super(tokens, synchronizer, cmdDirection);
        cmd = INTERNAL_SYNC_CMD;
    }

    @Override
    protected void executeReceiving() {
        ServerInfo serverInfo = synchronizer.getFirstServerInReadyState();
        if(serverInfo != null) {
            ServerCommand syncCommand = new SyncRequestServerCommand(null, synchronizer, Direction.Sending);
            synchronizer.sendCommandTo(serverInfo.getId(), syncCommand);
        }
    }
}
