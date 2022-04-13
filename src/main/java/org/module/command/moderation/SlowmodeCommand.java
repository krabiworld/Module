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
	name = "command.slowmode.name",
	args = "command.slowmode.args",
	help = "command.slowmode.help",
	category = "category.moderation",
	moderator = true,
	botPermissions = {Permission.MANAGE_CHANNEL},
	userPermissions = {Permission.MANAGE_CHANNEL}
)
public class SlowmodeCommand extends AbstractCommand {
    @Override
    protected void execute(CommandContext ctx) {
		if (ctx.getArgs().isEmpty()) {
			ctx.sendHelp();
            return;
        }

        int interval = Integer.parseInt(ctx.getArgs());

        if (interval < 0 || interval > 21600) {
			ctx.sendHelp();
            return;
        }
        if (ctx.getTextChannel().getSlowmode() == interval) {
			ctx.sendError("command.slowmode.error.already.set");
            return;
        }

        ctx.getTextChannel().getManager().setSlowmode(interval).queue();
		ctx.sendSuccess("command.slowmode.success.changed",
			ctx.getTextChannel().getAsMention(), interval);
    }
}
