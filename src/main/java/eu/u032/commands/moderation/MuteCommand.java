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
import eu.u032.Config;
import eu.u032.Utils;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

public class MuteCommand extends Command {
    public MuteCommand() {
        this.name = "mute";
        this.help = "Mute member on whole server";
        this.arguments = "<@Member | ID>";
        this.category = new Category("Moderation");
        this.userPermissions = new Permission[]{Permission.MANAGE_ROLES};
        this.botPermissions = new Permission[]{Permission.MANAGE_ROLES};
    }

    @Override
    protected void execute(final CommandEvent event) {
        final String[] args = Utils.splitArgs(event.getArgs());

        final String muteId = Config.getString("MUTE_ROLE");
        final Role muteRole = muteId.isEmpty() ? null : event.getGuild().getRoleById(muteId);

        final String memberId = Utils.getId(args[0], Utils.MEMBER);
        final Member member = memberId.isEmpty() ? null : event.getGuild().getMemberById(memberId);

        if (muteRole == null) {
			Utils.sendError(event, "Mute role is not set.");
            return;
        }
        if (args[0].isEmpty()) {
			Utils.sendError(event, "Required arguments are missing!");
            return;
        }
        if (member == null) {
			Utils.sendError(event, "Member not found.");
            return;
        }
        if (Utils.hasRole(member, muteRole)) {
			Utils.sendError(event, "This member already muted.");
            return;
        }

        event.getGuild().addRoleToMember(member, muteRole).queue();
        Utils.sendSuccess(event, String.format("**%s** muted by moderator **%s**.",
			member.getUser().getAsTag(), event.getMember().getEffectiveName()));
    }
}
