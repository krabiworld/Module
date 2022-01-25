/*
 * This file is part of Module.

 * Module is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * Module is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with Module. If not, see <https://www.gnu.org/licenses/>.
 */

package org.module.commands.moderation;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import org.module.constants.Constants;
import org.module.service.MessageService;
import org.module.service.ModerationService;
import org.module.util.ArgsUtil;
import org.module.util.PropertyUtil;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class KickCommand extends Command {
	private final MessageService messageService;
	private final ModerationService moderationService;

	@Autowired
    public KickCommand(MessageService messageService, ModerationService moderationService) {
		this.messageService = messageService;
		this.moderationService = moderationService;
        this.name = PropertyUtil.getProperty("command.kick.name");
        this.help = PropertyUtil.getProperty("command.kick.help");
        this.arguments = PropertyUtil.getProperty("command.kick.arguments");
        this.category = Constants.MODERATION;
        this.userPermissions = new Permission[]{Permission.KICK_MEMBERS};
        this.botPermissions = new Permission[]{Permission.KICK_MEMBERS};
    }

    @Override
    protected void execute(CommandEvent event) {
		if (!moderationService.isModerator(event.getMember())) {
			messageService.sendError(event, "error.not.mod");
			return;
		}
		if (event.getArgs().isEmpty()) {
			messageService.sendHelp(event, this);
			return;
		}

		String[] args = ArgsUtil.split(event.getArgs());
		Member member = ArgsUtil.getMember(event, args[0]);
		String reason = ArgsUtil.getGluedArg(args, 1);

        if (member == null) {
			messageService.sendHelp(event, this);
            return;
        }
		if (!event.getSelfMember().canInteract(member)) {
			messageService.sendError(event, "command.kick.error.role.position");
			return;
		}
		if (member == event.getMember()) {
			messageService.sendError(event, "command.kick.error.cannot.yourself");
			return;
		}

        event.getGuild().kick(member, reason).queue();
		messageService.sendSuccess(event, "command.kick.success.kicked",
			member.getUser().getAsTag(),
			event.getMember().getEffectiveName(),
			reason.isEmpty() ? "." : " with reason: " + reason);
    }
}
