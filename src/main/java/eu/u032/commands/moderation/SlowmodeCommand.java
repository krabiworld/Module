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
import eu.u032.utils.GeneralUtil;
import eu.u032.utils.MsgUtil;
import net.dv8tion.jda.api.Permission;

public class SlowmodeCommand extends Command {
    public SlowmodeCommand() {
        this.name = "slowmode";
        this.help = "Set slowmode in current channel";
        this.arguments = "<duration>";
        this.category = Constants.MODERATION;
        this.userPermissions = new Permission[]{Permission.MANAGE_CHANNEL};
        this.botPermissions = new Permission[]{Permission.MANAGE_CHANNEL};
    }

    @Override
    protected void execute(final CommandEvent event) {
		if (GeneralUtil.isNotMod(event)) {
			return;
		}
		if (event.getArgs().isEmpty()) {
			MsgUtil.sendError(event, Constants.MISSING_ARGS);
            return;
        }

        final int interval = Integer.parseInt(event.getArgs());

        if (interval < 0 || interval > 21600) {
			MsgUtil.sendError(event, "Specify in seconds from 0 (off) to 21600.");
            return;
        }
        if (event.getTextChannel().getSlowmode() == interval) {
			MsgUtil.sendError(event, "This value already set.");
            return;
        }

        event.getTextChannel().getManager().setSlowmode(interval).queue();
		MsgUtil.sendSuccess(event, String.format("Slowmode for channel %s changed to **%s**.",
			event.getTextChannel().getAsMention(),
			interval));
    }
}
