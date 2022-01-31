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
import org.module.dao.impl.WarnDaoImpl;
import org.module.model.Warn;
import org.module.dao.WarnDao;
import org.module.service.ModerationService;
import org.module.util.CheckUtil;
import org.module.util.SettingsUtil;

import java.util.List;

public class ModerationServiceImpl implements ModerationService {
	private final WarnDao warnDao = new WarnDaoImpl();

	@Override
	public boolean isModerator(Member member) {
		if (member.hasPermission(Permission.ADMINISTRATOR) || member.isOwner()) return true;
		Role modRole = SettingsUtil.getModRole(member.getGuild());
		if (modRole == null) return false;
		return CheckUtil.hasRole(member, modRole);
	}

	@Override
	public Warn getWarn(long id) {
		return warnDao.findById(id);
	}

	@Override
	public List<Warn> getWarns(Member member) {
		return warnDao.findAllByGuildAndUser(member.getGuild().getIdLong(), member.getIdLong());
	}

	@Override
	public long warn(Member member, String reason) {
		Warn warn = new Warn();
		warn.setGuild(member.getGuild().getIdLong());
		warn.setUser(member.getIdLong());
		warn.setReason(reason);
		warnDao.save(warn);
		return warn.getId();
	}

	@Override
	public void removeWarn(Warn warn) {
		warnDao.delete(warn);
	}
}
