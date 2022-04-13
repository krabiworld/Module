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

package org.module.listeners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.GuildBanEvent;
import net.dv8tion.jda.api.events.guild.GuildUnbanEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateNicknameEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.module.Constants;
import org.module.util.LogsUtil;

import java.util.List;

public class MemberListener extends ListenerAdapter {
	@Override
	public void onGuildBan(GuildBanEvent event) {
		User user = event.getUser();

		EmbedBuilder embed = new EmbedBuilder()
			.setAuthor(user.getAsTag() + " was banned", null, user.getEffectiveAvatarUrl())
			.setColor(Constants.ERROR)
			.setFooter("ID: " + user.getId());
		LogsUtil.sendLog(event.getGuild(), embed);
	}
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
		LogsUtil.sendLog(event.getGuild(), embed);
	}

	@Override
	public void onGuildMemberRemove(GuildMemberRemoveEvent event) {
		Member member = event.getMember();

		if (member == null) return;

		EmbedBuilder embed = new EmbedBuilder()
			.setAuthor(member.getUser().getAsTag(), null, member.getEffectiveAvatarUrl())
			.setColor(Constants.ERROR)
			.setDescription(member.getAsMention() + " has left the server!")
			.addField("Joined at",
				String.format("<t:%s>", member.getTimeJoined().toEpochSecond()), true)
			.addField(getRegisteredAtField(event.getMember()))
			.addField(getMemberCount(event.getGuild()))
			.setFooter("ID: " + member.getId());
		LogsUtil.sendLog(event.getGuild(), embed);
	}
	@Override
	public void onGuildMemberRoleAdd(GuildMemberRoleAddEvent event) {
		List<Role> roles = event.getRoles();
		StringBuilder addedRoles = new StringBuilder();

		for (Role role : roles) {
			addedRoles.append("`").append(role.getName()).append("` ");
		}

		EmbedBuilder embed = new EmbedBuilder()
			.setTitle("Added role(s) for " + event.getUser().getName())
			.setColor(Constants.SUCCESS)
			.setDescription(addedRoles.toString())
			.setFooter("ID: " + event.getUser().getId());
		LogsUtil.sendLog(event.getGuild(), embed);
	}
	@Override
	public void onGuildMemberRoleRemove(GuildMemberRoleRemoveEvent event) {
		List<Role> roles = event.getRoles();
		StringBuilder removedRoles = new StringBuilder();

		for (Role role : roles) {
			removedRoles.append("`").append(role.getName()).append("` ");
		}

		EmbedBuilder embed = new EmbedBuilder()
			.setTitle("Removed role(s) for " + event.getUser().getName())
			.setColor(Constants.ERROR)
			.setDescription(removedRoles.toString())
			.setFooter("ID: " + event.getUser().getId());
		LogsUtil.sendLog(event.getGuild(), embed);
	}
	@Override
	public void onGuildUnban(GuildUnbanEvent event) {
		User user = event.getUser();

		EmbedBuilder embed = new EmbedBuilder()
			.setAuthor(user.getAsTag() + " was unbanned", null, user.getEffectiveAvatarUrl())
			.setColor(Constants.SUCCESS)
			.setFooter("ID: " + user.getId());
		LogsUtil.sendLog(event.getGuild(), embed);
	}
	@Override
	public void onGuildMemberUpdateNickname(GuildMemberUpdateNicknameEvent event) {
		Member member = event.getMember();
		if (member.getUser().isBot()) return;
		String before = event.getOldNickname();
		String after = event.getNewNickname();
		String action = " was updated";

		if (before == null) return;
		if (after == null) action = " was reset";

		EmbedBuilder embed = new EmbedBuilder()
			.setAuthor("Nickname for " + member.getUser().getAsTag() + action,
				null, member.getEffectiveAvatarUrl())
			.setColor(Constants.WARN)
			.addField("Before", before, true)
			.setFooter("ID: " + member.getId());

		if (after != null) embed.addField("After", after, true);

		LogsUtil.sendLog(event.getGuild(), embed);
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
