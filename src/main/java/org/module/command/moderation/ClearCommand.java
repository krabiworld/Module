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

package org.module.command.moderation;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.module.structure.Category;
import org.module.structure.Command;
import org.module.structure.CommandContext;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.time.OffsetDateTime;
import java.util.LinkedList;
import java.util.List;

@Component
public class ClearCommand extends Command {
	public ClearCommand() {
		this.name = "clear";
		this.description = "Clear last messages in current channel";
		this.category = Category.MODERATION;
		this.moderationCommand = true;
		this.options.add(
			new OptionData(OptionType.INTEGER, "count", "Count to delete messages", true)
		);
		this.botPermissions = new Permission[]{Permission.MESSAGE_MANAGE, Permission.MESSAGE_ATTACH_FILES};
		this.userPermissions = new Permission[]{Permission.MESSAGE_MANAGE};
	}

    @Override
    protected void execute(CommandContext ctx) {
        int count = ctx.getOptionAsInt("count");
        if (count < 2 || count > 1000) {
			ctx.replyHelp();
            return;
        }

        try {
			List<Message> messages = new LinkedList<>();
            List<Message> delMessages = new LinkedList<>();
            MessageHistory history = ctx.getChannel().getHistory();
            OffsetDateTime dateTime = ctx.getChannel().getTimeCreated().minusHours(335);
            TextChannel channel = ctx.getTextChannel();

            while (count > 100) {
                messages.addAll(history.retrievePast(100).complete());
                count -= 100;
                if (messages.get(messages.size() - 1).getTimeCreated().isBefore(dateTime)) {
                    count = 0;
                    break;
                }
            }

            if (count > 0) messages.addAll(history.retrievePast(count).complete());

            for (Message message : messages) {
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

			ctx.replySuccess(MessageFormat.format("Successfully cleared {0} messages.", delMessages.size()));
        } catch (Exception e) {
            ctx.reply(e.getMessage());
        }
    }
}
