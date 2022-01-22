package org.module.service.impl;

import org.module.model.Stats;
import org.module.repository.StatsRepository;
import org.module.service.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatsServiceImpl implements StatsService {
	@Autowired
	private StatsRepository statsRepository;

	@Override
	public Stats getStats() {
		return statsRepository.findById(1);
	}

	@Override
	public void incrementExecutedCommands() {
		Stats stats = getStats();
		stats.setExecutedCommands(stats.getExecutedCommands() + 1);
		statsRepository.saveAndFlush(stats);
	}
}
