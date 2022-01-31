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

package org.module.events.member;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.module.Constants;
import org.module.util.MessageUtil;

public class MemberJoin extends ListenerAdapter {
	@Override
	public void onGuildMemberJoin(GuildMemberJoinEvent event) {
		Member member = event.getMember();

		EmbedBuilder embed = new EmbedBuilder()
			.setAuthor(member.getUser().getAsTag(), null, member.getEffectiveAvatarUrl())
			.setColor(Constants.SUCCESS)
			.setDescription(member.getAsMention() + " joined to server!")
			.addField(getRegisteredAtField(member))
			.addField(getMemberCount(member.getGuild()))
			.setFooter("ID: " + member.getId());
		MessageUtil.sendLog(event.getGuild(), embed);
	}

	private MessageEmbed.Field getRegisteredAtField(Member member) {
		long registeredAt = member.getTimeCreated().toEpochSecond();
		return new MessageEmbed.Field("Registered at",
			String.format("<t:%s:D> (<t:%s:R>)", registeredAt, registeredAt), true);
	}

	private MessageEmbed.Field getMemberCount(Guild guild) {
		return new MessageEmbed.Field("Member count", String.valueOf(guild.getMemberCount()), true);
	}
}
