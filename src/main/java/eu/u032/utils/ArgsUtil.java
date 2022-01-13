/*
 * UASM Discord Bot.
 * Copyright (C) 2022 untled032, Headcrab

 * UASM is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * UASM is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with UASM. If not, see https://www.gnu.org/licenses/.
 */

package eu.u032.utils;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.utils.FinderUtil;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

public class ArgsUtil {
	/** Will return split arguments. */
    public static String[] split(final String args) {
        return args.split("\\s+");
    }

	/** Get {@link Member} from argument. */
	public static Member getMember(final CommandEvent event, final String arg) {
		List<Member> members = FinderUtil.findMembers(arg, event.getGuild());
		for (final Member member : members) {
			return member;
		}
		return null;
	}

	/** Get {@link Emote} from argument. */
	public static Emote getEmote(final CommandEvent event, final String arg) {
		List<Emote> emotes = FinderUtil.findEmotes(arg, event.getGuild());
		for (final Emote emote : emotes) {
			return emote;
		}
		return null;
	}

	/** Get {@link TextChannel} from argument. */
	public static TextChannel getChannel(final CommandEvent event, final String arg) {
		List<TextChannel> channels = FinderUtil.findTextChannels(arg, event.getGuild());
		for (final TextChannel channel : channels) {
			return channel;
		}
		return null;
	}

	/** Get {@link Role} from argument. */
	public static Role getRole(final CommandEvent event, final String arg) {
		List<Role> roles = FinderUtil.findRoles(arg, event.getGuild());
		for (final Role role : roles) {
			return role;
		}
		return null;
	}

	/** Will return glued argument. */
    public static String getGluedArg(final String[] args, final int start) {
        final StringBuilder arg = new StringBuilder();

        for (int i = start; i < args.length; i++) {
            arg.append(args[i]).append(" ");
        }

        return arg.toString();
    }
}
