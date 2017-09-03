package ut.ee382n.ds.core.commands.server;

import ut.ee382n.ds.core.ICommand;
import ut.ee382n.ds.core.OnlineStore;

/**
 * Created by dinhvan5481 on 8/29/17.
 */
public abstract class BaseStoreCommand implements ICommand {

    protected OnlineStore store;
    protected String result;
    protected BaseStoreCommand(OnlineStore store) {
        this.store = store;
    }

    public abstract void execute();

    public String getResult() {
        return this.result;
    }
}
