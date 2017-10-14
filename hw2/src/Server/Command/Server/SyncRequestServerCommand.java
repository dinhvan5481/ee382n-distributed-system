package Server.Command.Server;

import Server.Core.ServerRequest;
import Server.Synchronize.ServerSynchronizer;

public class SyncRequestServerCommand extends ServerCommand {


    public SyncRequestServerCommand(String[] tokens, ServerSynchronizer synchronizer, Direction cmdDirection) {
        super(tokens, synchronizer, cmdDirection);
        cmd = ServerCommand.SYNC_REQUEST_CMD;
    }

    @Override
    protected void executeReceiving() {
        String reservation = synchronizer.getStore().getCurrentReservations();
        if(reservation.isEmpty()) {
            reservation = "-1";
        }
        ServerCommand synRespCmd = new SyncResponseServerCommand(new String[]{reservation}, synchronizer, Direction.Sending);
        synchronizer.sendCommandTo(sendingServerid, synRespCmd);
    }
}
