/*
 * Module Discord Bot.
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

package org.module.events;

import org.module.Constants;
import org.module.service.MessageService;
import org.module.util.PropertyUtil;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MemberEvents extends ListenerAdapter {
	@Autowired
	private MessageService messageService;

	@Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        Member member = event.getMember();

        EmbedBuilder embed = new EmbedBuilder()
			.setAuthor(member.getUser().getAsTag(), null, member.getEffectiveAvatarUrl())
			.setColor(Constants.COLOR_GREEN)
			.setDescription(member.getAsMention() + " joined to server!")
			.addField("Registered at",
				String.format("<t:%s>", member.getTimeCreated().toEpochSecond()), true)
			.addField("Member count",
				String.valueOf(member.getGuild().getMemberCount()), true)
			.setFooter("ID: " + member.getId());
		messageService.sendLog(event.getGuild(), embed);
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

    @Override
    public void onGuildMemberRoleAdd(GuildMemberRoleAddEvent event) {
        List<Role> roles = event.getRoles();
        StringBuilder addedRoles = new StringBuilder();

        for (Role role : roles) {
            addedRoles.append("`").append(role.getName()).append("` ");
        }

        EmbedBuilder embed = new EmbedBuilder()
			.setTitle("Added role(s) for " + event.getUser().getName())
			.setColor(Constants.COLOR_GREEN)
			.setDescription(addedRoles.toString())
			.setFooter("ID: " + event.getUser().getId());
		messageService.sendLog(event.getGuild(), embed);
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
			.setColor(Constants.COLOR_RED)
			.setDescription(removedRoles.toString())
			.setFooter("ID: " + event.getUser().getId());
		messageService.sendLog(event.getGuild(), embed);
    }

	@Override
	public void onGuildBan(GuildBanEvent event) {
		User user = event.getUser();

		EmbedBuilder embed = new EmbedBuilder()
			.setAuthor(user.getAsTag() + " was banned", null, user.getEffectiveAvatarUrl())
			.setColor(Constants.COLOR_RED)
			.setFooter("ID: " + user.getId());
		messageService.sendLog(event.getGuild(), embed);
	}

	@Override
	public void onGuildUnban(GuildUnbanEvent event) {
		User user = event.getUser();

		EmbedBuilder embed = new EmbedBuilder()
			.setAuthor(user.getAsTag() + " was unbanned", null, user.getEffectiveAvatarUrl())
			.setColor(Constants.COLOR_GREEN)
			.setFooter("ID: " + user.getId());
		messageService.sendLog(event.getGuild(), embed);
	}
}
