package ut.ee382n.ds.server;

import ut.ee382n.ds.core.Helper;
import ut.ee382n.ds.core.Logger;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class StoreServerTCPHandler extends Thread {
    private Socket clientSocket;
    private BufferedReader inputStream;
    private DataOutputStream outputStream;

    private OnlineStore store;

    private Logger logger;

    public StoreServerTCPHandler(Socket clientSocket, OnlineStore store) throws IOException {
        this.clientSocket = clientSocket;
        this.store = store;

        this.inputStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        this.outputStream = new DataOutputStream(clientSocket.getOutputStream());

        this.logger = new Logger(Logger.LOG_LEVEL.DEBUG);
    }

    public void run() {
        String cmdFromClient;
        try {
            while ((cmdFromClient = inputStream.readLine()) != null) {
                logger.log(Logger.LOG_LEVEL.INFO, String.format("Received from client: %", cmdFromClient));
                String result = Helper.parseServerInput(store, cmdFromClient);
                sendTCPMessage(result);
                break;
            }
        } catch (IOException e) {
            logger.log(Logger.LOG_LEVEL.INFO, "Error while processing client command");
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                logger.log(Logger.LOG_LEVEL.INFO, "Error while closing client socket");
                e.printStackTrace();
                return;
            }
        }
    }

    private boolean sendTCPMessage(String message) {
        try {
            outputStream.writeBytes(message);
        } catch (IOException e) {
            logger.log(Logger.LOG_LEVEL.INFO, "Error while sending TCP result");
            return false;
        }
        return true;
    }
}
