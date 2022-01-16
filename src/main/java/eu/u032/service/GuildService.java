package eu.u032.service;

import eu.u032.dao.impl.GuildDaoImpl;
import eu.u032.model.GuildModel;

public class GuildService {
	private final GuildDaoImpl guildDao = new GuildDaoImpl();

	public GuildModel findById(final long id) {
		return guildDao.findById(id);
	}

	public void save(final GuildModel guildModel) {
		guildDao.save(guildModel);
	}

	public void update(final GuildModel guildModel) {
		guildDao.update(guildModel);
	}

	public void delete(final GuildModel guildModel) {
		guildDao.delete(guildModel);
	}
}
