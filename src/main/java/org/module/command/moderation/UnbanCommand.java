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

package org.module.command.moderation;

import net.dv8tion.jda.api.Permission;
import org.module.structure.AbstractCommand;
import org.module.structure.Command;
import org.module.structure.CommandContext;
import org.springframework.stereotype.Component;

@Component
@Command(
	name = "command.unban.name",
	args = "command.unban.args",
	help = "command.unban.help",
	category = "category.moderation",
	moderator = true,
	botPermissions = {Permission.BAN_MEMBERS},
	userPermissions = {Permission.BAN_MEMBERS}
)
public class UnbanCommand extends AbstractCommand {
    @Override
    protected void execute(CommandContext ctx) {
		if (ctx.getArgs().isEmpty()) {
			ctx.sendHelp();
			return;
		}
		if (!ctx.isBanned(ctx.getArgs())) {
			ctx.sendError("command.unban.error.not.found");
			return;
		}

		ctx.getGuild().unban(ctx.getArgs()).queue();
		ctx.sendSuccess("command.unban.success.unbanned",
			ctx.getArgs(), ctx.getMember().getEffectiveName());
    }
}
