import java.util.LinkedList;
import java.util.List;

public abstract class ServerCommand {
    protected String cmd;
    protected int serverId;
    protected long clockValue;
    protected List<String> additionalInfos;

    protected ServerCommand(){}
    protected ServerCommand(int serverId, long clockValue) {
        this.cmd = "base";
        this.serverId = serverId;
        this.clockValue = clockValue;
        additionalInfos = new LinkedList<>();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(cmd + " " + clockValue + " " + serverId);
        if(additionalInfos != null) {
            for (String additionalInfo :
                    additionalInfos) {
                sb.append(" " + additionalInfo);
            }
        }
        return sb.toString();
    }
}
