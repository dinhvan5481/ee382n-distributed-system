
public class NullCommand extends Command {
    public NullCommand(String[] tokens, ServerSynchronizer synchronizer) {
        super(tokens, synchronizer, CommandType.Null, Direction.Receiving);
        cmd = Command.NULL_CMD;
    }

    @Override
    protected void executeReceiving() {

    }
}
