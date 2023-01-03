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
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.module.structure.Category;
import org.module.structure.Command;
import org.module.structure.CommandContext;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

@Component
public class BanCommand extends Command {
	public BanCommand() {
		this.name = "ban";
		this.description = "Ban member";
		this.category = Category.MODERATION;
		this.moderationCommand = true;
		this.options.add(new OptionData(
			OptionType.USER, "user", "User to ban", true
		));
		this.options.add(new OptionData(
			OptionType.STRING, "reason", "Ban reason", false
		));
		this.botPermissions = new Permission[]{Permission.BAN_MEMBERS};
		this.userPermissions = new Permission[]{Permission.BAN_MEMBERS};
	}

	@Override
	protected void execute(CommandContext ctx) {
		Member member = ctx.getOptionAsMember("user");
		String reason = ctx.getOptionAsString("reason");

		if (member == null) {
			ctx.replyHelp();
			return;
		}
		if (!ctx.getSelfMember().canInteract(member)) {
			ctx.replyError("I cannot ban member with role equal or higher than me.");
			return;
		}
		if (member == ctx.getMember()) {
			ctx.replyError("You cannot ban yourself.");
			return;
		}

		ctx.getGuild().ban(member, 0, TimeUnit.SECONDS).reason(reason).queue();
		ctx.replySuccess(MessageFormat.format("**{0}** banned by moderator **{1}**{2}",
			member.getUser().getAsTag(),
			ctx.getMember().getEffectiveName(),
			reason.isEmpty() ? "." : " with reason: " + reason));
	}
}
