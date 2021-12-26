package eu.u032.BotEvents;

import eu.u032.Config;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.channel.text.TextChannelCreateEvent;
import net.dv8tion.jda.api.events.channel.text.TextChannelDeleteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.util.Date;
import java.util.Objects;

public class ChannelEvents extends ListenerAdapter {

    @Override
    public void onTextChannelDelete(TextChannelDeleteEvent event) {
        TextChannel channel = event.getChannel();

        EmbedBuilder embed = new EmbedBuilder()
                .setAuthor("Text Channel Deleted", event.getGuild().getIconUrl(), event.getGuild().getIconUrl())
                .setColor(Color.decode("#e94b3e"))
                .addField("Channel", channel.getName(), false)
                .setFooter("Channel ID: " + channel.getId())
                .setTimestamp(new Date().toInstant());
        Objects.requireNonNull(event.getJDA().getTextChannelById(Config.getString("LOGS_CHANNEL")))
                .sendMessageEmbeds(embed.build())
                .queue();
    }

    @Override
    public void onTextChannelCreate(TextChannelCreateEvent event) {
        TextChannel channel = event.getChannel();

        EmbedBuilder embed = new EmbedBuilder()
                .setAuthor("Text Channel Created", event.getGuild().getIconUrl(), event.getGuild().getIconUrl())
                .setColor(Color.decode("#89d561"))
                .addField("Channel", channel.getName(), false)
                .setFooter("Channel ID: " + channel.getId())
                .setTimestamp(new Date().toInstant());
        Objects.requireNonNull(event.getJDA().getTextChannelById(Config.getString("LOGS_CHANNEL")))
                .sendMessageEmbeds(embed.build())
                .queue();
    }

}
