package eu.u032.Commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import eu.u032.Utils.Args;
import eu.u032.Utils.Property;
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
        Member member = Args.getMemberFromArgs(event);

        if (args[0].isEmpty()) {
            event.replyError(Property.getError("required_args"));
            return;
        } else if (member == null) {
            event.replyError(Property.getError("member_not_found"));
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
