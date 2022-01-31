/*
 * This file is part of Module.
 *
 * Module is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Module is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Module. If not, see <https://www.gnu.org/licenses/>.
 */

package org.module.commands.moderation;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import org.module.Constants;
import org.module.model.Warn;
import org.module.service.ModerationService;
import org.module.service.impl.ModerationServiceImpl;
import org.module.util.ArgsUtil;
import org.module.util.MessageUtil;
import org.module.util.PropertyUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;

import java.util.List;

public class WarnsCommand extends Command {
	private final ModerationService moderationService = new ModerationServiceImpl();

	public WarnsCommand() {
		this.name = PropertyUtil.getProperty("command.warns.name");
		this.help = PropertyUtil.getProperty("command.warns.help");
		this.arguments = PropertyUtil.getProperty("command.warns.arguments");
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

		List<Warn> warns = moderationService.getWarns(member);

		StringBuilder warnsMessage = new StringBuilder("Warns count: " + warns.size() + "\n");
		for (Warn warn : warns) {
			warnsMessage.append("ID: `").append(warn.getId()).append("`").append(" ")
				.append(member.getAsMention())
				.append(warn.getReason().isEmpty() ? "" : ": ")
				.append(warn.getReason()).append("\n");
		}

		EmbedBuilder embed = new EmbedBuilder()
			.setAuthor(member.getUser().getAsTag(), null, member.getEffectiveAvatarUrl())
			.setColor(Constants.DEFAULT)
			.setDescription(warnsMessage.isEmpty() ? "" : warnsMessage)
			.setFooter("ID: " + member.getId());
		event.reply(embed.build());
	}
}
