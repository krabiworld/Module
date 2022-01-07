package eu.u032.commands.utilities;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import eu.u032.Utils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emote;

public class EmoteCommand extends Command {
    public EmoteCommand() {
        this.name = "emote";
        this.help = "Information about emoji";
        this.arguments = "<@Emoji | ID>";
        this.category = new Category("Utilities");
    }

    @Override
    protected void execute(final CommandEvent event) {
        final String emoteId = Utils.getId(event.getArgs(), Utils.EMOJI);
        final Emote emote = emoteId.isEmpty() ? null : event.getGuild().getEmoteById(emoteId);

        if (event.getArgs().isEmpty()) {
            event.replyError("Required arguments are missing!");
            return;
        }
        if (emote == null) {
            event.replyError("Emote is not found.");
            return;
        }

        final EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Emote " + emote.getName(), emote.getImageUrl())
                .setColor(Utils.getColor())
                .setImage(emote.getImageUrl())
                .setFooter("ID: " + emote.getId());
        event.reply(embed.build());
    }
}
