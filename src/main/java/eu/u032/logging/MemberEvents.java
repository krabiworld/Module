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

import eu.u032.Utils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateNicknameEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.List;

public class MemberEvents extends ListenerAdapter {
    @Override
    public void onGuildMemberJoin(final GuildMemberJoinEvent event) {
        final Member member = event.getMember();

        final EmbedBuilder embed = new EmbedBuilder()
			.setAuthor(member.getUser().getAsTag(), null, member.getEffectiveAvatarUrl())
			.setColor(Utils.getColorGreen())
			.setDescription(member.getAsMention() + " joined to server!")
			.addField("Registered at",
				String.format("<t:%s>", member.getTimeCreated().toEpochSecond()), true)
			.addField("Member count",
				String.valueOf(member.getGuild().getMemberCount()), true)
			.setFooter("ID: " + member.getId());
        Utils.sendLog(event.getGuild(), embed);
    }

    @Override
    public void onGuildMemberRemove(final GuildMemberRemoveEvent event) {
        final Member member = event.getMember();

        if (member == null) return;

        final EmbedBuilder embed = new EmbedBuilder()
			.setAuthor(member.getUser().getAsTag(), null, member.getEffectiveAvatarUrl())
			.setColor(Utils.getColorRed())
			.setDescription(member.getAsMention() + " has left the server!")
			.addField("Joined at",
				String.format("<t:%s>", member.getTimeJoined().toEpochSecond()), true)
			.addField("Registered at",
				String.format("<t:%s>", member.getTimeCreated().toEpochSecond()), true)
			.addField("Member count",
				String.valueOf(event.getGuild().getMemberCount()), true)
			.setFooter("ID: " + member.getId());
        Utils.sendLog(event.getGuild(), embed);
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
			.setColor(Utils.getColorYellow())
			.addField("Before", before, true)
			.setFooter("ID: " + member.getId());

        if (after != null) embed.addField("After", after, true);

        Utils.sendLog(event.getGuild(), embed);
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
			.setColor(Utils.getColorGreen())
			.setDescription(addedRoles.toString())
			.setFooter("ID: " + event.getUser().getId());
        Utils.sendLog(event.getGuild(), embed);
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
			.setColor(Utils.getColorRed())
			.setDescription(removedRoles.toString())
			.setFooter("ID: " + event.getUser().getId());
        Utils.sendLog(event.getGuild(), embed);
    }
}
