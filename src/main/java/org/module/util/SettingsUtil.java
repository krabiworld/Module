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

package org.module.util;

import org.module.manager.GuildManager;
import net.dv8tion.jda.api.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SettingsUtil {
	private static GuildManager manager;

	@Autowired
	public SettingsUtil(GuildManager manager) {
		SettingsUtil.manager = manager;
	}

	private static GuildManager.GuildSettings getSettings(Guild guild) {
		return manager.getSettings(guild);
	}

	/** Get Mute {@link Role role} from {@link GuildManager.GuildSettings} */
	public static Role getMuteRole(Guild guild) {
		GuildManager.GuildSettings settings = getSettings(guild);
		return settings.getMuteRole() == 0 ? null : guild.getRoleById(settings.getMuteRole());
	}

	/** Get Moderator {@link Role role} from {@link GuildManager.GuildSettings} */
	public static Role getModRole(Guild guild) {
		GuildManager.GuildSettings settings = getSettings(guild);
		return settings.getModeratorRole() == 0 ? null : guild.getRoleById(settings.getModeratorRole());
	}

	/** Get Logs {@link TextChannel channel} from {@link GuildManager.GuildSettings} */
	public static TextChannel getLogsChannel(Guild guild) {
		GuildManager.GuildSettings settings = getSettings(guild);
		return settings.getLogsChannel() == 0 ? null : guild.getTextChannelById(settings.getLogsChannel());
	}

	/** Get Prefix from {@link GuildManager.GuildSettings} */
	public static String getPrefix(Guild guild) {
		return getSettings(guild).getPrefix();
	}
}
