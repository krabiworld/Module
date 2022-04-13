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

import net.dv8tion.jda.api.entities.Member;
import org.module.service.ModerationService;
import org.module.structure.AbstractCommand;
import org.module.structure.Command;
import org.module.structure.CommandContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Command(
	name = "command.warn.name",
	args = "command.warn.args",
	help = "command.warn.help",
	category = "category.moderation",
	moderator = true
)
public class WarnCommand extends AbstractCommand {
	private final ModerationService moderationService;

	@Autowired
	public WarnCommand(ModerationService moderationService) {
		this.moderationService = moderationService;
	}

	@Override
	protected void execute(CommandContext ctx) {
		if (ctx.getArgs().isEmpty()) {
			ctx.sendHelp();
			return;
		}

		String[] args = ctx.splitArgs();
		Member member = ctx.findMember(args[0]);
		String reason = ctx.getGluedArg(args, 1);

		if (member == null || member.getUser().isBot()) {
			ctx.sendHelp();
			return;
		}
		if (!ctx.getSelfMember().canInteract(member)) {
			ctx.sendError("command.warn.error.role.position");
			return;
		}
		if (member == ctx.getMember()) {
			ctx.sendError("command.warn.error.cannot.yourself");
			return;
		}

		long warnId = moderationService.warn(member, reason);

		ctx.sendSuccess("command.warn.success.warned",
			member.getUser().getAsTag(), warnId,
			ctx.getMember().getEffectiveName(),
			reason.isEmpty() ? "." : " with reason: " + reason);
	}
}
