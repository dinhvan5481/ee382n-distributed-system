import Server.Server;

import java.io.*;

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

        Thread[] threads = new Thread[client_files.length + server_files.length];
        int i = 0;


        for (File file : client_files) {
            String fileOutPath = String.format("%s%s%s", outputDir, File.separator, file.getName().replaceFirst("(.*)[.][^.]+$", "$1.out"));

            threads[i] = createClientThread(file.getPath(), fileOutPath);
            threads[i].start();
            i++;
            System.out.println(file.getAbsolutePath());
        }

        for (File file : server_files) {
            String fileOutPath = String.format("%s%s%s", outputDir, File.separator, file.getName().replaceFirst("(.*)[.][^.]+$", "$1.out"));

            threads[i] = createServerThread(file.getPath(), fileOutPath);
            threads[i].start();
            i++;
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
