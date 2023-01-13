package org.module.service.impl;

import org.module.model.GuildModel;
import org.module.repository.GuildRepository;
import org.module.service.GuildService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class GuildServiceImpl implements GuildService {
	private final GuildRepository guildRepository;

	@Autowired
	public GuildServiceImpl(GuildRepository guildRepository) {
		this.guildRepository = guildRepository;
	}

	@Override
	@Cacheable("guild")
	public GuildModel getGuild(long id) {
		return guildRepository.findById(id);
	}

	@Override
	public void addGuild(GuildModel guildModel) {
		guildRepository.saveAndFlush(guildModel);
	}

	@Override
	public void updateGuild(GuildModel guildModel) {
		guildRepository.saveAndFlush(guildModel);
	}
}
