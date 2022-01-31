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
import org.module.dao.impl.OwnerDaoImpl;
import org.module.model.Owner;
import org.module.dao.OwnerDao;
import org.module.service.OwnerService;

public class OwnerServiceImpl implements OwnerService {
	private final OwnerDao ownerDao = new OwnerDaoImpl();

	@Override
	public Owner getOwner(long id) {
		return ownerDao.findById(id);
	}

	@Override
	public boolean isNotOwner(Member member) {
		return getOwner(member.getIdLong()) == null;
	}

	@Override
	public void addOwner(Owner owner) {
		ownerDao.save(owner);
	}

	@Override
	public void removeOwner(Owner owner) {
		ownerDao.delete(owner);
	}
}
