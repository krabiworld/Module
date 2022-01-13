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
import eu.u032.models.WarnModel;
import eu.u032.utils.GeneralUtil;
import eu.u032.utils.MsgUtil;
import net.dv8tion.jda.api.entities.Member;
import org.hibernate.Session;

import static eu.u032.Constants.*;
import static eu.u032.utils.SessionFactoryUtil.getSessionFactory;

public class RemwarnCommand extends Command {
	public RemwarnCommand() {
		this.name = "remwarn";
		this.help = "Remove warn from member";
		this.arguments = "<case>";
		this.category = MODERATION;
	}

	@Override
	protected void execute(final CommandEvent event) {
		if (GeneralUtil.isNotMod(event)) {
			return;
		}
		if (event.getArgs().isEmpty()) {
			MsgUtil.sendError(event, MISSING_ARGS);
			return;
		}

		final Session session = getSessionFactory().openSession();

		final WarnModel warnModel = session.find(WarnModel.class, Long.parseLong(event.getArgs()));

		if (warnModel == null) {
			MsgUtil.sendError(event, "Warn not found.");
			return;
		}

		final Member member = event.getGuild().getMemberById(warnModel.getUser());

		if (member == null) {
			MsgUtil.sendError(event, MEMBER_NOT_FOUND);
			return;
		}

		session.delete(warnModel);
		session.flush();
		session.close();

		MsgUtil.sendSuccess(event, String.format("Moderator **%s** removed warning (ID `%s`) to **%s**",
			event.getMember().getEffectiveName(),
			warnModel.getId(),
			member.getUser().getAsTag()));
	}
}
