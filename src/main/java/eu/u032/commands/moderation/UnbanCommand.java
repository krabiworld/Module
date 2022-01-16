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

public class UnbanCommand extends Command {
    public UnbanCommand() {
		this.name = MessageUtil.getMessage("command.unban.name");
		this.help = MessageUtil.getMessage("command.unban.help");
		this.arguments = MessageUtil.getMessage("command.unban.arguments");
        this.category = Constants.MODERATION;
        this.userPermissions = new Permission[]{Permission.BAN_MEMBERS};
        this.botPermissions = new Permission[]{Permission.BAN_MEMBERS};
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
		if (!GeneralUtil.isBanned(event.getArgs(), event.getGuild())) {
			MessageUtil.sendError(event, "command.unban.error.not.found");
			return;
		}

		final String[] args = ArgsUtil.split(event.getArgs());

        try {
            event.getGuild().unban(args[0]).queue();
			MessageUtil.sendSuccessMessage(event, String.format("Member with id **%s** unbanned by moderator **%s**.",
				args[0],
				event.getMember().getEffectiveName()));
        } catch (final Exception e) {
            MessageUtil.sendErrorMessage(event, e.getMessage());
        }
    }
}
