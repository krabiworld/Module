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

package org.module.manager;

import com.jagrosh.jdautilities.command.GuildSettingsManager;
import com.jagrosh.jdautilities.command.GuildSettingsProvider;
import org.module.model.GuildConfig;
import org.module.service.GuildService;
import lombok.Getter;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

@Service
public class GuildManager extends ListenerAdapter implements GuildSettingsManager<GuildManager.GuildSettings> {
	@Autowired
	private GuildService guildService;

	@Override
	public void onGuildJoin(GuildJoinEvent event) {
		if (guildService.findById(event.getGuild().getIdLong()) != null) return;

		GuildConfig guildConfig = new GuildConfig();
		guildConfig.setId(event.getGuild().getIdLong());

		guildService.save(guildConfig);
	}

	@Override
	public GuildSettings getSettings(Guild guild) {
		return new GuildSettings(guildService.findById(guild.getIdLong()));
	}

	public void setPrefix(Guild guild, String prefix) {
		GuildConfig guildConfig = guildService.findById(guild.getIdLong());
		guildConfig.setPrefix(prefix);

		guildService.update(guildConfig);
	}

	public void setLogs(Guild guild, long logsId) {
		GuildConfig guildConfig = guildService.findById(guild.getIdLong());
		guildConfig.setLogs(logsId);

		guildService.update(guildConfig);
	}

	public void setMute(Guild guild, long muteId) {
		GuildConfig guildConfig = guildService.findById(guild.getIdLong());
		guildConfig.setMute(muteId);

		guildService.update(guildConfig);
	}

	public void setMod(Guild guild, long modId) {
		GuildConfig guildConfig = guildService.findById(guild.getIdLong());
		guildConfig.setMod(modId);

		guildService.update(guildConfig);
	}

	@Getter
	public static class GuildSettings implements GuildSettingsProvider {
		private final long mute, logs, mod;
		private final String prefix;
		
		private GuildSettings(GuildConfig guildConfig) {
			this.mute = guildConfig.getMute();
			this.logs = guildConfig.getLogs();
			this.mod = guildConfig.getMod();
			this.prefix = guildConfig.getPrefix();
		}

		@Override
		public Collection<String> getPrefixes() {
			return Collections.singleton(prefix);
		}
	}
}
