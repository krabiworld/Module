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
import org.module.Constants;
import org.module.Locale;
import org.module.manager.GuildManager;
import org.module.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LangCommand extends Command {
	private final GuildManager manager;
	private final MessageService messageService;

	@Autowired
	public LangCommand(GuildManager manager, MessageService messageService) {
		this.manager = manager;
		this.messageService = messageService;
		this.name = "lang";
		this.category = Constants.SETTINGS;
		this.userPermissions = new Permission[]{Permission.MANAGE_SERVER};
		this.children = new Command[]{new EnSubCommand(), new RuSubCommand()};
	}

	@Override
	protected void execute(CommandEvent event) {
		Locale locale = messageService.getLocale(event.getGuild());
		messageService.sendHelp(event, this, locale);
	}

	private class EnSubCommand extends Command {
		public EnSubCommand() {
			this.name = "en";
		}

		@Override
		protected void execute(CommandEvent event) {
			Locale locale = messageService.getLocale(event.getGuild());
			manager.setLang(event.getGuild(), Constants.Language.EN);
			messageService.sendSuccess(event, locale, "command.lang.success.changed", "en");
		}
	}

	private class RuSubCommand extends Command {
		public RuSubCommand() {
			this.name = "ru";
		}

		@Override
		protected void execute(CommandEvent event) {
			Locale locale = messageService.getLocale(event.getGuild());
			manager.setLang(event.getGuild(), Constants.Language.RU);
			messageService.sendSuccess(event, locale, "command.lang.success.changed", "ru");
		}
	}
}
