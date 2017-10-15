public class SyncStateServerCommand extends ServerCommand {
    public SyncStateServerCommand(String[] tokens, ServerSynchronizer synchronizer, Direction cmdDirection) {
        super(tokens, synchronizer, cmdDirection);
        cmd = SYNC_STATE_CMD;
    }

    @Override
    protected void executeReceiving() {
        ServerInfo.ServerState sendingServerState = ServerInfo.ServerState.valueOf(additionalInfos.get(0));
        synchronizer.setNeighborServerState(sendingServerid, sendingServerState);
    }
}
