package Fusion.test;

import Fusion.communication.BackupServerRMIClient;

import java.rmi.RemoteException;

public class RMIClientTest {
    public static void main(String args[]) {
        BackupServerRMIClient backupServerRMIClient = null;
        try {
            backupServerRMIClient = new BackupServerRMIClient("b1");
            backupServerRMIClient.connectToServer();
        } catch (RemoteException e) {
            e.printStackTrace();
            return;
        }
//        backupServerRMIClient.sendMsg();
    }
}
