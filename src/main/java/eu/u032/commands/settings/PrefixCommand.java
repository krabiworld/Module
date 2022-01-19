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
import eu.u032.manager.GuildManager;
import eu.u032.util.MessageUtil;
import net.dv8tion.jda.api.Permission;

public class PrefixCommand extends Command {
	private final GuildManager manager;

	public PrefixCommand(GuildManager manager) {
		this.manager = manager;
		this.name = MessageUtil.getMessage("command.prefix.name");
		this.help = MessageUtil.getMessage("command.prefix.help");
		this.arguments = MessageUtil.getMessage("command.prefix.arguments");
		this.category = Constants.SETTINGS;
		this.userPermissions = new Permission[]{Permission.MANAGE_SERVER};
	}

	@Override
	protected void execute(CommandEvent event) {
		if (event.getArgs().length() < 1 || event.getArgs().length() > 4) {
			MessageUtil.sendError(event, "command.prefix.error.length");
			return;
		}

		manager.setPrefix(event.getGuild(), event.getArgs());

		MessageUtil.sendSuccessMessage(event, "Prefix changed to **" + event.getArgs() + "**");
	}
}
