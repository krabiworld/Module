package eu.u032.Commands.Moderation;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import eu.u032.Config;
import eu.u032.Utils;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

public class MuteCommand extends Command {

    public MuteCommand() {
        this.name = "mute";
        this.help = "Mute member on whole server";
        this.arguments = "<@Member | ID>";
        this.category = new Category("Moderation");
        this.userPermissions = new Permission[]{Permission.MANAGE_ROLES};
        this.botPermissions = new Permission[]{Permission.MANAGE_ROLES};
    }

    @Override
    protected void execute(CommandEvent event) {
        String[] args = Utils.splitArgs(event.getArgs());
        Member member = Utils.getMemberFromArgs(event);
        Role muteRole = event.getGuild().getRoleById(Config.getString("MUTE_ROLE"));

        if (muteRole == null) {
            event.replyError("Mute role is not set.");
            return;
        }
        if (args[0].isEmpty()) {
            event.replyError("Required arguments are missing!");
            return;
        }
        if (member == null) {
            event.replyError("Member not found.");
            return;
        }

        try {
            event.getGuild().addRoleToMember(member, muteRole).queue();
            event.reactSuccess();
        } catch (Exception e) {
            event.replyError(e.getMessage());
        }
    }
}
