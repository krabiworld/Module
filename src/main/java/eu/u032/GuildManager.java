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

package eu.u032;

import com.jagrosh.jdautilities.command.GuildSettingsManager;
import com.jagrosh.jdautilities.command.GuildSettingsProvider;
import eu.u032.model.GuildModel;
import eu.u032.service.GuildService;
import lombok.Getter;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Collection;
import java.util.Collections;

public class GuildManager extends ListenerAdapter implements GuildSettingsManager<GuildManager.GuildSettings> {
	private final static GuildService guildService = new GuildService();

	@Override
	public void onGuildJoin(final GuildJoinEvent event) {
		if (guildService.findById(event.getGuild().getIdLong()) != null) return;

		final GuildModel guildModel = new GuildModel();
		guildModel.setId(event.getGuild().getIdLong());

		guildService.save(guildModel);
	}

	@Override
	public GuildSettings getSettings(final Guild guild) {
		return new GuildSettings(new GuildService().findById(guild.getIdLong()));
	}

	public void setPrefix(final Guild guild, final String prefix) {
		final GuildModel guildModel = guildService.findById(guild.getIdLong());
		guildModel.setPrefix(prefix);

		guildService.update(guildModel);
	}

	public void setLogs(final Guild guild, final long logsId) {
		final GuildModel guildModel = guildService.findById(guild.getIdLong());
		guildModel.setLogs(logsId);

		guildService.update(guildModel);
	}

	public void setMute(final Guild guild, final long muteId) {
		final GuildModel guildModel = guildService.findById(guild.getIdLong());
		guildModel.setMute(muteId);

		guildService.update(guildModel);
	}

	public void setMod(final Guild guild, final long modId) {
		final GuildModel guildModel = guildService.findById(guild.getIdLong());
		guildModel.setMod(modId);

		guildService.update(guildModel);
	}

	@Getter
	public static class GuildSettings implements GuildSettingsProvider {
		private final long mute, logs, mod;
		private final String prefix;
		
		private GuildSettings(GuildModel guildModel) {
			this.mute = guildModel.getMute();
			this.logs = guildModel.getLogs();
			this.mod = guildModel.getMod();
			this.prefix = guildModel.getPrefix();
		}

		@Override
		public Collection<String> getPrefixes() {
			return Collections.singleton(prefix);
		}
	}
}
