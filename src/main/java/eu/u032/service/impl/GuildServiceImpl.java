/*
 * Module Discord Bot.
 * Copyright (C) 2022 untled032, Headcrab

 * UASM is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * UASM is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with UASM. If not, see https://www.gnu.org/licenses/.
 */

package eu.u032.service.impl;

import eu.u032.model.GuildConfig;
import eu.u032.repository.GuildRepository;
import eu.u032.service.GuildService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GuildServiceImpl implements GuildService {
	@Autowired
	private GuildRepository guildRepository;

	@Override
	public GuildConfig findById(long id) {
		return guildRepository.findById(id);
	}

	@Override
	public void save(GuildConfig guildConfig) {
		guildRepository.saveAndFlush(guildConfig);
	}

	@Override
	public void update(GuildConfig guildConfig) {
		guildRepository.saveAndFlush(guildConfig);
	}

	@Override
	public void delete(GuildConfig guildConfig) {
		guildRepository.delete(guildConfig);
	}
}
