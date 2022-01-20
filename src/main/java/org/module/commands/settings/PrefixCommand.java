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

package org.module.commands.settings;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import org.module.Constants;
import org.module.manager.GuildManager;
import org.module.service.MessageService;
import org.module.util.PropertyUtil;
import net.dv8tion.jda.api.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PrefixCommand extends Command {
	@Autowired
	private GuildManager manager;

	@Autowired
	private MessageService messageService;

	public PrefixCommand() {
		this.name = PropertyUtil.getProperty("command.prefix.name");
		this.help = PropertyUtil.getProperty("command.prefix.help");
		this.arguments = PropertyUtil.getProperty("command.prefix.arguments");
		this.category = Constants.SETTINGS;
		this.userPermissions = new Permission[]{Permission.MANAGE_SERVER};
	}

	@Override
	protected void execute(CommandEvent event) {
		if (event.getArgs().length() < 1 || event.getArgs().length() > 4) {
			messageService.sendError(event, "command.prefix.error.length");
			return;
		}

		manager.setPrefix(event.getGuild(), event.getArgs());

		messageService.sendSuccessMessage(event, "Prefix changed to **" + event.getArgs() + "**");
	}
}
