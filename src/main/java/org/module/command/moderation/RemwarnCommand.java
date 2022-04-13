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
import org.module.model.WarnModel;
import org.module.service.ModerationService;
import org.module.structure.AbstractCommand;
import org.module.structure.Command;
import org.module.structure.CommandContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Command(
	name = "command.remwarn.name",
	args = "command.remwarn.args",
	help = "command.remwarn.help",
	category = "category.moderation",
	moderator = true
)
public class RemwarnCommand extends AbstractCommand {
	private final ModerationService moderationService;

	@Autowired
	public RemwarnCommand(ModerationService moderationService) {
		this.moderationService = moderationService;
	}

	@Override
	protected void execute(CommandContext ctx) {
		if (ctx.getArgs().isEmpty()) {
			ctx.sendHelp();
			return;
		}

		WarnModel warnModel = moderationService.getWarn(Long.parseLong(ctx.getArgs()));

		if (warnModel == null || warnModel.getGuild() != ctx.getGuild().getIdLong()) {
			ctx.sendError("command.remwarn.error.not.found");
			return;
		}

		Member member = ctx.getGuild().getMemberById(warnModel.getUser());

		if (member == null) {
			ctx.sendHelp();
			return;
		}

		moderationService.removeWarn(warnModel);

		ctx.sendSuccess("command.remwarn.success.removed.warn",
			ctx.getMember().getEffectiveName(),
			warnModel.getId(),
			member.getUser().getAsTag());
	}
}
