/*
 * This file is part of Module.
 *
 * Module is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Module is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Module. If not, see <https://www.gnu.org/licenses/>.
 */

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
		StatsModel stats = getStats();
		stats.setExecutedCommands(stats.getExecutedCommands() + number);
		statsRepository.saveAndFlush(stats);
	}
}
