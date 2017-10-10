package Server.Command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public abstract class Command {
    public static enum CommandType {
        Client,
        Server
    }

    protected CommandType type;
    protected Socket clientSocket;
    protected BufferedReader inputStream;
    protected PrintStream outputStream;
    protected String[] tokens;

    protected Command(String[] tokens, Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.tokens = tokens;

    }

    public CommandType getCommandType() {
        return type;
    }

    public abstract void execute();

    public void setSocket(Socket socket) throws IOException {
        this.clientSocket = socket;
    }


    public void setOutputStream(PrintStream outputStream) {
        this.outputStream = outputStream;
    }

    protected void sendTCPMessage(String message) {
        if(clientSocket.isConnected() && !clientSocket.isOutputShutdown()) {
            outputStream.println(message);
            outputStream.flush();
        }
    }
}
