package ut.ee382n.ds.core;

/**
 * Created by quachv on 8/25/2016.
 */
public class Logger {
    public enum LOG_LEVEL {
        DEBUG,
        INFO
    }

    private LOG_LEVEL logLevel;

    public Logger(LOG_LEVEL level) {
        this.logLevel = level;
    }

    public void setLogLevel(LOG_LEVEL value) {
        this.logLevel = value;
    }

    public void log(LOG_LEVEL logType, String message) {
        if(this.logLevel == LOG_LEVEL.DEBUG | (this.logLevel == LOG_LEVEL.INFO && logType == LOG_LEVEL.INFO)) {
            System.out.println(message);
        }
    }
}
