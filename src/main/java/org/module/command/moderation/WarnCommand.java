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
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.module.service.ModerationService;
import org.module.structure.Category;
import org.module.structure.Command;
import org.module.structure.CommandContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
public class WarnCommand extends Command {
	private final ModerationService moderationService;

	@Autowired
	public WarnCommand(ModerationService moderationService) {
		this.name = "warn";
		this.description = "Warn member";
		this.category = Category.MODERATION;
		this.moderationCommand = true;
		this.options.add(new OptionData(
			OptionType.USER, "user", "User to warn", true
		));
		this.options.add(new OptionData(
			OptionType.STRING, "reason", "Warn reason", false
		));
		this.moderationService = moderationService;
	}

	@Override
	protected void execute(CommandContext ctx) {
		Member member = ctx.getOptionAsMember("user");
		String reason = ctx.getOptionAsString("reason");

		if (member == null || member.getUser().isBot()) {
			ctx.replyHelp();
			return;
		}
		if (!ctx.getSelfMember().canInteract(member)) {
			ctx.replyError("I cannot warnModel member with role equal or higher than me.");
			return;
		}
		if (member == ctx.getMember()) {
			ctx.replyError("You cannot warnModel yourself.");
			return;
		}

		long warnId = moderationService.warn(member, reason);

		ctx.replySuccess(MessageFormat.format("**{0}** warned (ID: `#{1}`) by moderator **{2}**{3}",
			member.getUser().getAsTag(), warnId,
			ctx.getMember().getEffectiveName(),
			reason.isEmpty() ? "." : " with reason: " + reason));
	}
}
