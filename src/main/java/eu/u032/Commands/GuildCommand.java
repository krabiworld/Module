package eu.u032.Commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;

import java.awt.*;


public class GuildCommand extends Command {
    public GuildCommand() {
        this.name = "guild";
        this.aliases = new String[]{"stats"};
        this.guildOnly = true;
        this.help = "Guild stats";
    }

    @Override
    protected void execute(CommandEvent event) {
        Guild guild = event.getGuild();
        String value = String.format("Members count: %s\nOwner: %s\nEmotes: %s\nCreated: <t:%s:D>\nChannels: %s (%s text, %s voice, %s categories)",
                guild.getMemberCount(),
                guild.getOwner().getAsMention(),
                guild.getEmotes().size(),
                guild.getTimeCreated().toEpochSecond(),
                guild.getChannels().size(),
                guild.getTextChannels().size(),
                guild.getVoiceChannels().size(),
                guild.getCategories().size()
        );

        EmbedBuilder aboutembed = new EmbedBuilder()
                .setAuthor(guild.getName(), guild.getIconUrl(), guild.getIconUrl())
                .setColor(Color.decode("#6196d5"))
                .addField("Guild stats", value, true);
        event.reply(aboutembed.build());
    }
}
