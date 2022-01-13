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
import eu.u032.utils.ArgsUtil;
import eu.u032.utils.MsgUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

import static eu.u032.Constants.*;
import static eu.u032.utils.SessionFactoryUtil.getSessionFactory;

public class WarnsCommand extends Command {
	public WarnsCommand() {
		this.name = "warns";
		this.help = "Warns member";
		this.arguments = "[@Member | ID]";
		this.category = MODERATION;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void execute(final CommandEvent event) {
		Member member = event.getMember();

		if (!event.getArgs().isEmpty()) {
			member = ArgsUtil.getMember(event, event.getArgs());
		}
		if (member == null) {
			MsgUtil.sendError(event, MEMBER_NOT_FOUND);
			return;
		}

		final Session session = getSessionFactory().openSession();

		final Query<WarnModel> query = session.createQuery("from WarnModel where user_id = :id")
			.setParameter("id", member.getIdLong());
		final List<WarnModel> warnModels = query.getResultList();

		session.close();

		final StringBuilder warnsMessage = new StringBuilder("Warns count: " + warnModels.size() + "\n");
		for (WarnModel warnModel : warnModels) {
			warnsMessage.append("ID `").append(warnModel.getId()).append("`").append(" ")
				.append(member.getAsMention())
				.append(warnModel.getReason().isEmpty() ? "" : ": ")
				.append(warnModel.getReason()).append("\n");
		}

		EmbedBuilder embed = new EmbedBuilder()
			.setAuthor(member.getUser().getAsTag(), null, member.getEffectiveAvatarUrl())
			.setColor(COLOR)
			.setDescription(warnsMessage.isEmpty() ? "" : warnsMessage)
			.setFooter("ID: " + member.getId());
		event.reply(embed.build());
	}
}
