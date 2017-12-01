package Fusion.communication;

import com.sun.tools.corba.se.idl.InvalidArgument;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class BackupServerRMIServer extends FusedBackupServerRMI {
    public static final String BACKUP_RMI_SERVICE_NAME = "fused";
    public BackupServerRMIServer() {
        super();
    }

    public static void main(String args[]) {
        try {
            String rmiServerName = args[0] + "/" + BACKUP_RMI_SERVICE_NAME;
            int portNumber = Integer.parseInt(args[1]);
            int numberOfBackupServers = Integer.parseInt(args[2]);
            List<Integer> coeff = new ArrayList<>(numberOfBackupServers);
            for (int i = 3; i < 3 + numberOfBackupServers; i++) {
                coeff.add(Integer.parseInt(args[i]));
            }
            FusedBackupServerRMI fusedBackupServer = new FusedBackupServerRMI(numberOfBackupServers, coeff);

            IFusedBackupServerRMI stub = (IFusedBackupServerRMI) UnicastRemoteObject.exportObject(fusedBackupServer, portNumber);
            Registry registry = LocateRegistry.getRegistry();
            registry.bind(rmiServerName, stub);
            System.out.println("RMI server is ready with registry name: " + rmiServerName + " at port: " + portNumber);

        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (AlreadyBoundException e) {
            e.printStackTrace();
        } catch (InvalidArgument invalidArgument) {
            invalidArgument.printStackTrace();
        }
    }
}
