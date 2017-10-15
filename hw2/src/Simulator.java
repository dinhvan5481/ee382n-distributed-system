import java.io.*;
import java.util.LinkedList;

public class Simulator {

    private static Thread createClientThread(String inputFilePath, String outputFilePath) {
        try {
            FileInputStream threadInput = new FileInputStream(new File(inputFilePath));
            PrintStream threadOutput = new PrintStream(new File(outputFilePath));
            Client client = new Client(threadInput, threadOutput);
            return new Thread(client);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Cannot create TCP/UDP client protocol");
        }
        return null;
    }

    private static Thread createServerThread(String inputFilePath, String outputFilePath) {
        try {
            FileInputStream threadInput = new FileInputStream(new File(inputFilePath));
            PrintStream threadOutput = new PrintStream(new File(outputFilePath));
            Server server = new Server(threadInput, threadOutput);
            return new Thread(server);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Cannot create TCP/UDP client protocol");
        }
        return null;
    }

    public static void main(String[] args) {
        String inputDir = "clients_input";
        String serverDir = "servers_input";
        String outputDir = "standard_output";

        File client_input = new File(inputDir);
        File servers_input = new File(serverDir);

        File[] client_files = client_input.listFiles();
        File[] server_files = servers_input.listFiles();

        LinkedList<Thread> threads = new LinkedList<>();
        int i = 0;


        for (File file : client_files) {
            String fileOutPath = String.format("%s%s%s", outputDir, File.separator, file.getName().replaceFirst("(.*)[.][^.]+$", "$1.out"));
            Thread clientThread = createClientThread(file.getPath(), fileOutPath);
            threads.add(clientThread);
            clientThread.start();
            System.out.println(file.getAbsolutePath());
        }

        for (File file : server_files) {
            if(file.getName().contains("server1")) {
                continue;
            }
            String fileOutPath = String.format("%s%s%s", outputDir, File.separator, file.getName().replaceFirst("(.*)[.][^.]+$", "$1.out"));
            Thread serverThread = createServerThread(file.getPath(), fileOutPath);
            threads.add(serverThread);
            serverThread.start();
            System.out.println(file.getAbsolutePath());
        }

        for (Thread t : threads) {
            if (t != null) {
                try {
                    t.join();
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }
        }
    }
}
