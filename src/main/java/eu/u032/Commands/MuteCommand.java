package eu.u032.Commands;

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
        this.userPermissions = new Permission[]{Permission.VOICE_MUTE_OTHERS};
        this.botPermissions = new Permission[]{Permission.VOICE_MUTE_OTHERS};
    }

    @Override
    protected void execute(CommandEvent event) {
        String[] args = event.getArgs().split("\\s+");
        Member member = Utils.getMemberFromArgs(event);
        Role muteRole = event.getGuild().getRoleById(Config.getString("MUTE_ROLE"));

        if (muteRole == null) {
            event.replyError("Mute role is not set.");
            return;
        } else if (args[0].isEmpty()) {
            event.replyError("Required arguments are missing!");
            return;
        } else if (member == null) {
            event.replyError("Member not found.");
            return;
        }

        try {
            event.getGuild().addRoleToMember(member, muteRole).complete();
            event.reactSuccess();
        } catch (Exception e) {
            event.replyError(e.getMessage());
        }
    }

}
