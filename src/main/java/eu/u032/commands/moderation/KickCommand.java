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

public class KickCommand extends Command {
    public KickCommand() {
        this.name = "kick";
        this.help = "Kick member from server";
        this.arguments = "<@Member | ID> [reason]";
        this.category = new Category("Moderation");
        this.userPermissions = new Permission[]{Permission.KICK_MEMBERS};
        this.botPermissions = new Permission[]{Permission.KICK_MEMBERS};
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

        event.getGuild().kick(member, reason).queue();
        Utils.sendSuccess(event, String.format("**%s** kicked by moderator **%s**%s",
			member.getUser().getAsTag(), event.getMember().getEffectiveName(),
			reason.isEmpty() ? "." : " with reason: " + reason));
    }
}
