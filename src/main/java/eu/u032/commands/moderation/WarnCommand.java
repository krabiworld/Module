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
import eu.u032.Constants;
import eu.u032.model.Warn;
import eu.u032.service.WarnService;
import eu.u032.util.ArgsUtil;
import eu.u032.util.CheckUtil;
import eu.u032.util.MessageUtil;
import net.dv8tion.jda.api.entities.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class WarnCommand extends Command {
	@Autowired
	private WarnService warnService;

	public WarnCommand() {
		this.name = MessageUtil.getMessage("command.warn.name");
		this.help = MessageUtil.getMessage("command.warn.help");
		this.arguments = MessageUtil.getMessage("command.warn.arguments");
		this.category = Constants.MODERATION;
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
		Member member = ArgsUtil.getMember(event, args[0]);
		String reason = ArgsUtil.getGluedArg(args, 1);

		if (member == null || member.getUser().isBot()) {
			MessageUtil.sendHelp(event, this);
			return;
		}
		if (!event.getSelfMember().canInteract(member)) {
			MessageUtil.sendError(event, "error.role.position", "warn");
			return;
		}
		if (member == event.getMember()) {
			MessageUtil.sendError(event, "command.ban.error.cannot.yourself", "warn");
			return;
		}

		Warn warn = new Warn();
		warn.setGuild(event.getGuild().getIdLong());
		warn.setUser(member.getIdLong());
		warn.setReason(reason);

		warnService.save(warn);

		MessageUtil.sendSuccessMessage(event, String.format("**%s** warned (ID: `#%s`) by moderator **%s**%s",
			member.getUser().getAsTag(),
			warn.getId(),
			Objects.requireNonNull(event.getMember()).getEffectiveName(),
			reason.isEmpty() ? "." : " with reason: " + reason));
	}
}
