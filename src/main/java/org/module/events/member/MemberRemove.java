/*
 * This file is part of Module.

 * Module is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * Module is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with Module. If not, see <https://www.gnu.org/licenses/>.
 */

package org.module.events.member;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.module.constants.Constants;
import org.module.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MemberRemove extends ListenerAdapter {
	private final MessageService messageService;

	@Autowired
	private MemberRemove(MessageService messageService) {
		this.messageService = messageService;
	}

	@Override
	public void onGuildMemberRemove(GuildMemberRemoveEvent event) {
		Member member = event.getMember();

		if (member == null) return;

		EmbedBuilder embed = new EmbedBuilder()
			.setAuthor(member.getUser().getAsTag(), null, member.getEffectiveAvatarUrl())
			.setColor(Constants.COLOR_RED)
			.setDescription(member.getAsMention() + " has left the server!")
			.addField("Joined at",
				String.format("<t:%s>", member.getTimeJoined().toEpochSecond()), true)
			.addField("Registered at",
				String.format("<t:%s>", member.getTimeCreated().toEpochSecond()), true)
			.addField("Member count",
				String.valueOf(event.getGuild().getMemberCount()), true)
			.setFooter("ID: " + member.getId());
		messageService.sendLog(event.getGuild(), embed);
	}
}
