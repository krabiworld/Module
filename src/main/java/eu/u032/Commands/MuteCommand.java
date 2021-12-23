package eu.u032.Commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import eu.u032.Utils.Args;
import eu.u032.Utils.Config;
import eu.u032.Utils.Property;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

public class MuteCommand extends Command {

    public MuteCommand() {
        this.name = "mute";
        this.userPermissions = new Permission[]{Permission.MANAGE_ROLES};
    }

    @Override
    protected void execute(CommandEvent event) {
        String[] args = event.getArgs().split("\\s+");
        Member member = Args.getMemberFromArgs(event);
        Role muteRole = event.getGuild().getRoleById(Config.getString("MUTE_ROLE"));

        if (muteRole == null) {
            event.replyError(Property.getError("mute_role_not_set"));
            return;
        } else if (args[0].isEmpty()) {
            event.replyError(Property.getError("required_args"));
            return;
        } else if (member == null) {
            event.replyError(Property.getError("member_not_found"));
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
