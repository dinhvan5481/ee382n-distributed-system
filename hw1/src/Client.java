import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

public class Client implements IStoreClient, Runnable {

    TCPClientProtocol tcpClient;

    InetAddress serverAddress;
    int udpPort;
    int tcpPort;
    InputStream input;
    PrintStream output;

    public Client() throws IOException {
        this(System.in, System.out);
    }

    public Client(InputStream input, PrintStream output) throws IOException {
        this.input = input;
        this.output = output;
    }

    public String sendMessageAndReceiveResponse(String message) {
        return tcpClient.sendMessageAndReceiveResponse(message);
    }

    @Override
    public void run() {
        Scanner stdin = new Scanner(input);

        int nServers = Integer.parseInt(stdin.nextLine());
        ArrayList<String> servers = new ArrayList<>(nServers);

        for(int i = 0; i < nServers; i++) {
            servers.add(stdin.nextLine());
        }

        tcpClient = new TCPClientProtocol(servers);

        while(stdin.hasNextLine()) {
            String command = stdin.nextLine();
            output.println(sendMessageAndReceiveResponse(command));
        }
    }


    public static void main(String[] args) {
        try {
            Client client = new Client();
            Thread clientThread = new Thread(client);
            clientThread.start();
            clientThread.join();
        } catch (UnknownHostException uhe) {
            System.err.println(uhe);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Cannot create TCP/UDP client protocol");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}