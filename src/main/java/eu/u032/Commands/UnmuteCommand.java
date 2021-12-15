package eu.u032.Commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import eu.u032.Utils.Config;
import net.dv8tion.jda.api.Permission;

public class UnmuteCommand extends Command {

    public UnmuteCommand() {
        this.name = "unmute";
        this.help = "Unmute member";
        this.userPermissions = new Permission[]{Permission.MANAGE_ROLES};
        this.category = new Category("Moderation");
    }

    @Override
    protected void execute(CommandEvent event) {
        if (event.getArgs().isEmpty()) {
            event.reply("Required arguments are missing!");
            return;
        }

        try {
            if (event.getJDA().getUserById(event.getArgs()) == null) {
                throw new Exception();
            }
        } catch (Exception e) {
            event.reply(e.getMessage());
            event.getMessage().addReaction("U+274C").queue();
            return;
        }

        event.getGuild().removeRoleFromMember(
                event.getArgs(), event.getJDA().getRoleById(Config.getString("MUTE_ROLE"))
        ).queue();

        event.getMessage().addReaction("U+2705").queue();
    }

}
