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

package eu.u032.commands.settings;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import eu.u032.Constants;
import eu.u032.manager.GuildManager;
import eu.u032.util.ArgsUtil;
import eu.u032.util.SettingsUtil;
import eu.u032.util.MessageUtil;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;

public class ModroleCommand extends Command {
	private final GuildManager manager;

	public ModroleCommand(GuildManager manager) {
		this.manager = manager;
		this.name = MessageUtil.getMessage("command.modrole.name");
		this.help = MessageUtil.getMessage("command.modrole.help");
		this.arguments = MessageUtil.getMessage("command.modrole.arguments");
		this.category = Constants.SETTINGS;
		this.userPermissions = new Permission[]{Permission.MANAGE_SERVER};
	}

	@Override
	protected void execute(CommandEvent event) {
		if (event.getArgs().isEmpty()) {
			MessageUtil.sendHelp(event, this);
			return;
		}

		Role role = ArgsUtil.getRole(event, event.getArgs());
		Role modRole = SettingsUtil.getModRole(event.getGuild());

		if (role == null) {
			MessageUtil.sendError(event, "error.role.not.found");
			return;
		}
		if (role == modRole) {
			MessageUtil.sendError(event, "error.role.already.set");
			return;
		}

		manager.setMod(event.getGuild(), role.getIdLong());

		MessageUtil.sendSuccessMessage(event, "Moderator role changed to **" + role.getName() + "**");
	}
}
