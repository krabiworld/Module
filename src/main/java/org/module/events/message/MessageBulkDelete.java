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

import org.module.cache.MessageCache;
import org.module.Constants;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageBulkDeleteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.module.util.MessageUtil;

import java.time.format.DateTimeFormatter;
import java.util.*;

public class MessageBulkDelete extends ListenerAdapter {
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
                .setColor(Constants.ERROR);
		MessageUtil.sendLog(event.getGuild(), embed, deletedMessagesString.toString().getBytes());
    }
}
