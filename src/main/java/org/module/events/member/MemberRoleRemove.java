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
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.module.constants.Constants;
import org.module.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MemberRoleRemove extends ListenerAdapter {
	private final MessageService messageService;

	@Autowired
	public MemberRoleRemove(MessageService messageService) {
		this.messageService = messageService;
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
}
