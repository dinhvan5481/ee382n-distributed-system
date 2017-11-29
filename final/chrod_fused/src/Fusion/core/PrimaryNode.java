package Fusion.core;

public class PrimaryNode {
    private int key;
    private int value;
    private AuxNodePrimaryServer auxNode;

    private PrimaryNode(){}

    public PrimaryNode(int key, int value) {
        this.key = key;
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public AuxNodePrimaryServer getAuxNode() {
        return auxNode;
    }

    public void setAuxNode(AuxNodePrimaryServer auxNode) {
        this.auxNode = auxNode;
    }

    public int getKey() {
        return key;
    }
}
