package Server.Command.Client;

import Server.BookKeeper;
import Server.Command.Command;
import Server.Core.ITCPConnection;
import Server.Core.ServerRequest;
import Server.Synchronize.ServerSynchronizer;

import java.io.PrintStream;
import java.net.Socket;

public abstract class ClientCommand extends Command {
    public static String RESERVE_CMD = "reserve";
    public static String BOOK_SEAT_CMD = "bookseat";
    public static String DELETE_CMD = "delete";
    public static String SEARCH_CMD = "search";

    protected BookKeeper store;
    protected ServerSynchronizer synchronizer;
    private ITCPConnection tcpConnection;

    public ClientCommand(String[] tokens, ServerSynchronizer synchronizer) {
        super(tokens, synchronizer, CommandType.Client, Direction.Receiving);
        this.store = synchronizer.getStore();
    }

    public final void executeInCS() {
        if(tcpConnection == null) {
            throw new IllegalStateException("No TCP Client connection set for this command");
        }
        if(synchronizer.getRequestEnterCSState() == ServerSynchronizer.RequestEnterCSState.OK) {
            tcpConnection.sendTCPMessage(executeWhenInCS());
            synchronizer.exitCS();
        } else {
            throw new IllegalStateException("Execute command " + cmd + " when not allow in CS");
        }
    }

    public void setTcpConnection(ITCPConnection tcpConnection) {
        this.tcpConnection = tcpConnection;
    }

    protected abstract String executeWhenInCS();



    @Override
    protected void executeReceiving() {
        ServerRequest request = new ServerRequest(synchronizer.getId(), synchronizer.getLogicalClock().tick(), this);
        synchronizer.requestCS(request);
    }

}
