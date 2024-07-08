package org.module.manager;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.Nullable;
import org.module.model.GuildModel;
import org.module.service.GuildService;
import org.module.structure.GuildProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GuildManager extends ListenerAdapter implements GuildProvider.Manager {
	private final GuildService guildService;

	@Autowired
	public GuildManager(GuildService guildService) {
		this.guildService = guildService;
	}

	@Override
	public void onGuildJoin(GuildJoinEvent event) {
		guildService.getGuild(event.getGuild().getIdLong());
	}

	@Override
	public GuildProvider.Settings getSettings(Guild guild) {
		return new GuildSettings(guild, guildService.getGuild(guild.getIdLong()));
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

	public static class GuildSettings implements GuildProvider.Settings {
		@Nullable private final TextChannel logsChannel;
		@Nullable private final Role moderatorRole;

		private GuildSettings(Guild guild, GuildModel guildModel) {
			this.logsChannel = guild.getTextChannelById(guildModel.getLogs());
			this.moderatorRole = guild.getRoleById(guildModel.getMod());
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
