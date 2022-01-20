/*
 * Module Discord Bot.
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

package org.module.events;

import org.module.Constants;
import org.module.MessageCache;
import org.module.service.MessageService;
import org.module.util.PropertyUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageBulkDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
public class MessageEvents extends ListenerAdapter {
	@Autowired
	private MessageService messageService;

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        MessageCache.addMessage(event.getMessage());
    }

    @Override
    public void onMessageDelete(MessageDeleteEvent event) {
		Message msg = MessageCache.getMessage(event.getMessageIdLong());

		if (msg == null) return;

        User author = msg.getAuthor();

		if (author.isBot()) return;

        EmbedBuilder embed = new EmbedBuilder()
			.setAuthor(author.getAsTag(), author.getEffectiveAvatarUrl(), author.getEffectiveAvatarUrl())
			.setColor(Constants.COLOR_RED)
			.setDescription(String.format("Message from %s deleted in <#%s>",
				author.getAsMention(), msg.getChannel().getId()))
			.setFooter("ID: " + author.getId());

        if (!msg.getContentDisplay().isEmpty()) {
            embed.addField("Message content", "```" + msg.getContentStripped() + "```", false);
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

		messageService.sendLog(event.getGuild(), embed);
    }

    @Override
    public void onMessageUpdate(MessageUpdateEvent event) {
		Message after = event.getMessage();
		MessageCache.addMessage(after);

		Message before = MessageCache.getMessage(event.getMessageIdLong());
		User author = after.getAuthor();

		if (author.isBot() || before == null) return;

		EmbedBuilder embed = new EmbedBuilder()
			.setAuthor(author.getAsTag(), author.getEffectiveAvatarUrl(), author.getEffectiveAvatarUrl())
			.setColor(Constants.COLOR_YELLOW)
			.setDescription(String.format("Message from %s edited in <#%s>\n[Jump to Message](%s)",
				author.getAsMention(), after.getChannel().getId(), after.getJumpUrl()))
			.setFooter("ID: " + author.getId());

		if (!before.getContentStripped().isEmpty()) {
			String beforeFormatted = "```" + before.getContentStripped() + "```";
			embed.addField("Before", beforeFormatted, false);
		}
		if (!after.getContentStripped().isEmpty()) {
			String afterFormatted = "```" + after.getContentStripped() + "```";
			embed.addField("After", afterFormatted, false);
		}

		messageService.sendLog(event.getGuild(), embed);
	}

    @Override
    public void onMessageBulkDelete(MessageBulkDeleteEvent event) {
        List<String> deletedMessages = new ArrayList<>();

        for (String messageId : event.getMessageIds()) {
            Message message = MessageCache.getMessage(Long.parseLong(messageId));
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
        for (String message : deletedMessages) {
            deletedMessagesString.append(message);
        }

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Deleted " + event.getMessageIds().size() + " messages!")
                .setDescription("Deleted in " + event.getChannel().getAsMention())
                .setColor(Constants.COLOR_RED);
		messageService.sendLog(event.getGuild(), embed, deletedMessagesString.toString().getBytes());
    }
}
