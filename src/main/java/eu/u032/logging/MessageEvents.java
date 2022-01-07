package eu.u032.logging;

import eu.u032.Config;
import eu.u032.MessageCache;
import eu.u032.Utils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageBulkDeleteEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageDeleteEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;
import java.util.List;

public class MessageEvents extends ListenerAdapter {
    @Override
    public void onGuildMessageReceived(final GuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot() || event.getAuthor().isSystem()) return;
        MessageCache.addMessage(event.getMessage());
    }

    @Override
    public void onGuildMessageDelete(final GuildMessageDeleteEvent event) {
        if (MessageCache.getMessage(event.getMessageIdLong()) == null) return;

        final Message msg = MessageCache.getMessage(event.getMessageIdLong());
        final User author = Objects.requireNonNull(msg).getAuthor();

        final EmbedBuilder embed = new EmbedBuilder()
			.setAuthor(author.getAsTag(), author.getEffectiveAvatarUrl(), author.getEffectiveAvatarUrl())
			.setColor(Utils.getColorRed())
			.setDescription(String.format("Message from %s deleted in <#%s>",
				author.getAsMention(), msg.getChannel().getId()))
			.setFooter("ID: " + author.getId());

        if (!msg.getContentDisplay().isEmpty()) {
            embed.addField("Message content", "```" + msg.getContentDisplay()
				.replaceAll("```", "") + "```", false);
        }

        if (!msg.getAttachments().isEmpty()) {
            final StringBuilder attachments = new StringBuilder();

            for (final Message.Attachment attachment : msg.getAttachments()) {
                attachments.append(String.format("File: [%s](%s) ([Proxy](%s))",
					attachment.getFileName(), attachment.getUrl(), attachment.getProxyUrl())
                ).append("\n");
            }

            embed.addField("Attachments", attachments.toString(), false);
        }

        Utils.sendLog(event.getGuild(), embed);
    }

    @Override
    public void onGuildMessageUpdate(final GuildMessageUpdateEvent event) {
        if (MessageCache.getMessage(event.getMessageIdLong()) == null) return;

        final Message before = MessageCache.getMessage(event.getMessageIdLong());

        if (before == null) return;

        final Message after = event.getMessage();
        final User author = after.getAuthor();

        MessageCache.addMessage(after);

        final EmbedBuilder embed = new EmbedBuilder()
			.setAuthor(author.getAsTag(), author.getEffectiveAvatarUrl(), author.getEffectiveAvatarUrl())
			.setColor(Utils.getColorYellow())
			.setDescription(String.format("Message from %s edited in <#%s>\n[Jump to Message](%s)",
				author.getAsMention(), after.getChannel().getId(), after.getJumpUrl()))
			.addField("Before", before.getContentDisplay(), false)
			.addField("After", after.getContentDisplay(), false)
			.setFooter("ID: " + author.getId());
        Utils.sendLog(event.getGuild(), embed);
    }

    @Override
    public void onMessageBulkDelete(final MessageBulkDeleteEvent event) {
        final List<String> deletedMessages = new ArrayList<>();

        for (final String messageId : event.getMessageIds()) {
            final Message message = MessageCache.getMessage(Long.parseLong(messageId));
            if (message == null) continue;
            deletedMessages.add(String.format("%s %s: %s\n",
				message.getTimeCreated().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)),
				message.getAuthor().getAsTag(),
				message.getContentDisplay()
            ));
        }

        Collections.reverse(deletedMessages);
        final StringBuilder deletedMessagesString = new StringBuilder();
        for (final String message : deletedMessages) {
            deletedMessagesString.append(message);
        }

        final EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Deleted " + event.getMessageIds().size() + " messages!")
                .setDescription("Deleted in " + event.getChannel().getAsMention())
                .setColor(Utils.getColorRed())
                .setTimestamp(new Date().toInstant());
        Objects.requireNonNull(event.getGuild().getTextChannelById(Config.getString("LOGS_CHANNEL")))
                .sendMessageEmbeds(embed.build())
                .addFile(deletedMessagesString.toString().getBytes(), ".txt")
                .queue();
    }
}
