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
import net.dv8tion.jda.api.entities.TextChannel;

public class LogsCommand extends Command {
	private final GuildManager manager;

	public LogsCommand(GuildManager manager) {
		this.manager = manager;
		this.name = MessageUtil.getMessage("command.logs.name");
		this.help = MessageUtil.getMessage("command.logs.help");
		this.arguments = MessageUtil.getMessage("command.logs.arguments");
		this.category = Constants.SETTINGS;
		this.userPermissions = new Permission[]{Permission.MANAGE_SERVER};
	}

	@Override
	protected void execute(final CommandEvent event) {
		if (event.getArgs().isEmpty()) {
			MessageUtil.sendError(event, "error.missing.args");
			return;
		}

		final String[] args = ArgsUtil.split(event.getArgs());

		if (args[0].startsWith("off")) {
			if (GeneralUtil.getLogsChannel(event.getGuild()) == null) {
				MessageUtil.sendError(event, "command.logs.error.already.disabled");
				return;
			}
			manager.setLogs(event.getGuild(), 0);
			MessageUtil.sendSuccessMessage(event, "Logs are disabled.");
		} else if (args[0].startsWith("on")) {
			if (args.length <= 1) {
				MessageUtil.sendError(event, "error.missing.args");
				return;
			}

			final TextChannel channel = ArgsUtil.getChannel(event, args[1]);

			if (channel == null) {
				MessageUtil.sendError(event, "error.channel.not.found");
				return;
			}

			manager.setLogs(event.getGuild(), channel.getIdLong());

			MessageUtil.sendSuccess(event, "command.logs.success.changed", channel.getAsMention());
		} else {
			MessageUtil.sendError(event, "error.missing.args");
		}
	}
}
