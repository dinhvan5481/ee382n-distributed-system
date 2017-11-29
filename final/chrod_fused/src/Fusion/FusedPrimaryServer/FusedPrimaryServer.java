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
        send(key, oldValue, value);
    }

    public void delete(int key) {
        PrimaryNode deleteingNode = getPrimaryNodeWithKey(key);
        if(deleteingNode == null) {
            return;
        }
        int oldValue = deleteingNode.getValue();
        AuxNodePrimaryServer auxTailNode = auxNodes.getLast();
        int tosValue = auxTailNode.getPrimaryNode().getValue();

        sendDelete(key, oldValue, tosValue);
        AuxNodePrimaryServer deletingAuxNode = deleteingNode.getAuxNode();
        deletingAuxNode.setPrimaryNode(auxTailNode.getPrimaryNode());
        //Remove reference of primary node so that GC can collect aux node
        auxTailNode.setPrimaryNode(null);
        auxNodes.remove(auxTailNode);
    }

    private PrimaryNode getPrimaryNodeWithKey(int key) {
        Optional<PrimaryNode> resultOpt = primaryLinkedList.stream().filter(primaryNode -> primaryNode.getKey() == key).findFirst();
        if(resultOpt.isPresent()) {
            return resultOpt.get();
        } else {
            return null;
        }
    }

    private void send(int key, int oldValue, int newValue) {
        return;
    }
    private void sendDelete(int key, int oldValue, int newValue) {
        return;
    }
}
