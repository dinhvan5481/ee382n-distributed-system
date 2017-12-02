package Fusion.communication;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class RMIAgent {
    private List<String> serverRMIIDs;
    private List<BackupServerRMIClient> rmiClients;
    public RMIAgent(List<String> serverRMIIDs) {
        this.serverRMIIDs = serverRMIIDs;
        rmiClients = new ArrayList<>(serverRMIIDs.size());
        this.serverRMIIDs.forEach(id -> {
            try {
                BackupServerRMIClient rmiClient = new BackupServerRMIClient(id);
                rmiClient.connectToServer();
                rmiClients.add(rmiClient);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }

    public void broadcastUpsert(int serverId, int key, int newValue, int oldValue) {
        rmiClients.parallelStream().forEach(rmiClient -> {
            try {
                rmiClient.getRMIClient().upsert(serverId, key, newValue, oldValue);
            } catch (RemoteException e) {
                System.out.println("Error while sending upsert to: " + rmiClient.getServerRMIID());
                e.printStackTrace();
            }
        });
    }

    public void broadcastDelete(int serverId, int key, int deleteValue, int tosValue) {
        rmiClients.parallelStream().forEach(rmiClient -> {
            try {
                rmiClient.getRMIClient().delete(serverId, key, deleteValue, tosValue);
            } catch (RemoteException e) {
                System.out.println("Error while sending upsert to: " + rmiClient.getServerRMIID());
                e.printStackTrace();
            }
        });
    }

    public HashMap<String, List<Integer>> broadcastRecoverValue(int serverId) {
        HashMap<String, List<Integer>> result;
        result = new HashMap<>();
        rmiClients.parallelStream().forEach(rmiClient -> {
            try {
                result.put(rmiClient.getServerRMIID(), rmiClient.getRMIClient().retrieveAuxList(serverId));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
        return result;
    }
}
