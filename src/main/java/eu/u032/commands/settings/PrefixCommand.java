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
import eu.u032.utils.MsgUtil;
import net.dv8tion.jda.api.Permission;

public class PrefixCommand extends Command {
	final GuildManager manager;

	public PrefixCommand(GuildManager manager) {
		this.manager = manager;
		this.name = "prefix";
		this.help = "Change prefix on this server";
		this.arguments = "<prefix>";
		this.category = Constants.SETTINGS;
		this.userPermissions = new Permission[]{Permission.MANAGE_SERVER};
	}

	@Override
	protected void execute(final CommandEvent event) {
		if (event.getArgs().length() < 2 || event.getArgs().length() > 4) {
			MsgUtil.sendError(event, "The prefix length should not be less than 2 and greater than 4.");
		}

		manager.setPrefix(event.getGuild(), event.getArgs());

		MsgUtil.sendSuccess(event, "Prefix changed to **" + event.getArgs() + "**");
	}
}
