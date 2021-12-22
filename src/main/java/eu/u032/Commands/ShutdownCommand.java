package eu.u032.Commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class ShutdownCommand extends Command {

    public ShutdownCommand() {
        this.name = "shutdown";
        this.ownerCommand = true;
        this.hidden = true;
    }

    @Override
    protected void execute(CommandEvent event) {
        event.reactSuccess();
        event.getJDA().shutdown();
    }

}
