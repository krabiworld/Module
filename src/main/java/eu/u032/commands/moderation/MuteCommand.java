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
import eu.u032.util.ArgsUtil;
import eu.u032.util.CheckUtil;
import eu.u032.util.SettingsUtil;
import eu.u032.util.MessageUtil;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

import static eu.u032.Constants.*;

public class MuteCommand extends Command {
    public MuteCommand() {
        this.name = MessageUtil.getMessage("command.mute.name");
        this.help = MessageUtil.getMessage("command.mute.help");
        this.arguments = MessageUtil.getMessage("command.mute.arguments");
        this.category = MODERATION;
		this.userPermissions = new Permission[]{Permission.MANAGE_ROLES};
        this.botPermissions = new Permission[]{Permission.MANAGE_ROLES};
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

		String[] args = ArgsUtil.split(event.getArgs());
		Role muteRole = SettingsUtil.getMuteRole(event.getGuild());
		Member member = ArgsUtil.getMember(event, args[0]);

		if (muteRole == null) {
			MessageUtil.sendError(event, "command.mute.error.role.not.set");
			return;
		}
        if (member == null) {
			MessageUtil.sendHelp(event, this);
            return;
        }
		if (!event.getSelfMember().canInteract(member)) {
			MessageUtil.sendError(event, "error.role.position", "mute");
			return;
		}
		if (member == event.getMember()) {
			MessageUtil.sendError(event, "command.ban.error.cannot.yourself", "mute");
			return;
		}
        if (CheckUtil.hasRole(member, muteRole)) {
			MessageUtil.sendError(event, "command.mute.error.already.muted");
            return;
        }

        event.getGuild().addRoleToMember(member, muteRole).queue();
        MessageUtil.sendSuccessMessage(event, String.format("**%s** muted by moderator **%s**.",
			member.getUser().getAsTag(),
			event.getMember().getEffectiveName()));
    }
}
