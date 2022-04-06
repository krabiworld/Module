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

package org.module.util;

import com.jagrosh.jdautilities.commons.utils.FinderUtil;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

public class CheckUtil {
	/** Returns true if the member has the specified role. */
    public static boolean hasRole(Member member, Role role) {
		return member.getRoles().stream().anyMatch(memberRole -> memberRole == role);
    }

	/** Return true if member found in banned list. */
	public static boolean isBanned(String user, Guild guild) {
		return !FinderUtil.findBannedUsers(user, guild).isEmpty();
	}
}
