import java.io.*;
import java.net.Socket;

public class ServerTCPHandler extends Thread {
    private Socket clientSocket;
    private BufferedReader inputStream;
    private PrintStream outputStream;
    private BookKeeper store;
    private Logger logger;

    public ServerTCPHandler(Socket clientSocket, BookKeeper store) throws IOException {
        this.clientSocket = clientSocket;
        this.store = store;

        this.inputStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        this.outputStream = new PrintStream(clientSocket.getOutputStream());

        this.logger = new Logger(Logger.LOG_LEVEL.DEBUG);
    }

    public void run() {
        String cmdFromClient;
        try {
            while ((cmdFromClient = inputStream.readLine()) != null) {
                logger.log(Logger.LOG_LEVEL.INFO, String.format("Received from client: %s", cmdFromClient));
                String result = Parser.parseServerInput(store, cmdFromClient);
                sendTCPMessage(result);
            }
        } catch (IOException e) {
            logger.log(Logger.LOG_LEVEL.INFO, "Connection with client ended.");
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

    private void sendTCPMessage(String message) {
        outputStream.println(message);
        outputStream.flush();
    }
}
