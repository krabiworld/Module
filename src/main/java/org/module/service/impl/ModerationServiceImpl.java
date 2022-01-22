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

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import org.module.model.Warn;
import org.module.repository.WarnRepository;
import org.module.service.ModerationService;
import org.module.util.CheckUtil;
import org.module.util.SettingsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModerationServiceImpl implements ModerationService {
	@Autowired
	private WarnRepository warnRepository;

	@Override
	public boolean isModerator(Member member) {
		if (member.hasPermission(Permission.ADMINISTRATOR) || member.isOwner()) return true;
		Role modRole = SettingsUtil.getModRole(member.getGuild());
		if (modRole == null) return false;
		return CheckUtil.hasRole(member, modRole);
	}

	@Override
	public Warn getWarn(long id) {
		return warnRepository.findById(id);
	}

	@Override
	public List<Warn> getWarns(Member member) {
		return warnRepository.findAllByGuildAndUser(member.getGuild().getIdLong(), member.getIdLong());
	}

	@Override
	public long warn(Member member, String reason) {
		Warn warn = new Warn();
		warn.setGuild(member.getGuild().getIdLong());
		warn.setUser(member.getIdLong());
		warn.setReason(reason);
		warnRepository.saveAndFlush(warn);
		return warn.getId();
	}

	@Override
	public void removeWarn(Warn warn) {
		warnRepository.delete(warn);
	}
}
