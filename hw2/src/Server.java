
import java.io.InputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Server implements Runnable {

    InputStream input;
    PrintStream output;
    private int serverId;

    private Logger logger;
    public Server() {
        this(System.in, System.out);
        logger = new Logger(Logger.LOG_LEVEL.DEBUG);
    }

    public Server(InputStream input, PrintStream output) {
        this.input = input;
        this.output = output;
    }

    @Override
    public void run() {
        Scanner stdIn = new Scanner(input);

        int serverId = Integer.parseInt(stdIn.next());
        int nServers = Integer.parseInt(stdIn.next());
        int seats = Integer.parseInt(stdIn.next());
        stdIn.nextLine();


        ArrayList<String> servers = new ArrayList<>(nServers);
        for(int i = 0; i < nServers; i++){
            servers.add(stdIn.nextLine());
        }

        LogicalClock logicalClock = new LogicalClock();
        BookKeeper store = new BookKeeper(seats);
        SystemInfo systemInfo = new SystemInfo(serverId, logicalClock);
        ServerSynchronizer synchronizer = new ServerSynchronizer(serverId, logicalClock, store);

        List<Integer> neighboorServers = new LinkedList<>();

        int port = 0;
        for (int i = 0; i < nServers; i++) {
            String[] strServerInfo = servers.get(i).split(":");
            InetAddress serverIp = null;
            try {
                serverIp = InetAddress.getByName(strServerInfo[0]);
            } catch (UnknownHostException e) {
                System.out.println("Server input file is not correct.");
                e.printStackTrace();
                return;
            }
            ServerInfo serverInfo = new ServerInfo(i + 1, serverIp, Integer.parseInt(strServerInfo[1]));
            synchronizer.addServer(i + 1, serverInfo);

            if(i + 1 == serverId) {
                port = Integer.parseInt(strServerInfo[1]);
                serverInfo.setServerState(ServerInfo.ServerState.JOIN);
            } else {
                neighboorServers.add(i + 1);
            }
        }

        Thread synchronizeThread = new Thread(synchronizer);
        try {
            synchronizeThread.start();
        } catch (Exception e) {
            logger.log(Logger.LOG_LEVEL.INFO, "Failed to start sync thread - server " + serverId);
            e.printStackTrace();
        }

        try {
            synchronizeThread.join();
        } catch (InterruptedException e) {
            logger.log(Logger.LOG_LEVEL.INFO, "Server " + serverId + "stopped");
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