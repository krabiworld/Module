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

package org.module.command.owner;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.entities.User;
import org.module.Constants;
import org.module.Locale;
import org.module.service.MessageService;
import org.module.service.OwnerService;
import org.module.util.ArgsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OwnerCommand extends Command {
	private final OwnerService ownerService;
	private final MessageService messageService;

	@Autowired
	public OwnerCommand(OwnerService ownerService, MessageService messageService) {
		this.ownerService = ownerService;
		this.messageService = messageService;
		this.name = "owner";
		this.category = Constants.OWNER;
		this.hidden = true;
		this.ownerCommand = true;
	}

	@Override
	protected void execute(CommandEvent event) {
		Locale locale = messageService.getLocale(event.getGuild());
		String[] args = ArgsUtil.split(event.getArgs());

		if (event.getArgs().isEmpty() || args.length <= 1) {
			messageService.sendHelp(event, this, locale);
			return;
		}

		User user = ArgsUtil.getUser(event, args[1]);

		if (user == null) {
			messageService.sendHelp(event, this, locale);
			return;
		}

		if (args[0].startsWith("add")) {
			boolean isAdded = ownerService.addOwner(user);

			if (!isAdded) {
				messageService.sendError(event, locale, "command.owner.error.already.added");
				return;
			}

			messageService.sendSuccess(event, locale, "command.owner.success.added", user.getAsTag());
		} else if (args[0].startsWith("remove")) {
			boolean isRemoved = ownerService.removeOwner(user);

			if (!isRemoved) {
				messageService.sendError(event, locale, "command.owner.error.not.found");
				return;
			}

			messageService.sendSuccess(event, locale, "command.owner.success.removed", user.getAsTag());
		}
	}
}
