package eu.u032;

import eu.u032.Utils.Config;
import eu.u032.Utils.MessageCache;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Invite;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.invite.GuildInviteCreateEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
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
        if (event.getAuthor().isBot()) return;
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
                .setDescription(String.format("%s created an [invite](https://discord.gg/%s)", inviter.getAsMention(), invite.getCode()))
                .setTimestamp(new Date().toInstant());
        event.getJDA()
                .getTextChannelById(Config.getString("LOGS_CHANNEL"))
                .sendMessageEmbeds(embed.build())
                .queue();

    }

    // Message deleted
    @Override
    public void onGuildMessageDelete(GuildMessageDeleteEvent event) {
        if (MessageCache.getMessage(event.getMessageIdLong()) == null) return;

        Message msg = MessageCache.getMessage(event.getMessageIdLong());
        User author = msg.getAuthor();

        EmbedBuilder embed = new EmbedBuilder()
                .setAuthor(author.getAsTag(), author.getEffectiveAvatarUrl(), author.getEffectiveAvatarUrl())
                .setColor(Color.decode("#e94b3e"))
                .setDescription(String.format("Message from %s deleted in <#%s>", author.getAsMention(), msg.getChannel().getId()))
                .setTimestamp(new Date().toInstant());

        if(!msg.getAttachments().isEmpty()) {
            embed.addField("Message content", msg.getAttachments().get(0).getUrl(), false);
        } else {
            embed.addField("Message content", msg.getContentDisplay(), false);
        }

        event.getJDA()
                .getTextChannelById(Config.getString("LOGS_CHANNEL"))
                .sendMessageEmbeds(embed.build())
                .queue();
    }

    // Message edited
    @Override
    public void onGuildMessageUpdate(GuildMessageUpdateEvent event) {
        Message before = MessageCache.getMessage(event.getMessageIdLong());
        Message after = event.getMessage();
        User author = after.getAuthor();

        EmbedBuilder embed = new EmbedBuilder()
                .setAuthor(author.getAsTag(), author.getEffectiveAvatarUrl(), author.getEffectiveAvatarUrl())
                .setColor(Color.decode("#f7d724"))
                .setDescription(String.format("Message from %s edited in <#%s>\n[Jump to Message](https://discordapp.com/channels/%s/%s/%s)", author.getAsMention(), after.getChannel().getId(), after.getGuild().getId(), after.getChannel().getId(), after.getId()))
                .addField("Before", before.getContentDisplay(), true)
                .addField("After", after.getContentDisplay(), false)
                .setTimestamp(new Date().toInstant());
        event.getJDA()
                .getTextChannelById(Config.getString("LOGS_CHANNEL"))
                .sendMessageEmbeds(embed.build())
                .queue();

    }

    // Member join
    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        Member member = event.getMember();

        EmbedBuilder embed = new EmbedBuilder()
                .setAuthor(member.getUser().getAsTag(), member.getEffectiveAvatarUrl(), member.getEffectiveAvatarUrl())
                .setColor(Color.decode("#f7d724"))
                .setDescription(String.format("%s joined to server!", member.getAsMention()))
                .addField("Registered at", String.format("<t:%s>", member.getTimeCreated().toEpochSecond()), true)
                .addField("Member count", String.valueOf(member.getGuild().getMemberCount()), true)
                .setFooter("ID: " + member.getId())
                .setTimestamp(new Date().toInstant());
        event.getJDA()
                .getTextChannelById(Config.getString("LOGS_CHANNEL"))
                .sendMessageEmbeds(embed.build())
                .queue();
    }

    // Member leave
    @Override
    public void onGuildMemberRemove(GuildMemberRemoveEvent event) {
        User user = event.getUser();

        EmbedBuilder embed = new EmbedBuilder()
                .setAuthor(user.getAsTag(), user.getEffectiveAvatarUrl(), user.getEffectiveAvatarUrl())
                .setColor(Color.decode("#e94b3e"))
                .setDescription(String.format("%s has left the server!", user.getAsMention()))
                .addField("Registered at", String.format("<t:%s>", user.getTimeCreated().toEpochSecond()), true)
                .addField("Member count", String.valueOf(event.getGuild().getMemberCount()), true)
                .setFooter("ID: " + user.getId())
                .setTimestamp(new Date().toInstant());
        event.getJDA()
                .getTextChannelById(Config.getString("LOGS_CHANNEL"))
                .sendMessageEmbeds(embed.build())
                .queue();
    }


}
