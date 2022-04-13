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

package org.module.manager;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.module.Constants;
import org.module.model.GuildModel;
import org.module.service.GuildService;
import org.module.structure.GuildManagerProvider;
import org.module.structure.GuildSettingsProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Component
public class GuildManager extends ListenerAdapter implements GuildManagerProvider {
	private final GuildService guildService;

	@Autowired
	public GuildManager(GuildService guildService) {
		this.guildService = guildService;
	}

	@Override
	public void onGuildJoin(GuildJoinEvent event) {
		if (guildService.getGuild(event.getGuild().getIdLong()) != null) return;

		GuildModel guildConfig = new GuildModel();
		guildConfig.setId(event.getGuild().getIdLong());

		guildService.addGuild(guildConfig);
	}

	@Override
	public GuildSettingsProvider getSettings(Guild guild) {
		return new GuildSettings(guild, guildService.getGuild(guild.getIdLong()));
	}

	@Override
	public void setPrefix(Guild guild, String prefix) {
		GuildModel guildConfig = guildService.getGuild(guild.getIdLong());
		guildConfig.setPrefix(prefix);

		guildService.updateGuild(guildConfig);
	}

	@Override
	public void setLang(Guild guild, Constants.Language lang) {
		GuildModel guildConfig = guildService.getGuild(guild.getIdLong());
		guildConfig.setLang(lang == Constants.Language.EN ? "en" : "ru");

		guildService.updateGuild(guildConfig);
	}

	@Override
	public void setLogsChannel(Guild guild, TextChannel channel) {
		GuildModel guildConfig = guildService.getGuild(guild.getIdLong());
		guildConfig.setLogs(channel == null ? 0 : channel.getIdLong());

		guildService.updateGuild(guildConfig);
	}

	@Override
	public void setModeratorRole(Guild guild, Role role) {
		GuildModel guildConfig = guildService.getGuild(guild.getIdLong());
		guildConfig.setMod(role.getIdLong());

		guildService.updateGuild(guildConfig);
	}

	public static class GuildSettings implements GuildSettingsProvider {
		@Nonnull private final String prefix, lang;
		@Nullable private final TextChannel logsChannel;
		@Nullable private final Role moderatorRole;

		private GuildSettings(Guild guild, GuildModel guildModel) {
			this.prefix = guildModel.getPrefix();
			this.lang = guildModel.getLang();
			this.logsChannel = guild.getTextChannelById(guildModel.getLogs());
			this.moderatorRole = guild.getRoleById(guildModel.getMod());
		}

		@Nonnull
		@Override
		public String getPrefix() {
			return prefix;
		}

		@Nonnull
		@Override
		public String getLang() {
			return lang;
		}

		@Nullable
		@Override
		public TextChannel getLogsChannel() {
			return logsChannel;
		}

		@Nullable
		@Override
		public Role getModeratorRole() {
			return moderatorRole;
		}
	}
}
