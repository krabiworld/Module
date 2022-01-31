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

package org.module.commands.moderation;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import org.module.Constants;
import org.module.service.ModerationService;
import org.module.service.impl.ModerationServiceImpl;
import org.module.util.ArgsUtil;
import org.module.util.MessageUtil;
import org.module.util.PropertyUtil;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class MuteCommand extends Command {
	private final Logger logger = LoggerFactory.getLogger(MuteCommand.class);
	private final ModerationService moderationService = new ModerationServiceImpl();
	
    public MuteCommand() {
        this.name = PropertyUtil.getProperty("command.mute.name");
        this.help = PropertyUtil.getProperty("command.mute.help");
        this.arguments = PropertyUtil.getProperty("command.mute.arguments");
        this.category = Constants.MODERATION;
		this.userPermissions = new Permission[]{Permission.MANAGE_ROLES};
        this.botPermissions = new Permission[]{Permission.MANAGE_ROLES};
    }

    @Override
    protected void execute(CommandEvent event) {
		if (!moderationService.isModerator(event.getMember())) {
			MessageUtil.sendError(event, "error.not.mod");
			return;
		}

		String[] args = ArgsUtil.split(event.getArgs());

		if (args.length < 3) {
			MessageUtil.sendHelp(event, this);
			return;
		}

		Member member = ArgsUtil.getMember(event, args[0]);
		long durationLong;
		String unitOfTime = args[2];

        if (member == null) {
			MessageUtil.sendHelp(event, this);
            return;
        }
		if (!event.getSelfMember().canInteract(member)) {
			MessageUtil.sendError(event, "command.mute.error.role.position");
			return;
		}
		if (member == event.getMember()) {
			MessageUtil.sendError(event, "command.mute.error.cannot.yourself");
			return;
		}
		if (member.isTimedOut()) {
			MessageUtil.sendError(event, "command.mute.error.already.muted");
            return;
		}

		try {
			durationLong = Long.parseLong(args[1]);
		} catch (Exception e) {
			logger.error(e.getMessage());
			MessageUtil.sendHelp(event, this);
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
			logger.error(e.getMessage());
			MessageUtil.sendHelp(event, this);
			return;
		}

		member.timeoutFor(duration).queue();
		MessageUtil.sendSuccess(event, "command.mute.success.muted",
			member.getUser().getAsTag(),
			event.getMember().getEffectiveName(),
			durationLong, unitOfTime);
    }
}
