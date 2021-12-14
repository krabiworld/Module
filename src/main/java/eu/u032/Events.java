package eu.u032;

import eu.u032.Utils.Config;
import eu.u032.Utils.MessageCache;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Invite;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.invite.GuildInviteCreateEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageDeleteEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.util.Date;

public class Events extends ListenerAdapter {

    // Cache messages

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        MessageCache.addMessage(event.getMessage());
    }

    // Invite created
    @Override
    public void onGuildInviteCreate(GuildInviteCreateEvent event) {
        Invite invite = event.getInvite();
        User inviter = event.getInvite().getInviter();

        EmbedBuilder embed = new EmbedBuilder()
                .setAuthor(inviter.getAsTag(), inviter.getEffectiveAvatarUrl(), inviter.getEffectiveAvatarUrl())
                .setColor(Color.decode("#89d561"))
                .setDescription(String.format("<@%s> created an [invite](https://discord.gg/%s)", inviter.getId(), invite.getCode()))
                .setTimestamp(new Date().toInstant());
        event.getJDA()
                .getTextChannelById(Config.getString("LOGS_CHANNEL"))
                .sendMessage(embed.build())
                .queue();

    }

    @Override
    public void onGuildMessageDelete(GuildMessageDeleteEvent event) {
        Message msg = MessageCache.getMessage(event.getMessageIdLong());
        User author = msg.getAuthor();

        EmbedBuilder embed = new EmbedBuilder()
                .setAuthor(author.getAsTag(), author.getAvatarUrl(), author.getAvatarUrl())
                .setColor(Color.decode("#e94b3e"))
                .setDescription(String.format("Message from <@%s> deleted in <#%s>", author.getId(), msg.getChannel().getId()))
                .addField("Message content", msg.getContentDisplay(), true)
                .setTimestamp(new Date().toInstant());
        event.getJDA()
                .getTextChannelById(Config.getString("LOGS_CHANNEL"))
                .sendMessage(embed.build())
                .queue();
    }

    @Override
    public void onGuildMessageUpdate(GuildMessageUpdateEvent event) {
        Message old = MessageCache.getMessage(event.getMessageIdLong());
        Message after = event.getMessage();
        User author = after.getAuthor();

        EmbedBuilder embed = new EmbedBuilder()
                .setAuthor(author.getAsTag(), author.getAvatarUrl(), author.getAvatarUrl())
                .setColor(Color.decode("#f7d724"))
                .setDescription(String.format("Message from <@%s> edited in <#%s>", author.getId(), after.getChannel().getId()))
                .addField("Before", old.getContentDisplay(), true)
                .addField("After", after.getContentDisplay(), false)
                .setTimestamp(new Date().toInstant());
        event.getJDA()
                .getTextChannelById(Config.getString("LOGS_CHANNEL"))
                .sendMessage(embed.build())
                .queue();
    }

}
