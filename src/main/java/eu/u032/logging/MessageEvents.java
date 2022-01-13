/*
 * UASM Discord Bot.
 * Copyright (C) 2022 untled032, Headcrab

 * UASM is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * UASM is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with UASM. If not, see https://www.gnu.org/licenses/.
 */

package eu.u032.logging;

import eu.u032.Constants;
import eu.u032.MessageCache;
import eu.u032.utils.MsgUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageBulkDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

public class MessageEvents extends ListenerAdapter {
    @Override
    public void onMessageReceived(final MessageReceivedEvent event) {
        MessageCache.addMessage(event.getMessage());
    }

    @Override
    public void onMessageDelete(final MessageDeleteEvent event) {
		final Message msg = MessageCache.getMessage(event.getMessageIdLong());

		if (msg == null) return;

        final User author = msg.getAuthor();

		if (author.isBot()) return;

        final EmbedBuilder embed = new EmbedBuilder()
			.setAuthor(author.getAsTag(), author.getEffectiveAvatarUrl(), author.getEffectiveAvatarUrl())
			.setColor(Constants.COLOR_RED)
			.setDescription(String.format("Message from %s deleted in <#%s>",
				author.getAsMention(), msg.getChannel().getId()))
			.setFooter("ID: " + author.getId());

        if (!msg.getContentDisplay().isEmpty()) {
            embed.addField("Message content", "```" + msg.getContentStripped() + "```", false);
        }

        if (!msg.getAttachments().isEmpty()) {
            final StringBuilder attachments = new StringBuilder();

            for (final Message.Attachment attachment : msg.getAttachments()) {
                attachments.append(String.format("File: [%s](%s) ([Proxy](%s))",
					attachment.getFileName(), attachment.getUrl(), attachment.getProxyUrl()))
					.append("\n");
            }

            embed.addField("Attachments", attachments.toString(), false);
        }

        MsgUtil.sendLog(event.getGuild(), embed);
    }

    @Override
    public void onMessageUpdate(final MessageUpdateEvent event) {
		final Message after = event.getMessage();
		MessageCache.addMessage(after);

		final Message before = MessageCache.getMessage(event.getMessageIdLong());
		final User author = after.getAuthor();

		if (author.isBot() || before == null) return;

		final EmbedBuilder embed = new EmbedBuilder()
			.setAuthor(author.getAsTag(), author.getEffectiveAvatarUrl(), author.getEffectiveAvatarUrl())
			.setColor(Constants.COLOR_YELLOW)
			.setDescription(String.format("Message from %s edited in <#%s>\n[Jump to Message](%s)",
				author.getAsMention(), after.getChannel().getId(), after.getJumpUrl()))
			.setFooter("ID: " + author.getId());

		if (!before.getContentStripped().isEmpty()) {
			final String beforeFormatted = "```" + before.getContentStripped() + "```";
			embed.addField("Before", beforeFormatted, false);
		}
		if (!after.getContentStripped().isEmpty()) {
			final String afterFormatted = "```" + after.getContentStripped() + "```";
			embed.addField("After", afterFormatted, false);
		}

		MsgUtil.sendLog(event.getGuild(), embed);
	}

    @Override
    public void onMessageBulkDelete(final MessageBulkDeleteEvent event) {
        final List<String> deletedMessages = new ArrayList<>();

        for (final String messageId : event.getMessageIds()) {
            final Message message = MessageCache.getMessage(Long.parseLong(messageId));
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
        final StringBuilder deletedMessagesString = new StringBuilder();
        for (final String message : deletedMessages) {
            deletedMessagesString.append(message);
        }

        final EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Deleted " + event.getMessageIds().size() + " messages!")
                .setDescription("Deleted in " + event.getChannel().getAsMention())
                .setColor(Constants.COLOR_RED);
		MsgUtil.sendLog(event.getGuild(), embed, deletedMessagesString.toString().getBytes());
    }
}
