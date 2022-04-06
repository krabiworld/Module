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
import net.dv8tion.jda.api.entities.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WarnCommand extends Command {
	private final ModerationService moderationService;
	private final MessageService messageService;

	@Autowired
	public WarnCommand(ModerationService moderationService, MessageService messageService) {
		this.moderationService = moderationService;
		this.messageService = messageService;
		this.name = "warn";
		this.category = Constants.MODERATION;
	}

	@Override
	protected void execute(CommandEvent event) {
		Locale locale = messageService.getLocale(event.getGuild());
		if (!moderationService.isModerator(event.getMember())) {
			messageService.sendError(event, locale, "error.not.mod");
			return;
		}
		if (event.getArgs().isEmpty()) {
			messageService.sendHelp(event, this, locale);
			return;
		}

		String[] args = ArgsUtil.split(event.getArgs());
		Member member = ArgsUtil.getMember(event, args[0]);
		String reason = ArgsUtil.getGluedArg(args, 1);

		if (member == null || member.getUser().isBot()) {
			messageService.sendHelp(event, this, locale);
			return;
		}
		if (!event.getSelfMember().canInteract(member)) {
			messageService.sendError(event, locale, "command.warn.error.role.position");
			return;
		}
		if (member == event.getMember()) {
			messageService.sendError(event, locale, "command.warn.error.cannot.yourself");
			return;
		}

		long warnId = moderationService.warn(member, reason);

		messageService.sendSuccess(event, locale, "command.warn.success.warned",
			member.getUser().getAsTag(), warnId,
			event.getMember().getEffectiveName(),
			reason.isEmpty() ? "." : " with reason: " + reason);
	}
}
