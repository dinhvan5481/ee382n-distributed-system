package Server.Command.Server;

import Server.Core.ServerInfo;
import Server.Server;
import Server.Synchronize.ServerSynchronizer;
import Server.Utils.Logger;

public class AckJoinServerCommand extends ServerCommand {

    public AckJoinServerCommand(String[] tokens, ServerSynchronizer synchronizer, Direction cmdDirection) {
        super(tokens, synchronizer, cmdDirection);
        cmd = ServerCommand.ACK_JOIN_CMD;
    }

    @Override
    public void executeReceiving() {
        synchronizer.getLogicalClock().tick(sendingServerClockValue);
        ServerInfo.ServerState sendingServerState = ServerInfo.ServerState.valueOf(additionalInfos.get(0));
        synchronizer.getServerInfo(sendingServerid).setServerState(sendingServerState);
        if(sendingServerState == ServerInfo.ServerState.READY) {
            logger.log(Logger.LOG_LEVEL.DEBUG, synchronizer.toString() + ": server " + sendingServerid + " in READY STATE" );
            //Start sync process
            synchronizer.syncStore();
        } else if(sendingServerState == ServerInfo.ServerState.JOIN) {
            // determine who has smallest id, will be the server in ready state
        }
    }
}
