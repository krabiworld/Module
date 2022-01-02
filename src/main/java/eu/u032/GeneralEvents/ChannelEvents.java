package eu.u032.GeneralEvents;

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
    public void onTextChannelDelete(TextChannelDeleteEvent event) {
        TextChannel channel = event.getChannel();

        EmbedBuilder embed = new EmbedBuilder()
                .setAuthor("Text Channel Deleted", event.getGuild().getIconUrl(), event.getGuild().getIconUrl())
                .setColor(Utils.getColorDelete())
                .addField("Text Channel", channel.getName(), false)
                .setFooter("ID: " + channel.getId());
        Utils.sendLog(event.getGuild(), embed);
    }

    @Override
    public void onTextChannelCreate(TextChannelCreateEvent event) {
        TextChannel channel = event.getChannel();

        EmbedBuilder embed = new EmbedBuilder()
                .setAuthor("Text Channel Created", event.getGuild().getIconUrl(), event.getGuild().getIconUrl())
                .setColor(Utils.getColorCreate())
                .addField("Text Channel", channel.getName(), false)
                .setFooter("ID: " + channel.getId());
        Utils.sendLog(event.getGuild(), embed);
    }

    @Override
    public void onVoiceChannelDelete(VoiceChannelDeleteEvent event) {
        VoiceChannel channel = event.getChannel();

        EmbedBuilder embed = new EmbedBuilder()
                .setAuthor("Voice Channel Deleted", event.getGuild().getIconUrl(), event.getGuild().getIconUrl())
                .setColor(Utils.getColorDelete())
                .addField("Voice Channel", channel.getName(), false)
                .setFooter("ID: " + channel.getId());
        Utils.sendLog(event.getGuild(), embed);
    }

    @Override
    public void onVoiceChannelCreate(VoiceChannelCreateEvent event) {
        VoiceChannel channel = event.getChannel();

        EmbedBuilder embed = new EmbedBuilder()
                .setAuthor("Voice Channel Created", event.getGuild().getIconUrl(), event.getGuild().getIconUrl())
                .setColor(Utils.getColorCreate())
                .addField("Voice Channel", channel.getName(), false)
                .setFooter("ID: " + channel.getId());
        Utils.sendLog(event.getGuild(), embed);
    }
}
