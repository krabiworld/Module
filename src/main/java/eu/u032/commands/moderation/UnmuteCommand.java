/*
 * UASM Discord Bot.
 * Copyright (C) 2022 untled032, Headcrab

 * UASM is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * UASM is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with UASM. If not, see https://www.gnu.org/licenses/.
 */

package eu.u032.commands.moderation;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import eu.u032.Constants;
import eu.u032.util.ArgsUtil;
import eu.u032.util.GeneralUtil;
import eu.u032.util.MessageUtil;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

public class UnmuteCommand extends Command {
    public UnmuteCommand() {
		this.name = MessageUtil.getMessage("command.unmute.name");
		this.help = MessageUtil.getMessage("command.unmute.help");
		this.arguments = MessageUtil.getMessage("command.unmute.arguments");
        this.category = Constants.MODERATION;
        this.userPermissions = new Permission[]{Permission.MANAGE_ROLES};
        this.botPermissions = new Permission[]{Permission.MANAGE_ROLES};
    }

    @Override
    protected void execute(final CommandEvent event) {
		if (GeneralUtil.isNotMod(event)) {
			MessageUtil.sendError(event, "error.not.mod");
			return;
		}
		if (event.getArgs().isEmpty()) {
			MessageUtil.sendError(event, "error.missing.args");
			return;
		}

		final String[] args = ArgsUtil.split(event.getArgs());
		final Role muteRole = GeneralUtil.getMuteRole(event.getGuild());
		final Member member = ArgsUtil.getMember(event, args[0]);

		if (muteRole == null) {
			MessageUtil.sendError(event, "error.role.mute.not.set");
			return;
		}
        if (member == null) {
			MessageUtil.sendError(event, "error.member.not.found");
            return;
        }
		if (GeneralUtil.isRoleHigher(member, event.getMember())) {
			MessageUtil.sendError(event, "error.role.position", "unmute");
			return;
		}
        if (!GeneralUtil.hasRole(member, muteRole)) {
			MessageUtil.sendError(event, "command.unmute.error.not.muted");
            return;
        }

        event.getGuild().removeRoleFromMember(member, muteRole).queue();
		MessageUtil.sendSuccessMessage(event, String.format("**%s** unmuted by moderator **%s**.",
			member.getUser().getAsTag(),
			event.getMember().getEffectiveName()));
    }
}
