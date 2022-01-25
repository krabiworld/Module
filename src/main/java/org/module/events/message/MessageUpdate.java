/*
 * This file is part of Module.

 * Module is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * Module is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with Module. If not, see <https://www.gnu.org/licenses/>.
 */

package org.module.events.message;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.module.cache.MessageCache;
import org.module.constants.Constants;
import org.module.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageUpdate extends ListenerAdapter {
	private final MessageService messageService;

	@Autowired
	public MessageUpdate(MessageService messageService) {
		this.messageService = messageService;
	}

	@Override
	public void onMessageUpdate(MessageUpdateEvent event) {
		Message before = MessageCache.getMessage(event.getMessageIdLong());
		Message after = event.getMessage();
		User author = after.getAuthor();

		MessageCache.addMessage(after);

		if (author.isBot() || before == null) return;
		if (before.getContentStripped().equals(after.getContentStripped())) return;

		EmbedBuilder embed = new EmbedBuilder()
			.setColor(Constants.COLOR_YELLOW)
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

		messageService.sendLog(event.getGuild(), embed);
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
