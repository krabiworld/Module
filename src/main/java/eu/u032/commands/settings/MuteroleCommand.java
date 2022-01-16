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

package eu.u032.commands.settings;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import eu.u032.Constants;
import eu.u032.GuildManager;
import eu.u032.util.ArgsUtil;
import eu.u032.util.GeneralUtil;
import eu.u032.util.MessageUtil;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;

public class MuteroleCommand extends Command {
	final GuildManager manager;

	public MuteroleCommand(GuildManager manager) {
		this.manager = manager;
		this.name = MessageUtil.getMessage("command.muterole.name");
		this.help = MessageUtil.getMessage("command.muterole.help");
		this.arguments = MessageUtil.getMessage("command.muterole.arguments");
		this.category = Constants.SETTINGS;
		this.userPermissions = new Permission[]{Permission.MANAGE_SERVER};
	}

	@Override
	protected void execute(final CommandEvent event) {
		if (event.getArgs().isEmpty()) {
			MessageUtil.sendError(event, "error.missing.args");
			return;
		}

		final Role role = ArgsUtil.getRole(event, event.getArgs());
		final Role muteRole = GeneralUtil.getMuteRole(event.getGuild());

		if (role == null) {
			MessageUtil.sendError(event, "error.role.not.found");
			return;
		}
		if (role.getIdLong() == (muteRole == null ? 0 : muteRole.getIdLong())) {
			MessageUtil.sendError(event, "error.role.already.set");
		}

		manager.setMute(event.getGuild(), role.getIdLong());

		MessageUtil.sendSuccessMessage(event, "Mute role changed to **" + role.getName() + "**");
	}
}
