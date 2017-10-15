
public class ReleaseServerCommand extends ServerCommand {


    public ReleaseServerCommand(String[] tokens, ServerSynchronizer synchronizer, Direction cmdDirection) {
        super(tokens, synchronizer, cmdDirection);
        cmd = ServerCommand.RELEASE_CMD;
    }

    @Override
    public void executeReceiving() {
        if(synchronizer.getMyState() == ServerInfo.ServerState.READY) {
            if(additionalInfos.size() > 0) {
                StringBuilder sb = new StringBuilder();
                additionalInfos.forEach(token -> sb.append(token + " "));
                sb.setLength(sb.length() - 1);
                Command releaseCmd = Command.parseCommand(sb.toString(), synchronizer);
                if(releaseCmd.getCommandType() == CommandType.Client) {
                    ((ClientCommand)releaseCmd).executeWhenInCS();
                }
            }
            synchronizer.removeMinRequestFromServer(sendingServerid);
            ServerRequest request = synchronizer.getMinRequest();
            if(request == null) {
                return;
            }
            if(request.isMine(synchronizer.getId())) {
                Command requestedCmd = request.getRequestedCmd();
                if(requestedCmd != null) {
                    requestedCmd.executeReceivingCmd();
                }
            }
        }
    }
}
