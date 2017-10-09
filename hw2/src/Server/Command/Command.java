package Server.Command;

import java.net.Socket;

public abstract class Command {
    public static enum CommandType {
        Client,
        Server
    }

    protected CommandType type;
    protected Socket clientSocket;

    protected Command(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }



    public CommandType getCommandType() {
        return type;
    }

    public abstract String executeClientCmd(String[] tokens);
    public abstract void executeServerCmd(String[] tokens);

    public void execute() {
//        if(type == CommandType.Client) {
//            executeClientCmd(tokens);
//        } else {
//            executeServerCmd(tokens);
//        }
    }
}
