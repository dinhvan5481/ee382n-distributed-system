package Fusion.test;

import Fusion.FusedBackupServer.FusedBackupServer;

import java.io.Console;

public class serverstest {

    public FusedBackupServer fusedBackupServer;

    public serverstest(int numOfServers) {
        fusedBackupServer = new FusedBackupServer(numOfServers);
    }

    public static void main(String[] args) {
        int numOfServers = 3;
        serverstest server = new serverstest(numOfServers);
        for(int i = 0; i < 10; i++) {
            for (int j = 0; j < numOfServers; j++) {
                server.fusedBackupServer.upsert(j, i, i, 0);
            }
        }

        for(int i = 0; i < 10; i++) {
            for (int j = 0; j < numOfServers; j++) {
                server.fusedBackupServer.upsert(j, i, i + 1, 0);
            }
        }
        for(int key = 0; key < 10; key++) {
            server.fusedBackupServer.delete(0, key, key + 1, 0);
        }
        for(int key = 0; key < 10; key++) {
            server.fusedBackupServer.upsert(0, key, key + 1, 0);
        }
        Console console = System.console();
        console.readLine();

    }
}
