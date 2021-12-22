package eu.u032.Commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;

public class AvatarCommand extends Command {

    public AvatarCommand() {
        this.name = "avatar";
    }

    @Override
    protected void execute(CommandEvent event) {
        try {
            User user =
                    event.getArgs().isEmpty() ? event.getAuthor() : event.getGuild().getJDA().retrieveUserById(event.getArgs()).complete();

            EmbedBuilder embed = new EmbedBuilder()
                    .setAuthor("Avatar of " + user.getName())
                    .setColor(Color.decode("#6196d5"))
                    .setImage(user.getEffectiveAvatarUrl() + "?size=512");
            event.reply(embed.build());
        } catch (Exception e) {
            event.replyError(e.getMessage());
        }
    }
}
