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
import eu.u032.utils.ArgsUtil;
import eu.u032.utils.GeneralUtil;
import eu.u032.utils.MsgUtil;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;

import static eu.u032.Constants.*;

public class BanCommand extends Command {
    public BanCommand() {
        this.name = "ban";
        this.help = "Ban member from server";
        this.arguments = "<@Member | ID> [reason]";
        this.category = MODERATION;
        this.userPermissions = new Permission[]{Permission.BAN_MEMBERS};
        this.botPermissions = new Permission[]{Permission.BAN_MEMBERS};
    }

    @Override
    protected void execute(final CommandEvent event) {
		if (GeneralUtil.isNotMod(event)) {
			return;
		}
		if (event.getArgs().isEmpty()) {
			MsgUtil.sendError(event, MISSING_ARGS);
			return;
		}

		final String[] args = ArgsUtil.split(event.getArgs());
		final Member member = ArgsUtil.getMember(event, args[0]);
		final String reason = ArgsUtil.getGluedArg(args, 1);

        if (member == null) {
			MsgUtil.sendError(event, MEMBER_NOT_FOUND);
            return;
        }
		if (member == event.getSelfMember()) {
			MsgUtil.sendError(event, MsgUtil.getTemplate(CANNOT_ME, "ban"));
			return;
		}
		if (member == event.getMember()) {
			MsgUtil.sendError(event, MsgUtil.getTemplate(CANNOT_YOURSELF, "ban"));
			return;
		}
		if (GeneralUtil.checkRolePosition(member, event.getMember())) {
			MsgUtil.sendError(event, MsgUtil.getTemplate(ROLE_POSITION, "ban"));
			return;
		}

        try {
			event.getGuild().ban(member, 0, reason).queue();
			MsgUtil.sendSuccess(event, String.format("**%s** banned by moderator **%s**%s",
				member.getUser().getAsTag(),
				event.getMember().getEffectiveName(),
				reason.isEmpty() ? "." : " with reason: " + reason));
        } catch (final Exception e) {
            MsgUtil.sendError(event, e.getMessage());
        }
    }
}
