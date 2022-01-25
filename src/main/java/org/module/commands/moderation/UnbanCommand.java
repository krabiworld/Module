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

package org.module.commands.moderation;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import org.module.constants.Constants;
import org.module.service.MessageService;
import org.module.service.ModerationService;
import org.module.util.CheckUtil;
import org.module.util.PropertyUtil;
import net.dv8tion.jda.api.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UnbanCommand extends Command {
	private final MessageService messageService;
	private final ModerationService moderationService;

	@Autowired
    public UnbanCommand(MessageService messageService, ModerationService moderationService) {
		this.messageService = messageService;
		this.moderationService = moderationService;
		this.name = PropertyUtil.getProperty("command.unban.name");
		this.help = PropertyUtil.getProperty("command.unban.help");
		this.arguments = PropertyUtil.getProperty("command.unban.arguments");
        this.category = Constants.MODERATION;
        this.userPermissions = new Permission[]{Permission.BAN_MEMBERS};
        this.botPermissions = new Permission[]{Permission.BAN_MEMBERS};
    }

    @Override
    protected void execute(CommandEvent event) {
		if (!moderationService.isModerator(event.getMember())) {
			messageService.sendError(event, "error.not.mod");
			return;
		}
		if (event.getArgs().isEmpty()) {
			messageService.sendHelp(event, this);
			return;
		}
		if (!CheckUtil.isBanned(event.getArgs(), event.getGuild())) {
			messageService.sendError(event, "command.unban.error.not.found");
			return;
		}

		event.getGuild().unban(event.getArgs()).queue();
		messageService.sendSuccess(event, "command.unban.success.unbanned",
			event.getArgs(), event.getMember().getEffectiveName());
    }
}
