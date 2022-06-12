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
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.module.structure.Category;
import org.module.structure.Command;
import org.module.structure.CommandContext;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
public class ModRoleCommand extends Command {
	public ModRoleCommand() {
		this.name = "modrole";
		this.description = "Set mod role to use moderator commands";
		this.category = Category.SETTINGS;
		this.options.add(
			new OptionData(OptionType.ROLE, "role", "Role to set", true)
		);
		this.userPermissions = new Permission[]{Permission.MANAGE_SERVER};
	}

	@Override
	protected void execute(CommandContext ctx) {
		Role role = ctx.getOptionAsRole("role");
		Role modRole = ctx.getSettings().getModeratorRole();

		if (role == null) {
			ctx.replyError("Role not found.");
			return;
		}
		if (role == modRole) {
			ctx.replyError("This role already set.");
			return;
		}

		ctx.getClient().getManager().setModeratorRole(ctx.getGuild(), role);

		ctx.replySuccess(MessageFormat.format("Moderator role changed to **{0}**.", role.getName()));
	}
}
