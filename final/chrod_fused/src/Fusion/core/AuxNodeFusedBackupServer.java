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
}
