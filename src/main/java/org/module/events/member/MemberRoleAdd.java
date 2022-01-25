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
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.module.constants.Constants;
import org.module.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MemberRoleAdd extends ListenerAdapter {
	private final MessageService messageService;

	@Autowired
	public MemberRoleAdd(MessageService messageService) {
		this.messageService = messageService;
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
}
