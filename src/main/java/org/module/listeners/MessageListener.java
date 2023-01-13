package org.module.listeners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageBulkDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.module.Constants;
import org.module.manager.CacheManager;
import org.module.util.LogsUtil;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;

public class MessageListener extends ListenerAdapter {
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		CacheManager.addMessage(event.getMessage());
	}

	@Override
	public void onMessageBulkDelete(MessageBulkDeleteEvent event) {
		ArrayList<Object> deletedMessages = new ArrayList<>();

		for (String messageId : event.getMessageIds()) {
			Message message = CacheManager.getMessage(Long.parseLong(messageId));
			if (message == null) continue;
			deletedMessages.add(String.format("%s %s%s%s: %s\n",
				message.getTimeCreated().format(DateTimeFormatter.ofPattern("dd-MM-yyyy, HH:mm:ss,")),
				message.getAuthor().getAsTag(),
				message.getAuthor().isBot() ? ", (Bot)" : "",
				message.getEmbeds().isEmpty() ? "" : ", (With Embed)",
				message.getContentStripped().isEmpty() ? "Message is empty" : message.getContentStripped()
			));
		}

		Collections.reverse(deletedMessages);
		StringBuilder deletedMessagesString = new StringBuilder();
		for (Object message : deletedMessages) {
			deletedMessagesString.append(message);
		}

		EmbedBuilder embed = new EmbedBuilder()
			.setTitle("Deleted " + event.getMessageIds().size() + " messages!")
			.setDescription("Deleted in " + event.getChannel().getAsMention())
			.setColor(Constants.ERROR);
		LogsUtil.sendLog(event.getGuild(), embed, deletedMessagesString.toString().getBytes());
	}

	@Override
	public void onMessageDelete(MessageDeleteEvent event) {
		Message msg = CacheManager.getMessage(event.getMessageIdLong());
		if (msg == null) return;

		User author = msg.getAuthor();
		if (author.isBot()) return;

		EmbedBuilder embed = new EmbedBuilder()
			.setColor(Constants.ERROR)
			.setDescription("Message has been deleted")
			.addField(getAuthorField(author))
			.addField(getChannelField(msg.getChannel()))
			.setFooter("Member ID: " + author.getId());

		if (!msg.getContentDisplay().isEmpty()) {
			embed.addField("Content", "```" + msg.getContentStripped() + "```", false);
		}

		if (!msg.getAttachments().isEmpty()) {
			StringBuilder attachments = new StringBuilder();

			for (Message.Attachment attachment : msg.getAttachments()) {
				attachments.append(String.format("File: [%s](%s) ([Proxy](%s))",
						attachment.getFileName(), attachment.getUrl(), attachment.getProxyUrl()))
					.append("\n");
			}

			embed.addField("Attachments", attachments.toString(), false);
		}

		LogsUtil.sendLog(event.getGuild(), embed);
	}

	@Override
	public void onMessageUpdate(MessageUpdateEvent event) {
		Message before = CacheManager.getMessage(event.getMessageIdLong());
		Message after = event.getMessage();
		User author = after.getAuthor();

		CacheManager.addMessage(after);

		if (author.isBot() || before == null) return;
		if (before.getContentStripped().equals(after.getContentStripped())) return;

		EmbedBuilder embed = new EmbedBuilder()
			.setColor(Constants.WARN)
			.setDescription(String.format("[Message has been edited](%s)", after.getJumpUrl()))
			.addField(getAuthorField(author))
			.addField(getChannelField(after.getChannel()))
			.setFooter("Member ID: " + author.getId());

		if (!before.getContentStripped().isEmpty()) {
			embed.addField("Before", "```" + before.getContentStripped() + "```", false);
		}
		if (!after.getContentStripped().isEmpty()) {
			embed.addField("After", "```" + after.getContentStripped() + "```", false);
		}

		LogsUtil.sendLog(event.getGuild(), embed);
	}

	private MessageEmbed.Field getAuthorField(User user) {
		String author = String.format("%s (%s)", user.getName(), user.getAsMention());
		return new MessageEmbed.Field("Author", author, true);
	}

	private MessageEmbed.Field getChannelField(MessageChannel messageChannel) {
		String channel = String.format("%s (<#%s>)", messageChannel.getName(), messageChannel.getId());
		return new MessageEmbed.Field("Channel", channel, true);
	}
}
