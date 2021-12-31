package eu.u032.Commands.Moderation;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import eu.u032.Utils;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;

public class BanCommand extends Command {

    public BanCommand() {
        this.name = "ban";
        this.help = "Ban member from server";
        this.arguments = "<@Member | ID> [reason]";
        this.category = new Category("Moderation");
        this.userPermissions = new Permission[]{Permission.BAN_MEMBERS};
        this.botPermissions = new Permission[]{Permission.BAN_MEMBERS};
    }

    @Override
    protected void execute(CommandEvent event) {
        String[] args = Utils.splitArgs(event.getArgs());
        Member member = Utils.getMemberFromArgs(event);
        String reason = Utils.getReasonFromArgs(args, 1);

        if (args[0].isEmpty()) {
            event.replyError("Required arguments are missing!");
            return;
        }
        if (member == null) {
            event.replyError("Member not found.");
            return;
        }

        try {
            event.getGuild().ban(member, 0, reason).queue();
            event.reactSuccess();
        } catch (Exception e) {
            event.replyError(e.getMessage());
        }
    }

}
