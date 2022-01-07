package eu.u032.logging;

import eu.u032.Utils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.channel.text.TextChannelCreateEvent;
import net.dv8tion.jda.api.events.channel.text.TextChannelDeleteEvent;
import net.dv8tion.jda.api.events.channel.voice.VoiceChannelCreateEvent;
import net.dv8tion.jda.api.events.channel.voice.VoiceChannelDeleteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ChannelEvents extends ListenerAdapter {
    @Override
    public void onTextChannelDelete(final TextChannelDeleteEvent event) {
        final TextChannel channel = event.getChannel();

        final EmbedBuilder embed = new EmbedBuilder()
			.setAuthor("Text Channel Deleted", event.getGuild().getIconUrl(), event.getGuild().getIconUrl())
			.setColor(Utils.getColorRed())
			.addField("Text Channel", channel.getName(), false)
			.setFooter("ID: " + channel.getId());
        Utils.sendLog(event.getGuild(), embed);
    }

    @Override
    public void onTextChannelCreate(final TextChannelCreateEvent event) {
        final TextChannel channel = event.getChannel();

        final EmbedBuilder embed = new EmbedBuilder()
			.setAuthor("Text Channel Created", event.getGuild().getIconUrl(), event.getGuild().getIconUrl())
			.setColor(Utils.getColorGreen())
			.addField("Text Channel", channel.getName(), false)
			.setFooter("ID: " + channel.getId());
        Utils.sendLog(event.getGuild(), embed);
    }

    @Override
    public void onVoiceChannelDelete(final VoiceChannelDeleteEvent event) {
        final VoiceChannel channel = event.getChannel();

        final EmbedBuilder embed = new EmbedBuilder()
			.setAuthor("Voice Channel Deleted", event.getGuild().getIconUrl(), event.getGuild().getIconUrl())
			.setColor(Utils.getColorRed())
			.addField("Voice Channel", channel.getName(), false)
			.setFooter("ID: " + channel.getId());
        Utils.sendLog(event.getGuild(), embed);
    }

    @Override
    public void onVoiceChannelCreate(final VoiceChannelCreateEvent event) {
        final VoiceChannel channel = event.getChannel();

        final EmbedBuilder embed = new EmbedBuilder()
			.setAuthor("Voice Channel Created", event.getGuild().getIconUrl(), event.getGuild().getIconUrl())
			.setColor(Utils.getColorGreen())
			.addField("Voice Channel", channel.getName(), false)
			.setFooter("ID: " + channel.getId());
        Utils.sendLog(event.getGuild(), embed);
    }
}
