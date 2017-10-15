package Server.Command;

import Server.BookKeeper;
import Server.Command.Client.*;
import Server.Command.Server.*;
import Server.Core.ServerInfo;
import Server.Server;
import Server.Synchronize.ServerSynchronizer;
import Server.Utils.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

public abstract class Command {
    public static enum CommandType {
        Client,
        Server,
        Null
    }

    public enum Direction {
        Receiving,
        Sending
    }

    public static String NULL_CMD = "null";

    protected int serverId;
    protected CommandType type;
    protected String cmd;
    protected String[] tokens;
    protected List<String> additionalInfos;
    protected ServerSynchronizer synchronizer;
    protected int sendingServerid;
    protected long sendingServerClockValue;
    protected ServerInfo receivedServerInfo;
    protected ServerCommand.Direction cmdDirection;

    protected Logger logger;
    protected Command(String[] tokens, ServerSynchronizer synchronizer, CommandType type,ServerCommand.Direction cmdDirection) {
        this.serverId = synchronizer.getId();
        this.synchronizer = synchronizer;
        this.type = type;
        this.cmdDirection = cmdDirection;
        additionalInfos = new LinkedList<>();
        this.tokens = tokens;
        if(cmdDirection == ServerCommand.Direction.Receiving && getCommandType().equals(CommandType.Server)) {
            if(tokens != null && tokens.length >= 3) {
                this.cmd = tokens[0];
                this.sendingServerid = Integer.parseInt(tokens[1]);
                this.sendingServerClockValue = Long.parseLong(tokens[2]);
                for (int i = 3; i < tokens.length; i++) {
                    additionalInfos.add(tokens[i]);
                }
            }
        } else {
            if(tokens != null) {
                for (String token : tokens) {
                    additionalInfos.add(token);
                }
            }
        }
        if(getCommandType() == CommandType.Server) {
            this.receivedServerInfo = synchronizer.getServerInfo(sendingServerid);
        }
        logger = new Logger(Logger.LOG_LEVEL.DEBUG);
    }

    public CommandType getCommandType() {
        return type;
    }

    protected abstract void executeReceiving();

    public final void executeReceivingCmd() {
        if(cmdDirection == Direction.Receiving && synchronizer.getMyState() == ServerInfo.ServerState.READY) {
            synchronizer.getLogicalClock().tick(sendingServerClockValue);
            executeReceiving();
        }

    }

    public int getSendingServerid() {
        if(type == CommandType.Server && cmdDirection == Direction.Receiving) {
            return sendingServerid;
        } else {
            return -1;
        }
    }

    public String[] getCmdTokens() {
        return tokens;
    }

    @Override
    public String toString() {
        if(tokens != null) {
            StringBuilder sb = new StringBuilder();
            Stream.of(tokens).forEach(token -> sb.append(token + " "));
            return sb.toString();
        }
        return "no tokens";
    }

    public static Command parseCommand(String input, ServerSynchronizer synchronizer) {
        String[] tokens = input.split(" ");
        String command = tokens[0].toLowerCase();

        if (command.equals(ClientCommand.RESERVE_CMD)) {
            return new ReserveClientCommand(tokens, synchronizer);
        } else if (command.equals(ClientCommand.BOOK_SEAT_CMD)) {
            return new BookSeatClientCommand(tokens, synchronizer);
        } else if (command.equals(ClientCommand.DELETE_CMD)) {
            return new DeleteClientCommand(tokens, synchronizer);
        } else if (command.equals(ClientCommand.SEARCH_CMD)) {
            return new SearchClientCommand(tokens, synchronizer);
        } else if (command.equals(ServerCommand.JOIN_CMD)) {
            return new JoinServerCommand(tokens, synchronizer, ServerCommand.Direction.Receiving);
        } else if (command.equals(ServerCommand.REQUEST_CMD)) {
            return new RequestServerCommand(tokens, synchronizer, ServerCommand.Direction.Receiving);
        } else if (command.equals(ServerCommand.ACK_CMD)) {
            return new AckServerCommand(tokens, synchronizer, ServerCommand.Direction.Receiving);
        } else if (command.equals(ServerCommand.RELEASE_CMD)) {
            return new ReleaseServerCommand(tokens, synchronizer, ServerCommand.Direction.Receiving);
        } else if (command.equals(ServerCommand.ACK_JOIN_CMD)) {
            return new AckJoinServerCommand(tokens, synchronizer, ServerCommand.Direction.Receiving);
        } else if (command.equals(ServerCommand.SYNC_REQUEST_CMD)) {
            return new SyncRequestServerCommand(tokens, synchronizer, ServerCommand.Direction.Receiving);
        } else if (command.equals(ServerCommand.SYNC_RESPONSE_CMD)) {
            return new SyncResponseServerCommand(tokens, synchronizer, ServerCommand.Direction.Receiving);
        } else if(command.equals(ServerCommand.SYNC_STATE_CMD)) {
            return new SyncStateServerCommand(tokens, synchronizer, Direction.Receiving);
        }
        else {
            return new NullCommand(tokens, synchronizer);
        }

    }
}
