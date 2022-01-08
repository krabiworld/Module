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

public class UnbanCommand extends Command {
    public UnbanCommand() {
        this.name = "unban";
        this.help = "Unban member from server";
        this.arguments = "<ID>";
        this.category = new Category("Moderation");
        this.userPermissions = new Permission[]{Permission.BAN_MEMBERS};
        this.botPermissions = new Permission[]{Permission.BAN_MEMBERS};
    }

    @Override
    protected void execute(final CommandEvent event) {
        final String[] args = Utils.splitArgs(event.getArgs());

        if (args[0].isEmpty()) {
			Utils.sendError(event, "Required arguments are missing!");
            return;
        }

        try {
            event.getGuild().unban(args[0]).queue();
			Utils.sendSuccess(event, String.format("Member with id **%s** unbanned by moderator **%s**.",
				args[0], event.getMember().getEffectiveName()));
        } catch (final Exception e) {
            event.replyError(e.getMessage());
        }
    }
}
