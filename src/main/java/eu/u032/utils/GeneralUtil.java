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

package eu.u032.utils;

import com.jagrosh.jdautilities.command.CommandEvent;
import eu.u032.Constants;
import eu.u032.GuildManager;
import net.dv8tion.jda.api.entities.*;

import java.util.Objects;

public class GeneralUtil {
	private static final GuildManager manager = new GuildManager();

	/** Check if member has a role. */
    public static boolean hasRole(final Member member, final Role role) {
        for (final Role memberRole : member.getRoles()) {
            if (memberRole == role) return true;
        }
        return false;
    }

	/** Returns true if the member role is higher than or equal to the target role. */
	public static boolean checkRolePosition(final Member member, final Member target) {
		if (target.getRoles().size() == 0 || member.getRoles().size() == 0) return false;
		return member.getRoles().get(0).getPosition() >= target.getRoles().get(0).getPosition();
	}

	/** Get Mute role from {@link GuildManager.GuildSettings} */
	public static Role getMuteRole(final Guild guild) {
		final GuildManager.GuildSettings settings = manager.getSettings(guild);
		if (Objects.requireNonNull(settings).getLogs() == 0) return null;
		return guild.getRoleById(settings.getMute());
	}

	/** Get Moderator role from {@link GuildManager.GuildSettings} */
	public static Role getModRole(final Guild guild) {
		final GuildManager.GuildSettings settings = manager.getSettings(guild);
		if (Objects.requireNonNull(settings).getMod() == 0) return null;
		return guild.getRoleById(settings.getMod());
	}

	/** Get Logs channel from {@link GuildManager.GuildSettings} */
	public static TextChannel getLogsChannel(final Guild guild) {
		final GuildManager.GuildSettings settings = manager.getSettings(guild);
		if (Objects.requireNonNull(settings).getLogs() == 0) return null;
		return guild.getTextChannelById(settings.getLogs());
	}

	/** Get Prefix from {@link GuildManager.GuildSettings} */
	public static String getPrefix(final Guild guild) {
		final GuildManager.GuildSettings settings = manager.getSettings(guild);
		if (Objects.requireNonNull(settings).getPrefix().isEmpty()) return null;
		return settings.getPrefix();
	}

	/** Return true if member is not moderator. */
	public static boolean isNotMod(final CommandEvent event) {
		if (GeneralUtil.hasRole(event.getMember(), GeneralUtil.getModRole(event.getGuild()))) {
			return false;
		}
		MsgUtil.sendError(event, Constants.NOT_MOD);
		return true;
	}
}
