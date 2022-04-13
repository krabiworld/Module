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

import net.dv8tion.jda.api.Permission;
import org.module.structure.AbstractCommand;
import org.module.structure.Command;
import org.module.structure.CommandContext;
import org.springframework.stereotype.Component;

@Component
@Command(
	name = "command.prefix.name",
	args = "command.prefix.args",
	help = "command.prefix.help",
	category = "category.settings",
	userPermissions = {Permission.MANAGE_SERVER}
)
public class PrefixCommand extends AbstractCommand {
	@Override
	protected void execute(CommandContext ctx) {
		String newPrefix = ctx.getArgs();

		if (newPrefix.length() < 1 || newPrefix.length() > 4) {
			ctx.sendError("command.prefix.error.length");
			return;
		}

		ctx.getManager().setPrefix(ctx.getGuild(), newPrefix);

		ctx.sendSuccess("command.prefix.success.changed", ctx.getArgs());
	}
}
