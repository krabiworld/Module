package org.module.service;

import org.module.model.GuildModel;

public interface GuildService {
	GuildModel getGuild(long id);

	void updateGuild(GuildModel guildModel);
}
