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

package eu.u032.logging;

import eu.u032.Constants;
import eu.u032.utils.MsgUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.GuildBanEvent;
import net.dv8tion.jda.api.events.guild.GuildUnbanEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateNicknameEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.List;

import static eu.u032.Constants.BOTS;

public class MemberEvents extends ListenerAdapter {
	@Override
    public void onGuildMemberJoin(final GuildMemberJoinEvent event) {
        final Member member = event.getMember();

        final EmbedBuilder embed = new EmbedBuilder()
			.setAuthor(member.getUser().getAsTag(), null, member.getEffectiveAvatarUrl())
			.setColor(Constants.COLOR_GREEN)
			.setDescription(String.format("%s%s joined to server!",
				member.getAsMention(), member.getUser().isBot() ? " " + BOTS : ""))
			.addField("Registered at",
				String.format("<t:%s>", member.getTimeCreated().toEpochSecond()), true)
			.addField("Member count",
				String.valueOf(member.getGuild().getMemberCount()), true)
			.setFooter("ID: " + member.getId());
        MsgUtil.sendLog(event.getGuild(), embed);
    }

    @Override
    public void onGuildMemberRemove(final GuildMemberRemoveEvent event) {
        final Member member = event.getMember();

        if (member == null) return;

        final EmbedBuilder embed = new EmbedBuilder()
			.setAuthor(member.getUser().getAsTag(), null, member.getEffectiveAvatarUrl())
			.setColor(Constants.COLOR_RED)
			.setDescription(String.format("%s%s has left the server!",
				member.getAsMention(), member.getUser().isBot() ? " " + BOTS : ""))
			.addField("Joined at",
				String.format("<t:%s>", member.getTimeJoined().toEpochSecond()), true)
			.addField("Registered at",
				String.format("<t:%s>", member.getTimeCreated().toEpochSecond()), true)
			.addField("Member count",
				String.valueOf(event.getGuild().getMemberCount()), true)
			.setFooter("ID: " + member.getId());
        MsgUtil.sendLog(event.getGuild(), embed);
    }

    @Override
    public void onGuildMemberUpdateNickname(final GuildMemberUpdateNicknameEvent event) {
        final Member member = event.getMember();
        final String before = event.getOldNickname();
        final String after = event.getNewNickname();
        String action = " was updated";

        if (before == null) return;
        if (after == null) action = " was reset";

        final EmbedBuilder embed = new EmbedBuilder()
			.setAuthor("Nickname for " + member.getUser().getAsTag() + action,
				null, member.getEffectiveAvatarUrl())
			.setColor(Constants.COLOR_YELLOW)
			.addField("Before", before, true)
			.setFooter("ID: " + member.getId());

        if (after != null) embed.addField("After", after, true);

        MsgUtil.sendLog(event.getGuild(), embed);
    }

    @Override
    public void onGuildMemberRoleAdd(final GuildMemberRoleAddEvent event) {
        final List<Role> roles = event.getRoles();
        final StringBuilder addedRoles = new StringBuilder();

        for (final Role role : roles) {
            addedRoles.append(role.getAsMention()).append(" ");
        }

        final EmbedBuilder embed = new EmbedBuilder()
			.setTitle("Added role(s) for " + event.getUser().getName())
			.setColor(Constants.COLOR_GREEN)
			.setDescription(addedRoles.toString())
			.setFooter("ID: " + event.getUser().getId());
        MsgUtil.sendLog(event.getGuild(), embed);
    }

    @Override
    public void onGuildMemberRoleRemove(final GuildMemberRoleRemoveEvent event) {
        final List<Role> roles = event.getRoles();
        final StringBuilder removedRoles = new StringBuilder();

        for (final Role role : roles) {
            removedRoles.append(role.getAsMention()).append(" ");
        }

        final EmbedBuilder embed = new EmbedBuilder()
			.setTitle("Removed role(s) for " + event.getUser().getName())
			.setColor(Constants.COLOR_RED)
			.setDescription(removedRoles.toString())
			.setFooter("ID: " + event.getUser().getId());
        MsgUtil.sendLog(event.getGuild(), embed);
    }

	@Override
	public void onGuildBan(final GuildBanEvent event) {
		final User user = event.getUser();

		final EmbedBuilder embed = new EmbedBuilder()
			.setAuthor(user.getAsTag() + " was banned", null, user.getEffectiveAvatarUrl())
			.setColor(Constants.COLOR_RED)
			.setFooter("ID: " + user.getId());
		MsgUtil.sendLog(event.getGuild(), embed);
	}

	@Override
	public void onGuildUnban(final GuildUnbanEvent event) {
		User user = event.getUser();

		final EmbedBuilder embed = new EmbedBuilder()
			.setAuthor(user.getAsTag() + " was unbanned", null, user.getEffectiveAvatarUrl())
			.setColor(Constants.COLOR_GREEN)
			.setFooter("ID: " + user.getId());
		MsgUtil.sendLog(event.getGuild(), embed);
	}
}
