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

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import org.module.Constants;
import org.module.Locale;
import org.module.service.MessageService;
import org.module.service.ModerationService;
import org.module.util.CheckUtil;
import net.dv8tion.jda.api.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UnbanCommand extends Command {
	private final ModerationService moderationService;
	private final MessageService messageService;

	@Autowired
    public UnbanCommand(ModerationService moderationService, MessageService messageService) {
		this.moderationService = moderationService;
		this.messageService = messageService;
		this.name = "unban";
        this.category = Constants.MODERATION;
        this.userPermissions = new Permission[]{Permission.BAN_MEMBERS};
        this.botPermissions = new Permission[]{Permission.BAN_MEMBERS};
    }

    @Override
    protected void execute(CommandEvent event) {
		Locale locale = messageService.getLocale(event.getGuild());
		if (!moderationService.isModerator(event.getMember())) {
			messageService.sendError(event, locale, "error.not.mod");
			return;
		}
		if (event.getArgs().isEmpty()) {
			messageService.sendHelp(event, this, locale);
			return;
		}
		if (!CheckUtil.isBanned(event.getArgs(), event.getGuild())) {
			messageService.sendError(event, locale, "command.unban.error.not.found");
			return;
		}

		event.getGuild().unban(event.getArgs()).queue();
		messageService.sendSuccess(event, locale, "command.unban.success.unbanned",
			event.getArgs(), event.getMember().getEffectiveName());
    }
}
