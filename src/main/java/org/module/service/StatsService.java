package org.module.service;

import org.module.model.Stats;

public interface StatsService {
	Stats getStats();

	void incrementExecutedCommands();
}
