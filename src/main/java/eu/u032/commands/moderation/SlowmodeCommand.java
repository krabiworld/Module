package eu.u032.commands.moderation;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.Permission;

public class SlowmodeCommand extends Command {
    public SlowmodeCommand() {
        this.name = "slowmode";
        this.help = "Set slowmode in current channel";
        this.arguments = "<duration>";
        this.category = new Category("Moderation");
        this.userPermissions = new Permission[]{Permission.MANAGE_CHANNEL};
        this.botPermissions = new Permission[]{Permission.MANAGE_CHANNEL};
    }

    @Override
    protected void execute(CommandEvent event) {
        String args = event.getArgs();

        if (args.isEmpty()) {
            event.replyError("Required arguments are missing!");
            return;
        }

        int interval = Integer.parseInt(args);

        if (interval < 0 || interval > 21600) {
            event.replyError("Specify in seconds from 0 (off) to 21600.");
            return;
        }
        if (event.getTextChannel().getSlowmode() == interval) {
            event.replyError("This value already set.");
            return;
        }

        event.getTextChannel().getManager().setSlowmode(interval).queue();
        event.reactSuccess();
    }
}
