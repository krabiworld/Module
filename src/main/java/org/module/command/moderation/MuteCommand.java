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

import java.time.Duration;

@Component
@Command(
	name = "command.mute.name",
	args = "command.mute.args",
	help = "command.mute.help",
	category = "category.moderation",
	moderator = true,
	botPermissions = {Permission.MANAGE_ROLES},
	userPermissions = {Permission.MANAGE_ROLES}
)
public class MuteCommand extends AbstractCommand {
    @Override
    protected void execute(CommandContext ctx) {
		String[] args = ctx.splitArgs();

		if (args.length < 3) {
			ctx.sendHelp();
			return;
		}

		Member member = ctx.findMember(args[0]);
		long durationLong;
		String unitOfTime = args[2];

        if (member == null) {
			ctx.sendHelp();
            return;
        }
		if (!ctx.getSelfMember().canInteract(member)) {
			ctx.sendError("command.mute.error.role.position");
			return;
		}
		if (member == ctx.getMember()) {
			ctx.sendError("command.mute.error.cannot.yourself");
			return;
		}
		if (member.isTimedOut()) {
			ctx.sendError("command.mute.error.already.muted");
            return;
		}

		try {
			durationLong = Long.parseLong(args[1]);
		} catch (Exception e) {
			ctx.sendHelp();
			return;
		}

		Duration duration;
		try {
			duration = switch (unitOfTime.toLowerCase()) {
				case "s" -> Duration.ofSeconds(durationLong);
				case "m" -> Duration.ofMinutes(durationLong);
				case "h" -> Duration.ofHours(durationLong);
				case "d" -> Duration.ofDays(durationLong);
				default -> throw new Exception(String.format("Unit of time %s not found.", unitOfTime));
			};
		} catch (Exception e) {
			ctx.sendHelp();
			return;
		}

		member.timeoutFor(duration).queue();
		ctx.sendSuccess("command.mute.success.muted",
			member.getUser().getAsTag(),
			ctx.getMember().getEffectiveName(),
			durationLong, unitOfTime);
    }
}
