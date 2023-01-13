package org.module.service;

import org.module.model.StatsModel;

public interface StatsService {
	StatsModel getStats();

	void incrementExecutedCommands(long number);
}
