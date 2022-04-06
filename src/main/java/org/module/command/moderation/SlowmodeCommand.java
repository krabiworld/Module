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
import net.dv8tion.jda.api.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SlowmodeCommand extends Command {
	private final ModerationService moderationService;
	private final MessageService messageService;

	@Autowired
    public SlowmodeCommand(ModerationService moderationService, MessageService messageService) {
		this.moderationService = moderationService;
		this.messageService = messageService;
		this.name = "slowmode";
        this.category = Constants.MODERATION;
        this.userPermissions = new Permission[]{Permission.MANAGE_CHANNEL};
        this.botPermissions = new Permission[]{Permission.MANAGE_CHANNEL};
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

        int interval = Integer.parseInt(event.getArgs());

        if (interval < 0 || interval > 21600) {
			messageService.sendHelp(event, this, locale);
            return;
        }
        if (event.getTextChannel().getSlowmode() == interval) {
			messageService.sendError(event, locale, "command.slowmode.error.already.set");
            return;
        }

        event.getTextChannel().getManager().setSlowmode(interval).queue();
		messageService.sendSuccess(event, locale, "command.slowmode.success.changed",
			event.getTextChannel().getAsMention(), interval);
    }
}
