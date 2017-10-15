package Server.Command.Server;

import Server.Core.ServerInfo;
import Server.Synchronize.ServerSynchronizer;
import Server.Utils.Logger;

public class AckJoinServerCommand extends ServerCommand {

    public AckJoinServerCommand(String[] tokens, ServerSynchronizer synchronizer, Direction cmdDirection) {
        super(tokens, synchronizer, cmdDirection);
        cmd = ServerCommand.ACK_JOIN_CMD;
    }

    @Override
    protected void executeReceiving() {
        ServerInfo.ServerState sendingServerState = ServerInfo.ServerState.valueOf(additionalInfos.get(0));
        synchronizer.setNeighborServerState(sendingServerid, sendingServerState);
        if(sendingServerState == ServerInfo.ServerState.READY) {
            logger.log(Logger.LOG_LEVEL.DEBUG, synchronizer.toString() + ": server " + sendingServerid + " in READY STATE" );
            //Start sync process
            synchronizer.startSyncStore();
        }
    }
}
