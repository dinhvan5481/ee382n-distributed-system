package ut.ee382n.ds.core;

/**
 * Created by dinhvan5481 on 8/30/17.
 */
public class CommandOperand {
    private ICommand command;
    private String[] operands;
    public CommandOperand(ICommand command, String[] operands) {
        this.command = command;
        this.operands = operands;
    }

    public ICommand getCommand() {
        return command;
    }

    public String[] getOperands() {
        return operands;
    }
}
