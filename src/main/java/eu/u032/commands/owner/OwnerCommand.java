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

package eu.u032.commands.owner;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import eu.u032.model.Owner;
import eu.u032.service.OwnerService;
import eu.u032.util.ArgsUtil;
import eu.u032.util.MessageUtil;
import net.dv8tion.jda.api.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OwnerCommand extends Command {
	@Autowired
	private OwnerService ownerService;

	public OwnerCommand() {
		this.name = MessageUtil.getMessage("command.owner.name");
		this.help = MessageUtil.getMessage("command.owner.help");
		this.arguments = MessageUtil.getMessage("command.owner.arguments");
		this.hidden = true;
		this.ownerCommand = true;
	}

	@Override
	protected void execute(CommandEvent event) {
		String[] args = ArgsUtil.split(event.getArgs());

		if (event.getArgs().isEmpty() || args.length <= 1) {
			MessageUtil.sendHelp(event, this);
			return;
		}

		User user = ArgsUtil.getUser(event, args[1]);

		if (user == null) {
			MessageUtil.sendHelp(event, this);
			return;
		}

		if (args[0].startsWith("add")) {
			Owner owner = new Owner();
			owner.setId(user.getIdLong());

			ownerService.save(owner);

			MessageUtil.sendSuccessMessage(event,
				"Member **" + user.getAsTag() + "** added to owner list.");
		} else if (args[0].startsWith("remove")) {
			Owner owner = ownerService.findById(user.getIdLong());

			if (owner == null) {
				MessageUtil.sendError(event, "command.owner.error.not.found");
				return;
			}

			ownerService.delete(owner);

			MessageUtil.sendSuccessMessage(event,
				"Member **" + user.getAsTag() + "** removed from owner list.");
		}
	}
}
