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

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import org.module.model.Cookie;
import org.module.repository.CookieRepository;
import org.module.service.CookieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CookieServiceImpl implements CookieService {
	private final CookieRepository cookieRepository;

	@Autowired
	public CookieServiceImpl(CookieRepository cookieRepository) {
		this.cookieRepository = cookieRepository;
	}

	@Override
	public long getCookies(Guild guild, User user) {
		Cookie cookie = cookieRepository.findByGuildAndUser(guild.getIdLong(), user.getIdLong());
		return cookie == null ? 0 : cookie.getCount();
	}

	@Override
	public void incrementCookie(Guild guild, User user) {
		Cookie cookie = cookieRepository.findByGuildAndUser(guild.getIdLong(), user.getIdLong());
		if (cookie == null) {
			Cookie newCookie = new Cookie();
			newCookie.setUser(user.getIdLong());
			newCookie.setGuild(guild.getIdLong());
			newCookie.setCount(1);
			cookieRepository.saveAndFlush(newCookie);
			return;
		}
		cookie.setCount(cookie.getCount() + 1);
		cookieRepository.saveAndFlush(cookie);
	}
}