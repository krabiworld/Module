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

package org.module.commands.settings;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import org.module.constants.Constants;
import org.module.manager.GuildManager;
import org.module.service.MessageService;
import org.module.util.ArgsUtil;
import org.module.util.SettingsUtil;
import org.module.util.PropertyUtil;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ModRoleCommand extends Command {
	@Autowired
	private GuildManager manager;

	@Autowired
	private MessageService messageService;

	public ModRoleCommand() {
		this.name = PropertyUtil.getProperty("command.modrole.name");
		this.help = PropertyUtil.getProperty("command.modrole.help");
		this.arguments = PropertyUtil.getProperty("command.modrole.arguments");
		this.category = Constants.SETTINGS;
		this.userPermissions = new Permission[]{Permission.MANAGE_SERVER};
	}

	@Override
	protected void execute(CommandEvent event) {
		if (event.getArgs().isEmpty()) {
			messageService.sendHelp(event, this);
			return;
		}

		Role role = ArgsUtil.getRole(event, event.getArgs());
		Role modRole = SettingsUtil.getModRole(event.getGuild());

		if (role == null) {
			messageService.sendError(event, "error.role.not.found");
			return;
		}
		if (role == modRole) {
			messageService.sendError(event, "error.role.already.set");
			return;
		}

		manager.setModeratorRole(event.getGuild(), role);

		messageService.sendSuccess(event, "command.modrole.success.changed", role.getName());
	}
}
