public class SystemInfo {


    private int myId;
    private LogicalClock logicalClock;

    public SystemInfo(int id, LogicalClock clock) {
        logicalClock = clock;
        myId = id;
    }

    public LogicalClock getLogicalClock() {
        return logicalClock;
    }

    public int getMyId() {
        return myId;
    }

}
