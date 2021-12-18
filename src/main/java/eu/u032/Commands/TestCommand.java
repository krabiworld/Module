package eu.u032.Commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class TestCommand extends Command {

    public TestCommand() {
        this.name = "test";
        this.ownerCommand = true;
        this.hidden = true;
    }

    @Override
    protected void execute(CommandEvent event) {
        event.replyWarning("Emoji warning: " + event.getClient().getWarning());
        event.reactWarning(); // Unknown Emoji, aaaaaaaaaaaaaaaaaaa
    }
}
