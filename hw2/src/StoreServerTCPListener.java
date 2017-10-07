import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class StoreServerTCPListener implements Runnable {
    private int welcomePort;
    private ServerSocket serverSocket;
    private BookKeeper store;

    private Logger logger;

    public StoreServerTCPListener(int port, BookKeeper store) throws IOException {
        this.welcomePort = port;
        serverSocket = new ServerSocket(welcomePort);
        this.store = store;
        logger = new Logger(Logger.LOG_LEVEL.DEBUG);
    }

    @Override
    public void run() {
        while (true) {
            Socket incomingClientSocket;
            try {
                incomingClientSocket = serverSocket.accept();
            } catch (IOException e) {
                logger.log(Logger.LOG_LEVEL.INFO, String.format("Error while accepting TCP connection"));
                e.printStackTrace();
                continue;
            } catch (Exception e) {
                logger.log(Logger.LOG_LEVEL.INFO, String.format("Unexpected error happened"));
                e.printStackTrace();
                continue;
            }

            StoreServerTCPHandler serverTCPHandler = null;
            try {
                serverTCPHandler = new StoreServerTCPHandler(incomingClientSocket, store);
            } catch (IOException e) {
                logger.log(Logger.LOG_LEVEL.INFO, String.format("Error while creating TCP handler"));
                e.printStackTrace();
                continue;
            }
            serverTCPHandler.start();
        }
    }
}
