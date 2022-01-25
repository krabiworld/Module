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
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateNicknameEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.module.constants.Constants;
import org.module.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MemberUpdateNickname extends ListenerAdapter {
	private final MessageService messageService;

	@Autowired
	public MemberUpdateNickname(MessageService messageService) {
		this.messageService = messageService;
	}

	@Override
	public void onGuildMemberUpdateNickname(GuildMemberUpdateNicknameEvent event) {
		Member member = event.getMember();
		String before = event.getOldNickname();
		String after = event.getNewNickname();
		String action = " was updated";

		if (before == null) return;
		if (after == null) action = " was reset";

		EmbedBuilder embed = new EmbedBuilder()
			.setAuthor("Nickname for " + member.getUser().getAsTag() + action,
				null, member.getEffectiveAvatarUrl())
			.setColor(Constants.COLOR_YELLOW)
			.addField("Before", before, true)
			.setFooter("ID: " + member.getId());

		if (after != null) embed.addField("After", after, true);

		messageService.sendLog(event.getGuild(), embed);
	}
}
