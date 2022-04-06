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

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import org.module.Constants;
import org.module.Locale;
import org.module.service.MessageService;
import org.module.service.ModerationService;
import org.module.util.ArgsUtil;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class MuteCommand extends Command {
	private final ModerationService moderationService;
	private final MessageService messageService;

	@Autowired
    public MuteCommand(ModerationService moderationService, MessageService messageService) {
		this.moderationService = moderationService;
		this.messageService = messageService;
        this.name = "mute";
        this.category = Constants.MODERATION;
		this.userPermissions = new Permission[]{Permission.MANAGE_ROLES};
        this.botPermissions = new Permission[]{Permission.MANAGE_ROLES};
    }

    @Override
    protected void execute(CommandEvent event) {
		Locale locale = messageService.getLocale(event.getGuild());
		if (!moderationService.isModerator(event.getMember())) {
			messageService.sendError(event, locale, "error.not.mod");
			return;
		}

		String[] args = ArgsUtil.split(event.getArgs());

		if (args.length < 3) {
			messageService.sendHelp(event, this, locale);
			return;
		}

		Member member = ArgsUtil.getMember(event, args[0]);
		long durationLong;
		String unitOfTime = args[2];

        if (member == null) {
			messageService.sendHelp(event, this, locale);
            return;
        }
		if (!event.getSelfMember().canInteract(member)) {
			messageService.sendError(event, locale, "command.mute.error.role.position");
			return;
		}
		if (member == event.getMember()) {
			messageService.sendError(event, locale, "command.mute.error.cannot.yourself");
			return;
		}
		if (member.isTimedOut()) {
			messageService.sendError(event, locale, "command.mute.error.already.muted");
            return;
		}

		try {
			durationLong = Long.parseLong(args[1]);
		} catch (Exception e) {
			messageService.sendHelp(event, this, locale);
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
			messageService.sendHelp(event, this, locale);
			return;
		}

		member.timeoutFor(duration).queue();
		messageService.sendSuccess(event, locale, "command.mute.success.muted",
			member.getUser().getAsTag(),
			event.getMember().getEffectiveName(),
			durationLong, unitOfTime);
    }
}
