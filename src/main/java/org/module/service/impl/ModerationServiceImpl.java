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
import org.module.model.WarnModel;
import org.module.repository.WarnRepository;
import org.module.service.ModerationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModerationServiceImpl implements ModerationService {
	private final WarnRepository warnRepository;

	@Autowired
	public ModerationServiceImpl(WarnRepository warnRepository) {
		this.warnRepository = warnRepository;
	}

	@Override
	@Cacheable("warn")
	public WarnModel getWarn(long id) {
		return warnRepository.findById(id);
	}

	@Override
	@Cacheable("warns")
	public List<WarnModel> getWarns(Member member) {
		return warnRepository.findAllByGuildAndUser(member.getGuild().getIdLong(), member.getIdLong());
	}

	@Override
	public long warn(Member member, String reason) {
		WarnModel warn = new WarnModel();
		warn.setGuild(member.getGuild().getIdLong());
		warn.setUser(member.getIdLong());
		warn.setReason(reason);
		warnRepository.saveAndFlush(warn);
		return warn.getId();
	}

	@Override
	public void removeWarn(WarnModel warnModel) {
		warnRepository.delete(warnModel);
	}
}
