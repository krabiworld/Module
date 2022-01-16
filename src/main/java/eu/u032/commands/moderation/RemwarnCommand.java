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
import eu.u032.model.WarnModel;
import eu.u032.service.WarnService;
import eu.u032.util.GeneralUtil;
import eu.u032.util.MessageUtil;
import net.dv8tion.jda.api.entities.Member;

import static eu.u032.Constants.*;

public class RemwarnCommand extends Command {
	public RemwarnCommand() {
		this.name = MessageUtil.getMessage("command.remwarn.name");
		this.help = MessageUtil.getMessage("command.remwarn.help");
		this.arguments = MessageUtil.getMessage("command.remwarn.arguments");
		this.category = MODERATION;
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

		final WarnService warnService = new WarnService();

		final WarnModel warnModel = warnService.findById(Long.parseLong(event.getArgs()));

		if (warnModel == null || warnModel.getGuild() != event.getGuild().getIdLong()) {
			MessageUtil.sendError(event, "command.warn.error.not.found");
			return;
		}

		final Member member = event.getGuild().getMemberById(warnModel.getUser());

		if (member == null) {
			MessageUtil.sendError(event, "error.member.not.found");
			return;
		}

		warnService.delete(warnModel);

		MessageUtil.sendSuccessMessage(event, String.format("Moderator **%s** removed warning (ID `%s`) to **%s**",
			event.getMember().getEffectiveName(),
			warnModel.getId(),
			member.getUser().getAsTag()));
	}
}
