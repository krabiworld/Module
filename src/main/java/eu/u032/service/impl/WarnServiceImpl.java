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

import eu.u032.model.Warn;
import eu.u032.repository.WarnRepository;
import eu.u032.service.WarnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WarnServiceImpl implements WarnService {
	@Autowired
	private WarnRepository warnRepository;

	@Override
	public Warn findById(long id) {
		return warnRepository.findById(id);
	}

	@Override
	public List<Warn> findAllByGuildAndUser(long guild, long user) {
		return warnRepository.findAllByGuildAndUser(guild, user);
	}

	@Override
	public void save(Warn warn) {
		warnRepository.saveAndFlush(warn);
	}

	@Override
	public void delete(Warn warn) {
		warnRepository.delete(warn);
	}
}
