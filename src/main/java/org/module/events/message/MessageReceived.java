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

import com.jagrosh.jdautilities.commons.utils.FinderUtil;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.module.cache.MessageCache;
import org.module.constants.Emoji;
import org.module.service.CookieService;
import org.module.util.ArgsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MessageReceived extends ListenerAdapter {
	private final CookieService cookieService;

	@Autowired
	public MessageReceived(CookieService cookieService) {
		this.cookieService = cookieService;
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		MessageCache.addMessage(event.getMessage());

		if (event.getAuthor().isBot() || event.getAuthor().isSystem()) return;

		String[] message = ArgsUtil.split(event.getMessage().getContentRaw());
		if (message.length < 2) return;

		List<Member> members = FinderUtil.findMembers(message[0], event.getGuild());
		if (members.isEmpty()) return;
		User user = members.get(0).getUser();

		if (user == event.getAuthor() || !message[1].equals(Emoji.COOKIE)) return;

		cookieService.incrementCookie(event.getGuild(), user);
	}
}
