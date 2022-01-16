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

import java.util.Objects;

public class KickCommand extends Command {
    public KickCommand() {
        this.name = MessageUtil.getMessage("command.kick.name");
        this.help = MessageUtil.getMessage("command.kick.help");
        this.arguments = MessageUtil.getMessage("command.kick.arguments");
        this.category = Constants.MODERATION;
        this.userPermissions = new Permission[]{Permission.KICK_MEMBERS};
        this.botPermissions = new Permission[]{Permission.KICK_MEMBERS};
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
		final Member member = ArgsUtil.getMember(event, args[0]);
		final String reason = ArgsUtil.getGluedArg(args, 1);

        if (member == null) {
			MessageUtil.sendError(event, "error.member.not.found");
            return;
        }
		if (member == event.getSelfMember()) {
			MessageUtil.sendError(event, "error.cannot.me", "kick");
			return;
		}
		if (member == event.getMember()) {
			MessageUtil.sendError(event, "error.cannot.yourself", "kick");
			return;
		}
		if (GeneralUtil.isRoleHigher(member, event.getMember())) {
			MessageUtil.sendError(event, "error.role.position", "kick");
			return;
		}

        event.getGuild().kick(member, reason).queue();
        MessageUtil.sendSuccessMessage(event, String.format("**%s** kicked by moderator **%s**%s",
			member.getUser().getAsTag(),
			Objects.requireNonNull(event.getMember()).getEffectiveName(),
			reason.isEmpty() ? "." : " with reason: " + reason));
    }
}
