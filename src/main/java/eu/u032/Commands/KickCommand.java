package eu.u032.Commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import eu.u032.Utils;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;

public class KickCommand extends Command {

    public KickCommand() {
        this.name = "kick";
        this.userPermissions = new Permission[]{Permission.KICK_MEMBERS};
    }

    @Override
    protected void execute(CommandEvent event) {
        String[] args = event.getArgs().split("\\s+");
        Member member = Utils.getMemberFromArgs(event);

        if (args[0].isEmpty()) {
            event.replyError("Required arguments are missing!");
            return;
        } else if (member == null) {
            event.replyError("Member not found.");
            return;
        }

        try {
            event.getGuild().kick(member).complete();
            event.reactSuccess();
        } catch (Exception e) {
            event.replyError(e.getMessage());
        }
    }
}
