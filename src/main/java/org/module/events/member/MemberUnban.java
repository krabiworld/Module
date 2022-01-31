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

import org.module.Constants;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.GuildUnbanEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.module.util.MessageUtil;

public class MemberUnban extends ListenerAdapter {
	@Override
	public void onGuildUnban(GuildUnbanEvent event) {
		User user = event.getUser();

		EmbedBuilder embed = new EmbedBuilder()
			.setAuthor(user.getAsTag() + " was unbanned", null, user.getEffectiveAvatarUrl())
			.setColor(Constants.SUCCESS)
			.setFooter("ID: " + user.getId());
		MessageUtil.sendLog(event.getGuild(), embed);
	}
}
