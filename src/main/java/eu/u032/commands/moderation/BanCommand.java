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
import eu.u032.Utils;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;

public class BanCommand extends Command {
    public BanCommand() {
        this.name = "ban";
        this.help = "Ban member from server";
        this.arguments = "<@Member | ID> [reason]";
        this.category = new Category("Moderation");
        this.userPermissions = new Permission[]{Permission.BAN_MEMBERS};
        this.botPermissions = new Permission[]{Permission.BAN_MEMBERS};
    }

    @Override
    protected void execute(final CommandEvent event) {
        final String[] args = Utils.splitArgs(event.getArgs());

		final String memberId = Utils.getId(args[0], Utils.MEMBER);
        final Member member = memberId.isEmpty() ? null : event.getGuild().getMemberById(memberId);

		final String reason = Utils.getGluedArg(args, 1);

        if (args[0].isEmpty()) {
			Utils.sendError(event, "Required arguments are missing!");
            return;
        }
        if (member == null) {
			Utils.sendError(event, "Member not found.");
            return;
        }
		if (member == event.getSelfMember()) {
			Utils.sendError(event, "You cannot ban me.");
			return;
		}
		if (member == event.getMember()) {
			Utils.sendError(event, "You cannot ban yourself.");
			return;
		}
		if (event.getMember().getRoles().get(0).getPosition() <= member.getRoles().get(0).getPosition()) {
			Utils.sendError(event, "I cannot ban a member with a role equal to or higher than yours.");
			return;
		}

        try {
			event.getGuild().ban(member, 0, reason).queue();
			Utils.sendSuccess(event, String.format("**%s** banned by moderator **%s**%s",
				member.getUser().getAsTag(), event.getMember().getEffectiveName(),
				reason.isEmpty() ? "." : " with reason: " + reason));
        } catch (final Exception e) {
            Utils.sendError(event, e.getMessage());
        }
    }
}
