package ut.ee382n.ds.core;

/**
 * Created by dinhvan5481 on 8/29/17.
 */
public abstract class BaseStoreCommand implements ICommand {

    protected OnlineStore store;
    protected BaseStoreCommand(OnlineStore store) {
        this.store = store;
    }

    public abstract void execute();
}
