package Fusion.core;

public class AuxNodePrimaryServer {
    private PrimaryNode primaryNode;

    private AuxNodePrimaryServer() {

    }

    public AuxNodePrimaryServer(PrimaryNode primaryNode) {
        this.primaryNode = primaryNode;
    }

    public PrimaryNode getPrimaryNode() {
        return primaryNode;
    }

    public void setPrimaryNode(PrimaryNode primaryNode) {
        this.primaryNode = primaryNode;
    }
}
