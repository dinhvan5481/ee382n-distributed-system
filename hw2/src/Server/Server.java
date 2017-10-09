package Server;

import Server.Core.*;
import Server.Core.SystemInfo;
import Server.Synchronize.LogicalClock;
import Server.Synchronize.*;
import Server.Utils.ServerTCPListener;


import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

public class Server implements Runnable {

    InputStream input;
    PrintStream output;
    private int serverId;

    public Server() {
        this(System.in, System.out);
    }

    public Server(InputStream input, PrintStream output) {
        this.input = input;
        this.output = output;
    }

    @Override
    public void run() {
        Scanner stdIn = new Scanner(input);
        LogicalClock logicalClock = new LogicalClock();

        int serverId = Integer.parseInt(stdIn.next());
        int nServers = Integer.parseInt(stdIn.next());
        int seats = Integer.parseInt(stdIn.next());


        ArrayList<String> servers = new ArrayList<>(nServers);
        for(int i = 0; i < nServers; i++){
            servers.add(stdIn.nextLine());
        }

        BookKeeper store = new BookKeeper(seats);
        SystemInfo systemInfo = new SystemInfo(serverId, logicalClock);
        ServerSynchronizer synchronizer = new ServerSynchronizer(serverId, logicalClock);

        int port = 0;
        for (int i = 0; i < nServers; i++) {
            String[] strServerInfo = servers.get(i).split(":");

            if(i != serverId - 1) {
                InetAddress serverIp = null;
                try {
                    serverIp = InetAddress.getByName(strServerInfo[0]);
                } catch (UnknownHostException e) {
                    System.out.println("Server input file is not correct.");
                    e.printStackTrace();
                    return;
                }
                ServerInfo serverInfo = new ServerInfo(i + 1, serverIp, Integer.parseInt(strServerInfo[1]));
                systemInfo.addServers(i + 1, serverInfo);
            } else {
                port = Integer.parseInt(strServerInfo[1]);
            }
        }

        ServerTCPListener tcpHandler;
        try {
            tcpHandler = new ServerTCPListener(port, store, synchronizer);
        } catch (IOException e) {
            System.out.println("Cannot initialize TCP Handler. Exit store");
            e.printStackTrace();
            return;
        }

        Thread tcpHandlerThread = new Thread(tcpHandler);

        try {
            tcpHandlerThread.start();
        } catch (Exception e) {
            System.out.println("Cannot run store TCP handler thread. Exit store");
            e.printStackTrace();
            return;
        }

        try {
            tcpHandlerThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        try {
            Server server = new Server();
            Thread serverThread = new Thread(server);
            serverThread.start();
            serverThread.join();
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }

    }
}