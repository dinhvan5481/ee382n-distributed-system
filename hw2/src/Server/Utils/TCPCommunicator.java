package Server.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class TCPCommunicator {
    private BufferedReader inputStream;
    private PrintStream outputStream;
    private Socket socket;

    public TCPCommunicator(Socket socket) throws IOException {
        this.socket = socket;
        this.inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.outputStream = new PrintStream(socket.getOutputStream());
    }

    private void sendTCPMessage(String message) {
        outputStream.println(message);
        outputStream.flush();
    }
}
