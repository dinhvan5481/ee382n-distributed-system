package ut.ee382n.ds.core;

import ut.ee382n.ds.core.commands.server.ListCommand;

/**
 * Created by dinhvan5481 on 8/29/17.
 */
public class Helper {
    public static CommandOperand parseServerInput(OnlineStore store, String input) {
        String[] tokens = input.split(" ");
        if(tokens.length == 1 && tokens[0] == "list") {
            return new CommandOperand((ICommand) new ListCommand(store), new String[]{});
        }
        return new CommandOperand((ICommand) new ListCommand(store), new String[]{});
    }

    public static String[] emptyOperands() {
        return new String[]{};
    }
}
