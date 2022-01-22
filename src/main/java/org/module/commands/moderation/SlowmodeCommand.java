/*
 * Module Discord Bot.
 * Copyright (C) 2022 untled032, Headcrab

 * Module is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * Module is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with Module. If not, see https://www.gnu.org/licenses/.
 */

package org.module.commands.moderation;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import org.module.constants.Constants;
import org.module.service.MessageService;
import org.module.service.ModerationService;
import org.module.util.PropertyUtil;
import net.dv8tion.jda.api.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SlowmodeCommand extends Command {
	@Autowired
	private MessageService messageService;

	@Autowired
	private ModerationService moderationService;

    public SlowmodeCommand() {
		this.name = PropertyUtil.getProperty("command.slowmode.name");
		this.help = PropertyUtil.getProperty("command.slowmode.help");
		this.arguments = PropertyUtil.getProperty("command.slowmode.arguments");
        this.category = Constants.MODERATION;
        this.userPermissions = new Permission[]{Permission.MANAGE_CHANNEL};
        this.botPermissions = new Permission[]{Permission.MANAGE_CHANNEL};
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

        int interval = Integer.parseInt(event.getArgs());

        if (interval < 0 || interval > 21600) {
			messageService.sendHelp(event, this);
            return;
        }
        if (event.getTextChannel().getSlowmode() == interval) {
			messageService.sendError(event, "command.slowmode.error.already.set");
            return;
        }

        event.getTextChannel().getManager().setSlowmode(interval).queue();
		messageService.sendSuccess(event, "command.slowmode.success.changed",
			event.getTextChannel().getAsMention(), interval);
    }
}
