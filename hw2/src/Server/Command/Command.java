package Server.Command;

import Server.Synchronize.ServerSynchronizer;
import Server.Utils.Logger;

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

    protected Logger logger;

    protected Command(String[] tokens, ServerSynchronizer serverSynchronizer) {
        this.tokens = tokens;
        logger = new Logger(Logger.LOG_LEVEL.DEBUG);
    }

    public CommandType getCommandType() {
        return type;
    }

    public abstract void execute();
}
