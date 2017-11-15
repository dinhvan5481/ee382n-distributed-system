package Fusion.core;

public class AuxNodePrimaryServer {
    private PrimaryNode primaryNode;

    private AuxNodePrimaryServer() {

    }

    public AuxNodePrimaryServer(PrimaryNode primaryNode) {
        this.primaryNode = primaryNode;
        this.primaryNode.setAuxNode(this);
    }

    public PrimaryNode getPrimaryNode() {
        return primaryNode;
    }
}
