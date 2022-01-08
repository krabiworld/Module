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

package eu.u032;

import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;

import java.awt.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
	// Patterns for searching emoji, member or channel mention.
	public static final Pattern EMOJI = Pattern.compile(":(\\d+)>");
    public static final Pattern MEMBER = Pattern.compile("<@!(\\d+)>");
    // public static final Pattern CHANNEL = Pattern.compile("<#(\\d+)>");

	public static void sendError(final CommandEvent event, final String error) {
		event.reactError();
		event.replyError(error, m -> m.delete().queueAfter(10, TimeUnit.SECONDS));
	}

	public static void sendSuccess(final CommandEvent event, final String success) {
		event.reactSuccess();
		event.replySuccess(success, m -> m.delete().queueAfter(20, TimeUnit.SECONDS));
	}

	public static void sendLog(final Guild guild, final EmbedBuilder embed) {
		embed.setTimestamp(new Date().toInstant());

		final String logsChannelId = Config.getString("LOGS_CHANNEL");
        final TextChannel textChannel = logsChannelId.isEmpty() ? null : guild.getTextChannelById(logsChannelId);

		if (textChannel == null) return;

        textChannel.sendMessageEmbeds(embed.build()).queue();
    }

    public static String[] splitArgs(final String args) {
        return args.split("\\s+");
    }

    public static String getId(final String arg, final Pattern pattern) {
        final Matcher matcher = pattern.matcher(arg);

        if (matcher.find()) {
            return matcher.group(1);
        } else if (arg.length() == 18) {
            return arg;
        }

        return "";
    }

    public static String getGluedArg(final String[] args, final int start) {
        final StringBuilder arg = new StringBuilder();

        for (int i = start; i < args.length; i++) {
            arg.append(args[i]).append(" ");
        }

        return arg.toString();
    }

    public static boolean hasRole(final Member member, final Role role) {
        for (final Role memberRole : member.getRoles()) {
            if (memberRole == role) return true;
        }
        return false;
    }

    public static Color getColor() {
        return Color.decode(Config.getString("COLOR"));
    }

    public static Color getColorGreen() {
        return Color.decode(Config.getString("COLOR_GREEN"));
    }

    public static Color getColorRed() {
        return Color.decode(Config.getString("COLOR_RED"));
    }

    public static Color getColorYellow() {
        return Color.decode(Config.getString("COLOR_YELLOW"));
    }
}
