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

package org.module.util;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.utils.FinderUtil;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import java.util.List;

public class ArgsUtil {
	/** Will return split arguments. */
    public static String[] split(String args) {
        return args.split("\\s+");
    }

	/** Get {@link Member} from argument. */
	public static Member getMember(CommandEvent event, String arg) {
		List<Member> members = FinderUtil.findMembers(arg, event.getGuild());
		for (Member member : members) {
			return member;
		}
		return null;
	}

	/** Get {@link User} from argument. */
	public static User getUser(CommandEvent event, String arg) {
		List<User> users = FinderUtil.findUsers(arg, event.getJDA());
		for (User user : users) {
			return user;
		}
		return null;
	}

	/** Get {@link Emote} from argument. */
	public static Emote getEmote(CommandEvent event, String arg) {
		List<Emote> emotes = FinderUtil.findEmotes(arg, event.getGuild());
		for (Emote emote : emotes) {
			return emote;
		}
		return null;
	}

	/** Get {@link Emote} from argument. */
	public static Emote getEmote(SlashCommandEvent event, String arg) {
		List<Emote> emotes = FinderUtil.findEmotes(arg, event.getGuild());
		for (Emote emote : emotes) {
			return emote;
		}
		return null;
	}

	/** Get {@link TextChannel} from argument. */
	public static TextChannel getTextChannel(CommandEvent event, String arg) {
		List<TextChannel> channels = FinderUtil.findTextChannels(arg, event.getGuild());
		for (TextChannel channel : channels) {
			return channel;
		}
		return null;
	}

	/** Get {@link Role} from argument. */
	public static Role getRole(CommandEvent event, String arg) {
		List<Role> roles = FinderUtil.findRoles(arg, event.getGuild());
		for (Role role : roles) {
			return role;
		}
		return null;
	}

	/** Will return glued argument. */
    public static String getGluedArg(String[] args, int start) {
        StringBuilder arg = new StringBuilder();

        for (int i = start; i < args.length; i++) {
            arg.append(args[i]).append(" ");
        }

        return arg.toString();
    }
}
