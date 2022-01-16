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

package eu.u032.util;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.utils.FinderUtil;
import eu.u032.GuildManager;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;

import java.util.List;

public class GeneralUtil {
	private static final GuildManager manager = new GuildManager();

	/** Check if member has a role. */
    public static boolean hasRole(final Member member, final Role role) {
        for (final Role memberRole : member.getRoles()) {
            if (memberRole == role) return true;
        }
        return false;
    }

	/** Returns true if the firstMember role is higher than or equal to secondMember role. */
	public static boolean isRoleHigher(final Member firstMember, final Member secondMember) {
		if (secondMember.getRoles().size() == 0 || firstMember.getRoles().size() == 0) return false;
		return firstMember.getRoles().get(0).getPosition() >= secondMember.getRoles().get(0).getPosition();
	}

	private static GuildManager.GuildSettings getSettings(Guild guild) {
		return manager.getSettings(guild);
	}

	/** Get Mute role from {@link GuildManager.GuildSettings} */
	public static Role getMuteRole(final Guild guild) {
		final GuildManager.GuildSettings settings = getSettings(guild);
		return settings.getLogs() == 0 ? null : guild.getRoleById(settings.getMute());
	}

	/** Get Moderator role from {@link GuildManager.GuildSettings} */
	public static Role getModRole(final Guild guild) {
		final GuildManager.GuildSettings settings = getSettings(guild);
		return settings.getMod() == 0 ? null : guild.getRoleById(settings.getMod());
	}

	/** Get Logs channel from {@link GuildManager.GuildSettings} */
	public static TextChannel getLogsChannel(final Guild guild) {
		final GuildManager.GuildSettings settings = getSettings(guild);
		return settings.getLogs() == 0 ? null : guild.getTextChannelById(settings.getLogs());
	}

	/** Get Prefix from {@link GuildManager.GuildSettings} */
	public static String getPrefix(final Guild guild) {
		final GuildManager.GuildSettings settings = getSettings(guild);
		return settings.getPrefix().isEmpty() ? null : settings.getPrefix();
	}

	/** Return true if member is not moderator. */
	public static boolean isNotMod(final CommandEvent event) {
		if (event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
			return false;
		}
		return !GeneralUtil.hasRole(event.getMember(), GeneralUtil.getModRole(event.getGuild()));
	}

	public static boolean isBanned(final String arg, final Guild guild) {
		List<User> users = FinderUtil.findBannedUsers(arg, guild);
		return !users.isEmpty();
	}
}
