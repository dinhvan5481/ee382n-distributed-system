package ut.ee382n.ds.core.commands;

public class UDPServerCommandFactory extends CommandFactory {

    public ICommand createPurchaseCommand(String[] params) {
        return new UDPPurchaseServerCommand(params);
    }

    public ICommand createCancelCommand(String[] params) {
        return new UDPCancelServerCommand(params);
    }

    public ICommand createSearchCommand(String[] params) {
        return new UDPSearchServerCommand(params);
    }

    public ICommand createListCommand() {
        return new UDPListServerCommand();
    }

}