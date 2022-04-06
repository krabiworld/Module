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

package org.module.command.settings;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import org.module.Constants;
import org.module.Locale;
import org.module.manager.GuildManager;
import org.module.service.MessageService;
import org.module.util.ArgsUtil;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ModRoleCommand extends Command {
	private final GuildManager manager;
	private final MessageService messageService;

	@Autowired
	public ModRoleCommand(GuildManager manager, MessageService messageService) {
		this.manager = manager;
		this.messageService = messageService;
		this.name = "modrole";
		this.category = Constants.SETTINGS;
		this.userPermissions = new Permission[]{Permission.MANAGE_SERVER};
	}

	@Override
	protected void execute(CommandEvent event) {
		Locale locale = messageService.getLocale(event.getGuild());
		if (event.getArgs().isEmpty()) {
			messageService.sendHelp(event, this, locale);
			return;
		}

		Role role = ArgsUtil.getRole(event, event.getArgs());

		GuildManager.GuildSettings settings = manager.getSettings(event.getGuild());
		if (settings == null) return;

		Role modRole = settings.getModeratorRole();
		if (role == null) {
			messageService.sendError(event, locale, "error.role.not.found");
			return;
		}
		if (role == modRole) {
			messageService.sendError(event, locale, "error.role.already.set");
			return;
		}

		manager.setModeratorRole(event.getGuild(), role);

		messageService.sendSuccess(event, locale, "command.modrole.success.changed", role.getName());
	}
}
