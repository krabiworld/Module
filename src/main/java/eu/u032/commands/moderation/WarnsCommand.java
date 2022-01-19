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
import eu.u032.model.Warn;
import eu.u032.service.WarnService;
import eu.u032.util.ArgsUtil;
import eu.u032.util.MessageUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WarnsCommand extends Command {
	@Autowired
	private WarnService warnService;

	public WarnsCommand() {
		this.name = MessageUtil.getMessage("command.warns.name");
		this.help = MessageUtil.getMessage("command.warns.help");
		this.arguments = MessageUtil.getMessage("command.warns.arguments");
		this.category = Constants.MODERATION;
	}

	@Override
	protected void execute(CommandEvent event) {
		Member member = event.getMember();

		if (!event.getArgs().isEmpty()) {
			member = ArgsUtil.getMember(event, event.getArgs());
		}
		if (member == null || member.getUser().isBot()) {
			MessageUtil.sendHelp(event, this);
			return;
		}

		List<Warn> warns = warnService
			.findAllByGuildAndUser(event.getGuild().getIdLong(), member.getIdLong());

		StringBuilder warnsMessage = new StringBuilder("Warns count: " + warns.size() + "\n");
		for (Warn warn : warns) {
			warnsMessage.append("ID `").append(warn.getId()).append("`").append(" ")
				.append(member.getAsMention())
				.append(warn.getReason().isEmpty() ? "" : ": ")
				.append(warn.getReason()).append("\n");
		}

		EmbedBuilder embed = new EmbedBuilder()
			.setAuthor(member.getUser().getAsTag(), null, member.getEffectiveAvatarUrl())
			.setColor(Constants.COLOR)
			.setDescription(warnsMessage.isEmpty() ? "" : warnsMessage)
			.setFooter("ID: " + member.getId());
		event.reply(embed.build());
	}
}
