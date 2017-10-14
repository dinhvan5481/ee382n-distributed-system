package Server.Command.Server;

import Server.Core.ServerInfo;
import Server.Synchronize.ServerSynchronizer;
import Server.Utils.Logger;

import java.io.IOException;
import java.net.Socket;

public class JoinServerCommand extends ServerCommand {

    public JoinServerCommand(String[] tokens, ServerSynchronizer synchronizer, Direction cmdDirection) {
        super(tokens, synchronizer, cmdDirection);
        cmd = ServerCommand.JOIN_CMD;
    }

//    @Override
//    protected String executeSending() {
//        long clockValue = synchronizer.getLogicalClock().tick();
//        String cmd = buildSendingCmd();
//
//
//        String response;
//        try {
//            while ((response = inputStream.readLine()) != null) {
//                logger.log(Logger.LOG_LEVEL.INFO, String.format("Server %d -  Response from server: %s", synchronizer.getId(), response));
//                ServerCommand result = parseServerInput(response);
//                result.execute();
//            }
//        } catch (IOException e) {
//            synchronizer.getServerInfo(endpointServerId).setNeighborServerState(ServerInfo.ServerState.OFFLINE);
//        } finally {
//            try {
//                clientSocket.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//                return cmd;
//            }
//        }
//        return cmd;
//        return "";
//    }

    @Override
    public void executeReceiving() {
        synchronizer.getLogicalClock().tick(sendingServerClockValue);
        ServerInfo serverInfo = synchronizer.getServerInfo(sendingServerid);
        serverInfo.setServerState(ServerInfo.ServerState.JOIN);
        ServerCommand ackJoinCmd = new AckJoinServerCommand(new String[]{synchronizer.getMyState().name()}, synchronizer, Direction.Sending);
        synchronizer.sendCommandTo(sendingServerid, ackJoinCmd);
    }
//
//    private ServerCommand parseServerInput(String input) {
//        String[] tokens = input.split(" ");
//        String command = tokens[0].toLowerCase();
////        switch (command) {
////            case "join":
////                return new JoinServerCommand(tokens, clientSocket, synchronizer, Direction.Receiving);
////            case "request":
////                return new RequestServerCommand(tokens, clientSocket, synchronizer, Direction.Receiving);
////            case "ack":
////                return new AckServerCommand(tokens, clientSocket, synchronizer, Direction.Receiving);
////            case "release":
////                return new ReleaseServerCommand(tokens, clientSocket, synchronizer, Direction.Receiving);
////            default:
////                return new NullServerCommand();
////        }
//    }

}
