package Server.Command.Server;

import Server.Synchronize.ServerSynchronizer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

public class SyncResponseServerCommand extends ServerCommand {

    public SyncResponseServerCommand(String[] tokens, ServerSynchronizer synchronizer, Direction cmdDirection) {
        super(tokens, synchronizer, cmdDirection);
        cmd = SYNC_RESPONSE_CMD;
    }

    @Override
    protected void executeReceiving() {
        String syncStore = additionalInfos.get(0);
        if(!syncStore.equals("-1")) {
            Pattern p = compile("(\\((\\d+),(\\w+)\\))");
            Matcher matcher = p.matcher(syncStore);
            while (matcher.find()) {
                int seatNum = Integer.parseInt(matcher.group(2));
                String name = matcher.group(3);
                synchronizer.getStore().bookSeat(name, seatNum);
            }
        }
        synchronizer.endSyncStore();
    }
}
