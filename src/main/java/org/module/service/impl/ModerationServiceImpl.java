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

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import org.module.repository.WarnRepository;
import org.module.manager.GuildManager;
import org.module.model.WarnModel;
import org.module.service.ModerationService;
import org.module.util.CheckUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModerationServiceImpl implements ModerationService {
	private final WarnRepository warnRepository;
	private final GuildManager manager;

	@Autowired
	public ModerationServiceImpl(WarnRepository warnRepository, GuildManager manager) {
		this.warnRepository = warnRepository;
		this.manager = manager;
	}

	@Override
	public boolean isModerator(Member member) {
		if (member.hasPermission(Permission.ADMINISTRATOR) || member.isOwner()) return true;

		GuildManager.GuildSettings settings = manager.getSettings(member.getGuild());
		if (settings == null) return false;
		Role modRole = settings.getModeratorRole();
		if (modRole == null) return false;

		return CheckUtil.hasRole(member, modRole);
	}

	@Override
	public WarnModel getWarn(long id) {
		return warnRepository.findById(id);
	}

	@Override
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
