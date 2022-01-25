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

package org.module.events.message;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.module.constants.Emoji;
import org.module.service.CookieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageReactionAdd extends ListenerAdapter {
	private final CookieService cookieService;

	@Autowired
	public MessageReactionAdd(CookieService cookieService) {
		this.cookieService = cookieService;
	}

	@Override
	public void onMessageReactionAdd(MessageReactionAddEvent event) {
		User user = event.getUser();
		if (user == null || user.isBot() || !event.getReactionEmote().getName().equals(Emoji.COOKIE)) {
			return;
		}

		event.retrieveMessage().queue(m -> {
			if (m.getAuthor().isBot() || m.getAuthor() == user) {
				return;
			}
			cookieService.incrementCookie(m.getGuild(), m.getAuthor());
		});
	}
}
