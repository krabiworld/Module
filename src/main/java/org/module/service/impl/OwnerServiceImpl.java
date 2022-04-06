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

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import org.module.repository.OwnerRepository;
import org.module.model.OwnerModel;
import org.module.service.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OwnerServiceImpl implements OwnerService {
	private final OwnerRepository ownerRepository;

	@Autowired
	public OwnerServiceImpl(OwnerRepository ownerRepository) {
		this.ownerRepository = ownerRepository;
	}

	@Override
	public OwnerModel getOwner(long id) {
		return ownerRepository.findById(id);
	}

	@Override
	public boolean isNotOwner(Member member) {
		return getOwner(member.getIdLong()) == null;
	}

	@Override
	public boolean addOwner(User user) {
		if (getOwner(user.getIdLong()) != null) return false;

		OwnerModel owner = new OwnerModel();
		owner.setId(user.getIdLong());
		ownerRepository.saveAndFlush(owner);
		return true;
	}

	@Override
	public boolean removeOwner(User user) {
		OwnerModel owner = getOwner(user.getIdLong());
		if (owner == null) return false;

		ownerRepository.delete(owner);
		return true;
	}
}
