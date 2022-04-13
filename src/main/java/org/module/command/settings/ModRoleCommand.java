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
import net.dv8tion.jda.api.entities.Role;
import org.module.structure.AbstractCommand;
import org.module.structure.Command;
import org.module.structure.CommandContext;
import org.springframework.stereotype.Component;

@Component
@Command(
	name = "command.modrole.name",
	args = "command.modrole.args",
	help = "command.modrole.help",
	category = "category.settings",
	userPermissions = {Permission.MANAGE_SERVER}
)
public class ModRoleCommand extends AbstractCommand {
	@Override
	protected void execute(CommandContext ctx) {
		if (ctx.getArgs().isEmpty()) {
			ctx.sendHelp();
			return;
		}

		Role role = ctx.findRole(ctx.getArgs());

		Role modRole = ctx.getSettings().getModeratorRole();
		if (role == null) {
			ctx.sendError("error.role.not.found");
			return;
		}
		if (role == modRole) {
			ctx.sendError("error.role.already.set");
			return;
		}

		ctx.getManager().setModeratorRole(ctx.getGuild(), role);

		ctx.sendSuccess("command.modrole.success.changed", role.getName());
	}
}
