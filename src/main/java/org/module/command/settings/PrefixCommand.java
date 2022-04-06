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
import net.dv8tion.jda.api.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PrefixCommand extends Command {
	private final GuildManager manager;
	private final MessageService messageService;

	@Autowired
	public PrefixCommand(GuildManager manager, MessageService messageService) {
		this.manager = manager;
		this.messageService = messageService;
		this.name = "prefix";
		this.category = Constants.SETTINGS;
		this.userPermissions = new Permission[]{Permission.MANAGE_SERVER};
	}

	@Override
	protected void execute(CommandEvent event) {
		Locale locale = messageService.getLocale(event.getGuild());
		String newPrefix = event.getArgs();

		if (newPrefix.length() < 1 || newPrefix.length() > 4) {
			messageService.sendError(event, locale, "command.prefix.error.length");
			return;
		}

		manager.setPrefix(event.getGuild(), newPrefix);

		messageService.sendSuccess(event, locale, "command.prefix.success.changed", event.getArgs());
	}
}
