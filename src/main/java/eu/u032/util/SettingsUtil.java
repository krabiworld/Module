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

import eu.u032.manager.GuildManager;
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
		return settings.getLogs() == 0 ? null : guild.getRoleById(settings.getMute());
	}

	/** Get Moderator {@link Role role} from {@link GuildManager.GuildSettings} */
	public static Role getModRole(Guild guild) {
		GuildManager.GuildSettings settings = getSettings(guild);
		return settings.getMod() == 0 ? null : guild.getRoleById(settings.getMod());
	}

	/** Get Logs {@link TextChannel channel} from {@link GuildManager.GuildSettings} */
	public static TextChannel getLogsChannel(Guild guild) {
		GuildManager.GuildSettings settings = getSettings(guild);
		return settings.getLogs() == 0 ? null : guild.getTextChannelById(settings.getLogs());
	}

	/** Get Prefix from {@link GuildManager.GuildSettings} */
	public static String getPrefix(Guild guild) {
		return getSettings(guild).getPrefix();
	}
}
