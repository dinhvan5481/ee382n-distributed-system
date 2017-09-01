package ut.ee382n.ds.core.commands;


public abstract class CommandFactory {

    public enum Protocol {
        UDP_CLIENT, UDP_SERVER
    }

    private static final UDPClientCommandFactory UDP_CLIENT_COMMAND_FACTORY = new UDPClientCommandFactory();
    private static final UDPServerCommandFactory UDP_SERVER_COMMAND_FACTORY = new UDPServerCommandFactory();

    static CommandFactory getFactory(Protocol protocol) {
        switch(protocol) {
            case UDP_CLIENT:
                return UDP_CLIENT_COMMAND_FACTORY;
            case UDP_SERVER:
                return UDP_SERVER_COMMAND_FACTORY;
        }
    }

    public abstract ICommand createPurchaseCommand(String[] params);

    public abstract ICommand createCancelCommand(String[] params);

    public abstract ICommand createSearchCommand(String[] params);

    public abstract ICommand createListCommand();

}