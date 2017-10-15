public class LogicalClock {
    private long clockValue;

    public LogicalClock() {
        clockValue = 0;
    }

    public synchronized long getCurrentClock() {
        return clockValue;
    }

    public synchronized long tick(long receivedClockValue) {
        if(clockValue < receivedClockValue) {
            this.clockValue = receivedClockValue + 1;
        } else {
            return tick();
        }
        return clockValue;
    }

    public synchronized long tick() {
        return ++clockValue;
    }
}
