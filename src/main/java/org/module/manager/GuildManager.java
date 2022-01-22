/*
 * Module Discord Bot.
 * Copyright (C) 2022 untled032, Headcrab

 * Module is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * Module is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with Module. If not, see https://www.gnu.org/licenses/.
 */

package org.module.manager;

import com.jagrosh.jdautilities.command.GuildSettingsManager;
import com.jagrosh.jdautilities.command.GuildSettingsProvider;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import org.module.model.GuildConfig;
import org.module.service.GuildService;
import lombok.Getter;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;

@Component
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

	public void setLogsChannel(Guild guild, TextChannel channel) {
		GuildConfig guildConfig = guildService.findById(guild.getIdLong());
		guildConfig.setLogs(channel == null ? 0 : channel.getIdLong());

		guildService.update(guildConfig);
	}

	public void setMuteRole(Guild guild, Role role) {
		GuildConfig guildConfig = guildService.findById(guild.getIdLong());
		guildConfig.setMute(role.getIdLong());

		guildService.update(guildConfig);
	}

	public void setModeratorRole(Guild guild, Role role) {
		GuildConfig guildConfig = guildService.findById(guild.getIdLong());
		guildConfig.setMod(role.getIdLong());

		guildService.update(guildConfig);
	}

	@Getter
	public static class GuildSettings implements GuildSettingsProvider {
		private final long muteRole, logsChannel, moderatorRole;
		private final String prefix;
		
		private GuildSettings(GuildConfig guildConfig) {
			this.muteRole = guildConfig.getMute();
			this.logsChannel = guildConfig.getLogs();
			this.moderatorRole = guildConfig.getMod();
			this.prefix = guildConfig.getPrefix();
		}

		@Override
		public Collection<String> getPrefixes() {
			return Collections.singleton(prefix);
		}
	}
}
