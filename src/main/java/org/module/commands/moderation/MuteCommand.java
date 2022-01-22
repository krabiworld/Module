/*
 * Module Discord Bot.
 * Copyright (C) 2022 untled032, Headcrab

 * Module is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * Module is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with Module. If not, see https://www.gnu.org/licenses/.
 */

package org.module.commands.moderation;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import org.module.constants.Constants;
import org.module.service.MessageService;
import org.module.service.ModerationService;
import org.module.util.ArgsUtil;
import org.module.util.CheckUtil;
import org.module.util.SettingsUtil;
import org.module.util.PropertyUtil;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MuteCommand extends Command {
	@Autowired
	private MessageService messageService;

	@Autowired
	private ModerationService moderationService;

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
			messageService.sendError(event, "error.not.mod");
			return;
		}
		if (event.getArgs().isEmpty()) {
			messageService.sendHelp(event, this);
			return;
		}

		String[] args = ArgsUtil.split(event.getArgs());
		Role muteRole = SettingsUtil.getMuteRole(event.getGuild());
		Member member = ArgsUtil.getMember(event, args[0]);

		if (muteRole == null) {
			messageService.sendError(event, "command.mute.error.role.not.set");
			return;
		}
        if (member == null) {
			messageService.sendHelp(event, this);
            return;
        }
		if (!event.getSelfMember().canInteract(member)) {
			messageService.sendError(event, "command.mute.error.role.position");
			return;
		}
		if (member == event.getMember()) {
			messageService.sendError(event, "command.mute.error.cannot.yourself");
			return;
		}
        if (CheckUtil.hasRole(member, muteRole)) {
			messageService.sendError(event, "command.mute.error.already.muted");
            return;
        }

        event.getGuild().addRoleToMember(member, muteRole).queue();
		messageService.sendSuccess(event, "command.mute.success.muted",
			member.getUser().getAsTag(),
			event.getMember().getEffectiveName());
    }
}
