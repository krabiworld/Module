package eu.u032.Commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import eu.u032.Utils;
import net.dv8tion.jda.api.Permission;

public class UnbanCommand extends Command {

    public UnbanCommand() {
        this.name = "unban";
        this.userPermissions = new Permission[]{Permission.BAN_MEMBERS};
    }

    @Override
    protected void execute(CommandEvent event) {
        String[] args = event.getArgs().split("\\s+");

        if (args[0].isEmpty()) {
            event.replyError("Required arguments are missing!");
            return;
        }

        try {
            event.getGuild().unban(args[0]).complete();
            event.reactSuccess();
        } catch (Exception e) {
            event.replyError(e.getMessage());
        }
    }

}
