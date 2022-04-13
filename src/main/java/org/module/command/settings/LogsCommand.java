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
import net.dv8tion.jda.api.entities.TextChannel;
import org.module.structure.AbstractCommand;
import org.module.structure.Command;
import org.module.structure.CommandContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Command(
	name = "command.logs.name",
	args = "command.logs.args",
	help = "command.logs.help",
	category = "category.settings",
	userPermissions = {Permission.MANAGE_SERVER}
)
public class LogsCommand extends AbstractCommand {
	@Autowired
	public LogsCommand() {
		this.children = new AbstractCommand[]{new OffSubCommand(), new OnSubCommand()};
	}

	@Override
	protected void execute(CommandContext ctx) {
		ctx.sendHelp();
	}

	@Command(name = "command.logs.off.name")
	private static class OffSubCommand extends AbstractCommand {
		@Override
		protected void execute(CommandContext ctx) {
			TextChannel currentLogsChannel = ctx.getSettings().getLogsChannel();

			if (currentLogsChannel == null) {
				ctx.sendError("command.logs.error.already.disabled");
				return;
			}

			ctx.getManager().setLogsChannel(ctx.getGuild(), null);
			ctx.sendSuccess("command.logs.success.disabled");
		}
	}

	@Command(name = "command.logs.on.name")
	private static class OnSubCommand extends AbstractCommand {
		@Override
		protected void execute(CommandContext ctx) {
			TextChannel currentLogsChannel = ctx.getSettings().getLogsChannel();

			if (ctx.getArgs().isEmpty()) {
				ctx.sendHelp();
				return;
			}

			TextChannel logsChannel = ctx.findTextChannel(ctx.getArgs());

			if (logsChannel == null) {
				ctx.sendError("error.channel.not.found");
				return;
			}
			if (logsChannel == currentLogsChannel) {
				ctx.sendError("error.channel.already.set");
				return;
			}

			ctx.getManager().setLogsChannel(ctx.getGuild(), logsChannel);

			ctx.sendSuccess("command.logs.success.changed", logsChannel.getAsMention());
		}
	}
}
