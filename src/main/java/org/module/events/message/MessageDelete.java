/*
 * This file is part of Module.
 *
 * Module is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Module is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Module. If not, see <https://www.gnu.org/licenses/>.
 */

package org.module.events.message;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.module.cache.MessageCache;
import org.module.Constants;
import org.module.util.MessageUtil;

public class MessageDelete extends ListenerAdapter {
	@Override
	public void onMessageDelete(MessageDeleteEvent event) {
		Message msg = MessageCache.getMessage(event.getMessageIdLong());
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

		MessageUtil.sendLog(event.getGuild(), embed);
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
