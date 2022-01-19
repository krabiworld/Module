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
import eu.u032.util.CheckUtil;
import eu.u032.util.MessageUtil;
import net.dv8tion.jda.api.Permission;

public class SlowmodeCommand extends Command {
    public SlowmodeCommand() {
		this.name = MessageUtil.getMessage("command.slowmode.name");
		this.help = MessageUtil.getMessage("command.slowmode.help");
		this.arguments = MessageUtil.getMessage("command.slowmode.arguments");
        this.category = Constants.MODERATION;
        this.userPermissions = new Permission[]{Permission.MANAGE_CHANNEL};
        this.botPermissions = new Permission[]{Permission.MANAGE_CHANNEL};
    }

    @Override
    protected void execute(CommandEvent event) {
		if (CheckUtil.isNotMod(null, event.getMember())) {
			return;
		}
		if (event.getArgs().isEmpty()) {
			MessageUtil.sendHelp(event, this);
            return;
        }

        int interval = Integer.parseInt(event.getArgs());

        if (interval < 0 || interval > 21600) {
			MessageUtil.sendHelp(event, this);
            return;
        }
        if (event.getTextChannel().getSlowmode() == interval) {
			MessageUtil.sendErrorMessage(event, "This value already set.");
            return;
        }

        event.getTextChannel().getManager().setSlowmode(interval).queue();
		MessageUtil.sendSuccessMessage(event, String.format("Slowmode for channel %s changed to **%s**.",
			event.getTextChannel().getAsMention(),
			interval));
    }
}
