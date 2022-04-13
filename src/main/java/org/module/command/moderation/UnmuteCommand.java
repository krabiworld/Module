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
import net.dv8tion.jda.api.entities.Member;
import org.module.structure.AbstractCommand;
import org.module.structure.Command;
import org.module.structure.CommandContext;
import org.springframework.stereotype.Component;

@Component
@Command(
	name = "command.unmute.name",
	args = "command.unmute.args",
	help = "command.unmute.help",
	category = "category.moderation",
	moderator = true,
	botPermissions = {Permission.MANAGE_ROLES},
	userPermissions = {Permission.MANAGE_ROLES}
)
public class UnmuteCommand extends AbstractCommand {
    @Override
    protected void execute(CommandContext ctx) {
		if (ctx.getArgs().isEmpty()) {
			ctx.sendHelp();
			return;
		}

		String[] args = ctx.splitArgs();
		Member member = ctx.findMember(args[0]);

        if (member == null) {
			ctx.sendHelp();
            return;
        }
		if (!ctx.getSelfMember().canInteract(member)) {
			ctx.sendError("command.unmute.error.role.position");
			return;
		}
		if (!member.isTimedOut()) {
			ctx.sendError("command.unmute.error.not.muted");
            return;
		}

		member.removeTimeout().queue();
		ctx.sendSuccess("command.unmute.success.unmuted",
			member.getUser().getAsTag(), ctx.getMember().getEffectiveName());
    }
}
