import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class  TCPClientProtocol extends ClientProtocol {
    private Socket tcpSocket;
    private PrintStream tcpOutputStream;
    private Scanner tcpInputStream;

    private Logger logger;

    public TCPClientProtocol(InetAddress address, int port) throws IOException {
        super(address, port);
        this.tcpSocket = new Socket(serverAddress, port);
        this.tcpOutputStream = new PrintStream(this.tcpSocket.getOutputStream());
        this.tcpInputStream = new Scanner(this.tcpSocket.getInputStream());

        logger = new Logger(Logger.LOG_LEVEL.DEBUG);
    }

    public String sendMessageAndReceiveResponse(String message) {
        sendMessage(message);

        StringBuilder sb = new StringBuilder();
        while (tcpInputStream.hasNextLine()) {
            String line = tcpInputStream.nextLine();
            // All responses from server send an extra new line to
            // let know the client the response has ended.
            if(line.length() == 0) {
                break;
            }
            sb.append(line);
            sb.append("\n");
        }
        return sb.toString();
    }

    private void sendMessage(String message) {
        tcpOutputStream.println(message);
        tcpOutputStream.flush();
    }

}