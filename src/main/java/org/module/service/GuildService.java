package org.module.service;

import org.module.model.GuildModel;

public interface GuildService {
	GuildModel getGuild(long id);

	void addGuild(GuildModel guildModel);

	void updateGuild(GuildModel guildModel);
}
