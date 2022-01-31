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

package org.module.commands.owner;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import org.module.Constants;
import org.module.model.Owner;
import org.module.service.OwnerService;
import org.module.service.impl.OwnerServiceImpl;
import org.module.util.ArgsUtil;
import org.module.util.MessageUtil;
import org.module.util.PropertyUtil;
import net.dv8tion.jda.api.entities.User;

public class OwnerCommand extends Command {
	private final OwnerService ownerService = new OwnerServiceImpl();

	public OwnerCommand() {
		this.name = PropertyUtil.getProperty("command.owner.name");
		this.help = PropertyUtil.getProperty("command.owner.help");
		this.arguments = PropertyUtil.getProperty("command.owner.arguments");
		this.category = Constants.OWNER;
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

			ownerService.addOwner(owner);

			MessageUtil.sendSuccess(event, "command.owner.success.added", user.getAsTag());
		} else if (args[0].startsWith("remove")) {
			Owner owner = ownerService.getOwner(user.getIdLong());

			if (owner == null) {
				MessageUtil.sendError(event, "command.owner.error.not.found");
				return;
			}

			ownerService.removeOwner(owner);

			MessageUtil.sendSuccess(event, "command.owner.success.removed", user.getAsTag());
		}
	}
}
