package eu.u032.Commands.Moderation;

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
        try {
            int interval = Integer.parseInt(event.getArgs());
            if (interval < 0 || interval > 21600) {
                event.replyError("Specify in seconds from 0 (off) to 21600.");
                return;
            }

            event.getTextChannel().getManager().setSlowmode(interval).queue();
            event.reactSuccess();
        } catch (Exception e) {
            event.replyError(e.getMessage());
        }
    }

}
