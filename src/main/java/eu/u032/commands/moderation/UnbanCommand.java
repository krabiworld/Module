package eu.u032.commands.moderation;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import eu.u032.Utils;
import net.dv8tion.jda.api.Permission;

public class UnbanCommand extends Command {
    public UnbanCommand() {
        this.name = "unban";
        this.help = "Unban member from server";
        this.arguments = "<ID>";
        this.category = new Category("Moderation");
        this.userPermissions = new Permission[]{Permission.BAN_MEMBERS};
        this.botPermissions = new Permission[]{Permission.BAN_MEMBERS};
    }

    @Override
    protected void execute(final CommandEvent event) {
        final String[] args = Utils.splitArgs(event.getArgs());

        if (args[0].isEmpty()) {
            event.replyError("Required arguments are missing!");
            return;
        }

        try {
            event.getGuild().unban(args[0]).queue();
            event.reactSuccess();
        } catch (Exception e) {
            event.replyError(e.getMessage());
        }
    }
}
