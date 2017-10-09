package Server.Utils;

import Server.BookKeeper;
import Server.Server.*;
import Server.Synchronize.ServerSynchronizer;
import Server.Utils.ServerTCPHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerTCPListener implements Runnable {
    private int welcomePort;
    private ServerSocket serverSocket;
    private BookKeeper store;
    private ServerSynchronizer synchronizer;


    private Logger logger;

    public ServerTCPListener(int port, BookKeeper store, ServerSynchronizer synchronizer) throws IOException {
        this.welcomePort = port;
        serverSocket = new ServerSocket(welcomePort);
        this.store = store;
        this.synchronizer = synchronizer;
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

            ServerTCPHandler serverTCPHandler = null;
            try {
                serverTCPHandler = new ServerTCPHandler(incomingClientSocket, store, synchronizer);
            } catch (IOException e) {
                logger.log(Logger.LOG_LEVEL.INFO, String.format("Error while creating TCP handler"));
                e.printStackTrace();
                continue;
            }
            serverTCPHandler.start();
        }
    }
}
