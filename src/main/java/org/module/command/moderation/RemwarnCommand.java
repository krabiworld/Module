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
import org.module.Locale;
import org.module.model.WarnModel;
import org.module.service.MessageService;
import org.module.service.ModerationService;
import net.dv8tion.jda.api.entities.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.module.Constants.*;

@Component
public class RemwarnCommand extends Command {
	private final ModerationService moderationService;
	private final MessageService messageService;

	@Autowired
	public RemwarnCommand(ModerationService moderationService, MessageService messageService) {
		this.moderationService = moderationService;
		this.messageService = messageService;
		this.name = "remwarn";
		this.category = MODERATION;
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

		WarnModel warnModel = moderationService.getWarn(Long.parseLong(event.getArgs()));

		if (warnModel == null || warnModel.getGuild() != event.getGuild().getIdLong()) {
			messageService.sendError(event, locale, "command.remwarn.error.not.found");
			return;
		}

		Member member = event.getGuild().getMemberById(warnModel.getUser());

		if (member == null) {
			messageService.sendHelp(event, this, locale);
			return;
		}

		moderationService.removeWarn(warnModel);

		messageService.sendSuccess(event, locale, "command.remwarn.success.removed.warn",
			event.getMember().getEffectiveName(),
			warnModel.getId(),
			member.getUser().getAsTag());
	}
}
