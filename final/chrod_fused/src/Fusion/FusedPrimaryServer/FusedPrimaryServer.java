package Fusion.FusedPrimaryServer;

import Fusion.core.AuxNodeFusedBackupServer;
import Fusion.core.AuxNodePrimaryServer;
import Fusion.core.PrimaryNode;

import java.util.LinkedList;
import java.util.Optional;

public class FusedPrimaryServer {
    private int serverId;
    private LinkedList<AuxNodePrimaryServer> auxNodes;
    private LinkedList<PrimaryNode> primaryLinkedList;

    private FusedPrimaryServer() {}
    public FusedPrimaryServer(int serverId) {
        this.serverId = serverId;
        auxNodes = new LinkedList<>();
        primaryLinkedList = new LinkedList<>();
    }

    public void upsert(int key, int value) {
        int oldValue = 0;
        PrimaryNode primaryNode = getPrimaryNodeWithKey(key);
        if(primaryNode != null) {
            oldValue = primaryNode.getValue();
            primaryNode.setValue(value);
        } else {
            primaryNode = new PrimaryNode(key, value);
            AuxNodePrimaryServer auxNode = new AuxNodePrimaryServer(primaryNode);
            primaryNode.setAuxNode(auxNode);
            auxNodes.add(auxNode);
        }
        send(serverId, key, value, oldValue);
    }

    private PrimaryNode getPrimaryNodeWithKey(int key) {
        Optional<PrimaryNode> resultOpt = primaryLinkedList.stream().filter(primaryNode -> primaryNode.getKey() == key).findFirst();
        if(resultOpt.isPresent()) {
            return resultOpt.get();
        } else {
            return null;
        }
    }

    private void send(int serverId, int key, int newValue, int oldValue) {
        return;
    }
}
