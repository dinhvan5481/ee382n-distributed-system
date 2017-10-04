import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class MultipleClientSimulator {

    private static Thread createClientThread(String inputFilePath, String outputFilePath) {
        try {
            FileInputStream threadInput = new FileInputStream(new File(inputFilePath));
            PrintStream threadOutput = new PrintStream(new File(outputFilePath));
            Client client = new Client(threadInput, threadOutput);
            return new Thread(client);
        } catch (UnknownHostException uhe) {
            System.err.println(uhe);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Cannot create TCP/UDP client protocol");
        }
        return null;
    }

    public static void main(String[] args) {
        String inputDir = "clients_input";
        String outputDir = "clients_output";
        String[] files = {"client1.txt", "client2.txt"};

        Thread[] threads = new Thread[files.length];

        for(int i = 0; i < files.length; i++) {
            String filePath = String.format("%s/%s", inputDir, files[i]);
            String fileOutPath = String.format("%s/%s", outputDir, files[i]);

            threads[i] = createClientThread(filePath, fileOutPath);
            threads[i].start();
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
