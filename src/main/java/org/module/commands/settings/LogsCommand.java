/*
 * This file is part of Module.

 * Module is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * Module is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with Module. If not, see <https://www.gnu.org/licenses/>.
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
import net.dv8tion.jda.api.entities.TextChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LogsCommand extends Command {
	private final MessageService messageService;
	private final GuildManager manager;

	@Autowired
	public LogsCommand(MessageService messageService, GuildManager manager) {
		this.messageService = messageService;
		this.manager = manager;
		this.name = PropertyUtil.getProperty("command.logs.name");
		this.help = PropertyUtil.getProperty("command.logs.help");
		this.arguments = PropertyUtil.getProperty("command.logs.arguments");
		this.category = Constants.SETTINGS;
		this.userPermissions = new Permission[]{Permission.MANAGE_SERVER};
	}

	@Override
	protected void execute(CommandEvent event) {
		if (event.getArgs().isEmpty()) {
			messageService.sendHelp(event, this);
			return;
		}

		String[] args = ArgsUtil.split(event.getArgs());
		TextChannel currentLogsChannel = SettingsUtil.getLogsChannel(event.getGuild());

		if (args[0].startsWith("off")) {
			if (currentLogsChannel == null) {
				messageService.sendError(event, "command.logs.error.already.disabled");
				return;
			}
			manager.setLogsChannel(event.getGuild(), null);
			messageService.sendSuccess(event, "command.logs.success.disabled");
		} else if (args[0].startsWith("on")) {
			if (args.length <= 1) {
				messageService.sendHelp(event, this);
				return;
			}

			TextChannel logsChannel = ArgsUtil.getTextChannel(event, args[1]);

			if (logsChannel == null) {
				messageService.sendError(event, "error.channel.not.found");
				return;
			}
			if (logsChannel == currentLogsChannel) {
				messageService.sendError(event, "error.channel.already.set");
				return;
			}

			manager.setLogsChannel(event.getGuild(), logsChannel);

			messageService.sendSuccess(event, "command.logs.success.changed", logsChannel.getAsMention());
		} else {
			messageService.sendHelp(event, this);
		}
	}
}
