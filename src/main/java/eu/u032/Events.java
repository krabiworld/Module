package eu.u032;

import eu.u032.Utils.Config;
import eu.u032.Utils.MessageCache;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.channel.text.TextChannelCreateEvent;
import net.dv8tion.jda.api.events.channel.text.TextChannelDeleteEvent;
import net.dv8tion.jda.api.events.guild.invite.GuildInviteCreateEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateNicknameEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageDeleteEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.util.ArrayList;
import java.util.Date;

public class Events extends ListenerAdapter {

    // Cache messages
    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot() || event.getAuthor().isSystem()) return;
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
                .setFooter("User ID: " + inviter.getId())
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
                .setFooter("User ID: " + author.getId())
                .setTimestamp(new Date().toInstant());

        if (!msg.getAttachments().isEmpty()) {
            StringBuilder message = new StringBuilder();
            for (Message.Attachment attachment : msg.getAttachments()) {
                message.append(
                        String.format("File: [%s](%s)", attachment.getFileName(), attachment.getUrl())
                ).append("\n");
            }
            message.append(msg.getContentDisplay());
            embed.addField("Message content", message.toString(), false);
        } else {
            embed.addField("Message content", msg.getContentDisplay(), false);
        }

        event.getJDA()
                .getTextChannelById(Config.getString("LOGS_CHANNEL"))
                .sendMessageEmbeds(embed.build())
                .queue();
    }

    // Message edited and update message cache
    @Override
    public void onGuildMessageUpdate(GuildMessageUpdateEvent event) {
        if (MessageCache.getMessage(event.getMessageIdLong()) == null) return;

        Message before = MessageCache.getMessage(event.getMessageIdLong());
        Message after = event.getMessage();
        User author = after.getAuthor();

        MessageCache.addMessage(after);

        EmbedBuilder embed = new EmbedBuilder()
                .setAuthor(author.getAsTag(), author.getEffectiveAvatarUrl(), author.getEffectiveAvatarUrl())
                .setColor(Color.decode("#f7d724"))
                .setDescription(String.format("Message from %s edited in <#%s>\n[Jump to Message](%s)", author.getAsMention(), after.getChannel().getId(), after.getJumpUrl()))
                .addField("Before", before.getContentDisplay(), false)
                .addField("After", after.getContentDisplay(), false)
                .setFooter("User ID: " + author.getId())
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
                .setFooter("User ID: " + member.getId())
                .setTimestamp(new Date().toInstant());
        event.getJDA()
                .getTextChannelById(Config.getString("LOGS_CHANNEL"))
                .sendMessageEmbeds(embed.build())
                .queue();
    }

    // Member leave
    @Override
    public void onGuildMemberRemove(GuildMemberRemoveEvent event) {
        Member member = event.getGuild().retrieveMemberById(event.getUser().getId()).complete();

        EmbedBuilder embed = new EmbedBuilder()
                .setAuthor(member.getUser().getAsTag(), member.getEffectiveAvatarUrl(), member.getEffectiveAvatarUrl())
                .setColor(Color.decode("#e94b3e"))
                .setDescription(String.format("%s has left the server!", member.getAsMention()))
                .addField("Joined at", String.format("<t:%s>", member.getTimeJoined().toEpochSecond()), true)
                .addField("Registered at", String.format("<t:%s>", member.getTimeCreated().toEpochSecond()), true)
                .addField("Member count", String.valueOf(event.getGuild().getMemberCount()), true)
                .setFooter("User ID: " + member.getId())
                .setTimestamp(new Date().toInstant());
        event.getJDA()
                .getTextChannelById(Config.getString("LOGS_CHANNEL"))
                .sendMessageEmbeds(embed.build())
                .queue();
    }

    // Channel delete
    @Override
    public void onTextChannelDelete(TextChannelDeleteEvent event) {
        TextChannel channel = event.getChannel();

        EmbedBuilder embed = new EmbedBuilder()
                .setAuthor("Text Channel Deleted", event.getGuild().getIconUrl(), event.getGuild().getIconUrl())
                .setColor(Color.decode("#e94b3e"))
                .addField("Channel", channel.getName(), false)
                .setFooter("Channel ID: " + channel.getId())
                .setTimestamp(new Date().toInstant());
        event.getJDA()
                .getTextChannelById(Config.getString("LOGS_CHANNEL"))
                .sendMessageEmbeds(embed.build())
                .queue();
    }

    // Channel create
    @Override
    public void onTextChannelCreate(TextChannelCreateEvent event) {
        TextChannel channel = event.getChannel();

        EmbedBuilder embed = new EmbedBuilder()
                .setAuthor("Text Channel Created", event.getGuild().getIconUrl(), event.getGuild().getIconUrl())
                .setColor(Color.decode("#89d561"))
                .addField("Channel", channel.getName(), false)
                .setFooter("Channel ID: " + channel.getId())
                .setTimestamp(new Date().toInstant());
        event.getJDA()
                .getTextChannelById(Config.getString("LOGS_CHANNEL"))
                .sendMessageEmbeds(embed.build())
                .queue();
    }

    // Member nickname update
    @Override
    public void onGuildMemberUpdateNickname(GuildMemberUpdateNicknameEvent event) {
        Member member = event.getMember();
        String before = event.getOldNickname();
        String after = event.getNewNickname();
        String action = " was updated";

        if (before == null) return;
        if (after == null) action = " was reset";

        EmbedBuilder embed = new EmbedBuilder()
                .setAuthor("Nickname for " + member.getUser().getAsTag() + action, null, member.getEffectiveAvatarUrl())
                .setColor(member.getColor())
                .addField("Before", before, true)
                .setFooter("User ID: " + member.getId())
                .setTimestamp(new Date().toInstant());

        if (after != null) embed.addField("After", after, true);

        event.getJDA()
                .getTextChannelById(Config.getString("LOGS_CHANNEL"))
                .sendMessageEmbeds(embed.build())
                .queue();
    }

}
