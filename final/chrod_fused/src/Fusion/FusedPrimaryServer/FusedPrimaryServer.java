package Fusion.FusedPrimaryServer;

import Fusion.communication.RMIAgent;
import Fusion.core.AuxNodePrimaryServer;
import Fusion.core.PrimaryNode;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class FusedPrimaryServer {
    private int serverId;
    private LinkedList<AuxNodePrimaryServer> auxNodes;
    private LinkedList<PrimaryNode> primaryLinkedList;
    private RMIAgent rmiAgent;

    private FusedPrimaryServer() {}
    public FusedPrimaryServer(int serverId, RMIAgent rmiAgent) {
        this.serverId = serverId;
        auxNodes = new LinkedList<>();
        primaryLinkedList = new LinkedList<>();
        this.rmiAgent = rmiAgent;
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
            primaryLinkedList.add(primaryNode);
        }
        send(key, value, oldValue);
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

    public List<Integer> getFusedLinkedList(int serverId) {
        return null;
    }

    private PrimaryNode getPrimaryNodeWithKey(int key) {
        Optional<PrimaryNode> resultOpt = primaryLinkedList.stream().filter(primaryNode -> primaryNode.getKey() == key).findFirst();
        if(resultOpt.isPresent()) {
            return resultOpt.get();
        } else {
            return null;
        }
    }

    private void send(int key, int newValue, int oldValue) {
        rmiAgent.broadcastUpsert(serverId, key, newValue, oldValue);
    }


    private void sendDelete(int key, int oldValue, int tosValue) {
        rmiAgent.broadcastDelete(serverId, key, oldValue, tosValue);
    }
}
