/*
 * Module Discord Bot.
 * Copyright (C) 2022 untled032, Headcrab

 * Module is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * Module is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with Module. If not, see https://www.gnu.org/licenses/.
 */

package org.module.service.impl;

import net.dv8tion.jda.api.entities.Member;
import org.module.model.Owner;
import org.module.repository.OwnerRepository;
import org.module.service.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OwnerServiceImpl implements OwnerService {
	@Autowired
	private OwnerRepository ownerRepository;

	@Override
	public Owner findById(long id) {
		return ownerRepository.findById(id);
	}

	@Override
	public boolean isOwner(Member member) {
		return findById(member.getIdLong()) != null;
	}

	@Override
	public void addOwner(Owner owner) {
		ownerRepository.saveAndFlush(owner);
	}

	@Override
	public void removeOwner(Owner owner) {
		ownerRepository.delete(owner);
	}
}