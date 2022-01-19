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

package eu.u032.util;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.utils.FinderUtil;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;

import java.util.List;

public class CheckUtil {
	/** Returns true if the member has the specified role. */
    public static boolean hasRole(Member member, Role role) {
        for (Role memberRole : member.getRoles()) {
            if (memberRole == role) return true;
        }
        return false;
    }

	/** Return true if member is not moderator. */
	public static boolean isNotMod(CommandEvent event, Member member) {
		if (member.hasPermission(Permission.ADMINISTRATOR)) return false;
		Role modRole = SettingsUtil.getModRole(member.getGuild());
		if (modRole == null) {
			MessageUtil.sendError(event, "error.role.mod.not.set");
			return true;
		}
		return !hasRole(member, modRole);
	}

	/** Return true if member found in banned list. */
	public static boolean isBanned(String user, Guild guild) {
		List<User> users = FinderUtil.findBannedUsers(user, guild);
		return !users.isEmpty();
	}
}
