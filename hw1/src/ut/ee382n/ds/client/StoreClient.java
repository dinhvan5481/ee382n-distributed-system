package ut.ee382n.ds.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class StoreClient implements IStoreClient, Runnable {

    ClientProtocol udpClient;
    ClientProtocol tcpClient;
    ClientProtocol protocolChosen;
    InetAddress serverAddress;
    int udpPort;
    int tcpPort;
    InputStream input;
    PrintStream output;

    public StoreClient(InetAddress address, int udpPort, int tcpPort) throws IOException {
        this(address, udpPort, tcpPort, System.in, System.out);
    }

    public StoreClient(InetAddress address, int udpPort, int tcpPort, InputStream input, PrintStream output) throws IOException {
        this.serverAddress = address;
        this.udpPort = udpPort;
        this.tcpPort = tcpPort;
        udpClient = new UDPClientProtocol(address, udpPort);
        tcpClient = new TCPClientProtocol(address, tcpPort);
        protocolChosen = tcpClient;
        this.input = input;
        this.output = output;
    }

    public void setMode(String type) {
        if(type.equals("U")) {
            protocolChosen = udpClient;
        } else if (type.equals("T")){
            protocolChosen = tcpClient;
        }
    }

    public String sendMessageAndReceiveResponse(String message) {
        return protocolChosen.sendMessageAndReceiveResponse(message);
    }

    @Override
    public void run() {
        Scanner stdin = new Scanner(input);

        while(stdin.hasNextLine()) {
            String command = stdin.nextLine();
            if (command.length() == 0) continue;

            String[] tokens = command.split(" ");
            if (tokens[0].equals("setmode")) {
                setMode(tokens[1]);
            } else if(tokens[0].equals("exit")) {
                break;
            } else {
                output.print(sendMessageAndReceiveResponse(command));
            }
        }
    }


    public static void main(String[] args) {
        String hostname = "localhost";
        final int UDP_PORT = 2040;
        final int TCP_PORT = 2050;

        try {
            StoreClient client = new StoreClient(InetAddress.getByName(hostname), UDP_PORT, TCP_PORT);
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