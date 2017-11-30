package Fusion.communication;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class BackupServerRMIServer extends FusedBackupServer {
    public static final String BACKUP_RMI_SERVICE_NAME = "fused";
    public BackupServerRMIServer() {
    }

    public static void main(String args[]) {
        try {
            String rmiServerName = args[0] + "/" + BACKUP_RMI_SERVICE_NAME;
            int portNumber = Integer.parseInt(args[1]);
            FusedBackupServer fusedBackupServer = new FusedBackupServer();

            IFusedBackupServerRMI stub = (IFusedBackupServerRMI) UnicastRemoteObject.exportObject(fusedBackupServer, portNumber);
            Registry registry = LocateRegistry.getRegistry();
            registry.bind(rmiServerName, stub);
            System.out.println("RMI server is ready with registry name: " + rmiServerName + " at port: " + portNumber);

        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (AlreadyBoundException e) {
            e.printStackTrace();
        }
    }
}
