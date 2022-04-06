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

@Component
public class KickCommand extends Command {
	private final ModerationService moderationService;
	private final MessageService messageService;

	@Autowired
    public KickCommand(ModerationService moderationService, MessageService messageService) {
		this.moderationService = moderationService;
		this.messageService = messageService;
        this.name = "kick";
        this.category = Constants.MODERATION;
        this.userPermissions = new Permission[]{Permission.KICK_MEMBERS};
        this.botPermissions = new Permission[]{Permission.KICK_MEMBERS};
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

        if (member == null) {
			messageService.sendHelp(event, this, locale);
            return;
        }
		if (!event.getSelfMember().canInteract(member)) {
			messageService.sendError(event, locale, "command.kick.error.role.position");
			return;
		}
		if (member == event.getMember()) {
			messageService.sendError(event, locale, "command.kick.error.cannot.yourself");
			return;
		}

        event.getGuild().kick(member, reason).queue();
		messageService.sendSuccess(event, locale, "command.kick.success.kicked",
			member.getUser().getAsTag(),
			event.getMember().getEffectiveName(),
			reason.isEmpty() ? "." : " with reason: " + reason);
    }
}
