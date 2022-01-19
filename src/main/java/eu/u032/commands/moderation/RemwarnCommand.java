/*
 * Module Discord Bot.
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
import eu.u032.model.Warn;
import eu.u032.service.WarnService;
import eu.u032.util.CheckUtil;
import eu.u032.util.MessageUtil;
import net.dv8tion.jda.api.entities.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static eu.u032.Constants.*;

@Component
public class RemwarnCommand extends Command {
	@Autowired
	private WarnService warnService;

	public RemwarnCommand() {
		this.name = MessageUtil.getMessage("command.remwarn.name");
		this.help = MessageUtil.getMessage("command.remwarn.help");
		this.arguments = MessageUtil.getMessage("command.remwarn.arguments");
		this.category = MODERATION;
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

		Warn warn = warnService.findById(Long.parseLong(event.getArgs()));

		if (warn == null || warn.getGuild() != event.getGuild().getIdLong()) {
			MessageUtil.sendError(event, "command.warn.error.not.found");
			return;
		}

		Member member = event.getGuild().getMemberById(warn.getUser());

		if (member == null) {
			MessageUtil.sendHelp(event, this);
			return;
		}

		warnService.delete(warn);

		MessageUtil.sendSuccessMessage(event, String.format("Moderator **%s** removed warning (ID `%s`) to **%s**",
			event.getMember().getEffectiveName(),
			warn.getId(),
			member.getUser().getAsTag()));
	}
}
