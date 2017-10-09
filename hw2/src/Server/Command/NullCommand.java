package Server.Command;

import java.net.Socket;

public class NullCommand extends Command {
    public NullCommand(Socket clientSocket) {
        super(clientSocket);
    }

    @Override
    public String executeClientCmd(String[] tokens) {
        return "";
    }

    @Override
    public void executeServerCmd(String[] tokens) {
        return;
    }
}
