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

package eu.u032.commands.moderation;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import eu.u032.Constants;
import eu.u032.util.GeneralUtil;
import eu.u032.util.MessageUtil;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.entities.TextChannel;

import java.time.OffsetDateTime;
import java.util.LinkedList;
import java.util.List;

public class ClearCommand extends Command {
    public ClearCommand() {
        this.name = MessageUtil.getMessage("command.clear.name");
        this.help = MessageUtil.getMessage("command.clear.help");
        this.arguments = MessageUtil.getMessage("command.clear.arguments");
        this.category = Constants.MODERATION;
        this.userPermissions = new Permission[]{Permission.MESSAGE_MANAGE};
        this.botPermissions = new Permission[]{Permission.MESSAGE_MANAGE, Permission.MESSAGE_ATTACH_FILES};
    }

    @Override
    protected void execute(final CommandEvent event) {
		if (GeneralUtil.isNotMod(event)) {
			MessageUtil.sendError(event, "error.not.mod");
			return;
		}
		if (event.getArgs().isEmpty()) {
			MessageUtil.sendError(event, "error.missing.args");
			return;
		}
        int count = Integer.parseInt(event.getArgs());
        if (count < 2 || count > 1000 || event.getArgs().isEmpty()) {
			MessageUtil.sendError(event, "command.clear.error.count");
            return;
        }

        try {
            event.getMessage().delete().queue();

            final List<Message> messages = new LinkedList<>();
            final List<Message> delMessages = new LinkedList<>();
            final MessageHistory history = event.getChannel().getHistory();
            final OffsetDateTime dateTime = event.getMessage().getTimeCreated().minusHours(335);
            final TextChannel channel = event.getTextChannel();

            while (count > 100) {
                messages.addAll(history.retrievePast(100).complete());
                count -= 100;
                if (messages.get(messages.size() - 1).getTimeCreated().isBefore(dateTime)) {
                    count = 0;
                    break;
                }
            }

            if (count > 0) messages.addAll(history.retrievePast(count).complete());

            for (final Message message : messages) {
                if (message.getTimeCreated().isBefore(dateTime)) break;
                if (message.isPinned()) continue;
                delMessages.add(message);
            }

            int index = 0;
            while (index < delMessages.size()) {
                if (index + 100 > delMessages.size()) {
					channel.deleteMessages(delMessages.subList(index, delMessages.size())).complete();
				} else {
					channel.deleteMessages(delMessages.subList(index, index + 100)).complete();
				}
                index += 100;
            }
        } catch (Exception e) {
            event.replyError(e.getMessage());
        }
    }
}
