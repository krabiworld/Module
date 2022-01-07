package eu.u032.commands.moderation;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import eu.u032.Utils;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;

public class KickCommand extends Command {
    public KickCommand() {
        this.name = "kick";
        this.help = "Kick member from server";
        this.arguments = "<@Member | ID> [reason]";
        this.category = new Category("Moderation");
        this.userPermissions = new Permission[]{Permission.KICK_MEMBERS};
        this.botPermissions = new Permission[]{Permission.KICK_MEMBERS};
    }

    @Override
    protected void execute(final CommandEvent event) {
        final String[] args = Utils.splitArgs(event.getArgs());

		final String memberId = Utils.getId(args[0], Utils.MEMBER);
        final Member member = memberId.isEmpty() ? null : event.getGuild().getMemberById(memberId);

		final String reason = Utils.getGluedArg(args, 1);

        if (args[0].isEmpty()) {
            event.replyError("Required arguments are missing!");
            return;
        }
        if (member == null) {
            event.replyError("Member not found.");
            return;
        }

        event.getGuild().kick(member, reason).queue();
        event.reactSuccess();
    }
}
