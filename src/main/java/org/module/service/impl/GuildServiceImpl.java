/*
 * This file is part of Module.

 * Module is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * Module is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with Module. If not, see <https://www.gnu.org/licenses/>.
 */

package org.module.service.impl;

import org.module.model.GuildConfig;
import org.module.repository.GuildRepository;
import org.module.service.GuildService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GuildServiceImpl implements GuildService {
	private final GuildRepository guildRepository;

	@Autowired
	public GuildServiceImpl(GuildRepository guildRepository) {
		this.guildRepository = guildRepository;
	}

	@Override
	public GuildConfig getGuild(long id) {
		return guildRepository.findById(id);
	}

	@Override
	public void addGuild(GuildConfig guildConfig) {
		guildRepository.saveAndFlush(guildConfig);
	}

	@Override
	public void updateGuild(GuildConfig guildConfig) {
		guildRepository.saveAndFlush(guildConfig);
	}

	@Override
	public void deleteGuild(GuildConfig guildConfig) {
		guildRepository.delete(guildConfig);
	}
}
