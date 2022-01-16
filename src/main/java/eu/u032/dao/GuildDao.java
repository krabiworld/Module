package eu.u032.dao;

import eu.u032.model.GuildModel;

public interface GuildDao {
	GuildModel findById(final long id);

	void save(final GuildModel guildModel);

	void update(final GuildModel guildModel);

	void delete(final GuildModel guildModel);
}
