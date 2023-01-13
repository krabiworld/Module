package org.module.structure;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.jetbrains.annotations.Nullable;

public class GuildProvider {
	public interface Manager {
		@Nullable
		Settings getSettings(Guild guild);

		void setLogsChannel(Guild guild, TextChannel channel);

		void setModeratorRole(Guild guild, Role role);
	}

	public interface Settings {
		@Nullable
		TextChannel getLogsChannel();

		@Nullable
		Role getModeratorRole();
	}
}
