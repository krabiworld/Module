/*
 * Module Discord Bot.
 * Copyright (C) 2022 untled032, Headcrab

 * Module is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * Module is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with Module. If not, see https://www.gnu.org/licenses/.
 */

package org.module.commands.owner;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import org.module.model.Owner;
import org.module.service.MessageService;
import org.module.service.OwnerService;
import org.module.util.ArgsUtil;
import org.module.util.PropertyUtil;
import net.dv8tion.jda.api.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OwnerCommand extends Command {
	@Autowired
	private OwnerService ownerService;

	@Autowired
	private MessageService messageService;

	public OwnerCommand() {
		this.name = PropertyUtil.getProperty("command.owner.name");
		this.help = PropertyUtil.getProperty("command.owner.help");
		this.arguments = PropertyUtil.getProperty("command.owner.arguments");
		this.hidden = true;
		this.ownerCommand = true;
	}

	@Override
	protected void execute(CommandEvent event) {
		String[] args = ArgsUtil.split(event.getArgs());

		if (event.getArgs().isEmpty() || args.length <= 1) {
			messageService.sendHelp(event, this);
			return;
		}

		User user = ArgsUtil.getUser(event, args[1]);

		if (user == null) {
			messageService.sendHelp(event, this);
			return;
		}

		if (args[0].startsWith("add")) {
			Owner owner = new Owner();
			owner.setId(user.getIdLong());

			ownerService.addOwner(owner);

			messageService.sendSuccess(event, "command.owner.success.added", user.getAsTag());
		} else if (args[0].startsWith("remove")) {
			Owner owner = ownerService.findById(user.getIdLong());

			if (owner == null) {
				messageService.sendError(event, "command.owner.error.not.found");
				return;
			}

			ownerService.removeOwner(owner);

			messageService.sendSuccess(event, "command.owner.success.removed", user.getAsTag());
		}
	}
}
