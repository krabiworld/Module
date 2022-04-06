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
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import org.module.Constants;
import org.module.Locale;
import org.module.manager.GuildManager;
import org.module.service.MessageService;
import org.module.util.ArgsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LogsCommand extends Command {
	private final GuildManager manager;
	private final MessageService messageService;

	@Autowired
	public LogsCommand(GuildManager manager, MessageService messageService) {
		this.manager = manager;
		this.messageService = messageService;
		this.name = "logs";
		this.category = Constants.SETTINGS;
		this.userPermissions = new Permission[]{Permission.MANAGE_SERVER};
		this.children = new Command[]{new OffSubCommand(), new OnSubCommand()};
	}

	@Override
	protected void execute(CommandEvent event) {
		Locale locale = messageService.getLocale(event.getGuild());

		messageService.sendHelp(event, this, locale);
	}

	private class OffSubCommand extends Command {
		public OffSubCommand() {
			this.name = "off";
		}

		@Override
		protected void execute(CommandEvent event) {
			Locale locale = messageService.getLocale(event.getGuild());

			GuildManager.GuildSettings settings = manager.getSettings(event.getGuild());
			if (settings == null) return;
			TextChannel currentLogsChannel = settings.getLogsChannel();

			if (currentLogsChannel == null) {
				messageService.sendError(event, locale, "command.logs.error.already.disabled");
				return;
			}
			manager.setLogsChannel(event.getGuild(), null);
			messageService.sendSuccess(event, locale, "command.logs.success.disabled");
		}
	}

	private class OnSubCommand extends Command {
		public OnSubCommand() {
			this.name = "on";
		}

		@Override
		protected void execute(CommandEvent event) {
			Locale locale = messageService.getLocale(event.getGuild());

			GuildManager.GuildSettings settings = LogsCommand.this.manager.getSettings(event.getGuild());
			if (settings == null) return;
			TextChannel currentLogsChannel = settings.getLogsChannel();

			if (event.getArgs().isEmpty()) {
				messageService.sendHelp(event, this, locale);
				return;
			}

			TextChannel logsChannel = ArgsUtil.getTextChannel(event, event.getArgs());

			if (logsChannel == null) {
				messageService.sendError(event, locale, "error.channel.not.found");
				return;
			}
			if (logsChannel == currentLogsChannel) {
				messageService.sendError(event, locale, "error.channel.already.set");
				return;
			}

			manager.setLogsChannel(event.getGuild(), logsChannel);

			messageService.sendSuccess(event, locale, "command.logs.success.changed", logsChannel.getAsMention());
		}
	}
}
