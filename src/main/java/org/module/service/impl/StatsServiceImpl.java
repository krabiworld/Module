package org.module.service.impl;

import org.module.model.StatsModel;
import org.module.repository.StatsRepository;
import org.module.service.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class StatsServiceImpl implements StatsService {
	private final StatsRepository statsRepository;

	@Autowired
	public StatsServiceImpl(StatsRepository statsRepository) {
		this.statsRepository = statsRepository;
	}

	@Override
	@Cacheable("stats")
	public StatsModel getStats() {
		return statsRepository.findById(1);
	}

	@Override
	public void incrementExecutedCommands(long number) {
		//noinspection SpringCacheableMethodCallsInspection
		StatsModel stats = getStats();
		stats.setExecutedCommands(stats.getExecutedCommands() + number);
		statsRepository.saveAndFlush(stats);
	}
}
