package Fusion.core;

public class AuxNodeFusedBackupServer {
    FusedNode fusedNode;

    private AuxNodeFusedBackupServer() {}
    public AuxNodeFusedBackupServer(FusedNode fusedNode) {
        this.fusedNode = fusedNode;
    }

    public FusedNode getFusedNode() {
        return fusedNode;
    }

    @Override
    public int hashCode() {
        return fusedNode.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof AuxNodeFusedBackupServer) {
            if(((AuxNodeFusedBackupServer)obj).fusedNode.equals(this.fusedNode)) {
                return true;
            }
        }
        return false;
    }
}
