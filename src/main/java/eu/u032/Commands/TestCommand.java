package eu.u032.Commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import java.util.Arrays;
import java.util.List;

public class TestCommand extends Command {

    public TestCommand() {
        this.name = "test";
        this.hidden = true;
        this.ownerCommand = true;
    }

    @Override
    protected void execute(CommandEvent event) {
        List<String> args = Arrays.asList(event.getArgs().split("\\s+"));

        StringBuilder reason = new StringBuilder();
        for (int i = 1; args.get(1).length() >= i; i++) {
            reason.append(args.get(i)).append(" ");
        }

        event.reply(reason.toString());
    }
}
