package eu.u032.Commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

import java.awt.*;


public class ServerinfoCommand extends Command {

    public ServerinfoCommand() {
        this.name = "serverinfo";
    }

    @Override
    protected void execute(CommandEvent event) {
        Guild guild = event.getGuild();

        int online = 0, dnd = 0, idle = 0, offline = 0;
        for (Member member : guild.getMembers()) {
            switch (member.getOnlineStatus()) {
                case ONLINE -> online++;
                case IDLE -> idle++;
                case DO_NOT_DISTURB -> dnd++;
                case OFFLINE -> offline++;
            }
        }

        String value = String.format("Members count: %s (%s online, %s idle, %s dnd, %s offline)\nOwner: %s\nEmojis count: %s\nCreated at: <t:%s>\nChannels: %s (%s text, %s voice, %s categories)",
                guild.getMemberCount(),
                online, idle, dnd, offline,
                guild.getOwner().getAsMention(),
                guild.getEmotes().size(),
                guild.getTimeCreated().toEpochSecond(),
                guild.getChannels().size(),
                guild.getTextChannels().size(),
                guild.getVoiceChannels().size(),
                guild.getCategories().size()
        );

        EmbedBuilder embed = new EmbedBuilder()
                .setAuthor(guild.getName())
                .setColor(Color.decode("#6196d5"))
                .setThumbnail(guild.getIconUrl())
                .setDescription(value);
        event.reply(embed.build());
    }

}
