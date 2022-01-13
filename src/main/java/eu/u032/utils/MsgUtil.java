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
import eu.u032.GuildManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class MsgUtil {
	/** React and send error message, after 10 seconds message has been deleted. */
	public static void sendError(final CommandEvent event, final String content) {
		event.reactError();
		event.replyError(content, m -> m.delete().queueAfter(10, TimeUnit.SECONDS));
	}

	/** React and send success message, after 20 seconds message has been deleted. */
	public static void sendSuccess(final CommandEvent event, final String content) {
		event.reactSuccess();
		event.replySuccess(content, m -> m.delete().queueAfter(20, TimeUnit.SECONDS));
	}

	/** Send log message with addition of timestamp to embed. */
	public static void sendLog(final Guild guild, final EmbedBuilder embed) {
		sendLog(guild, embed, null);
    }

	/** Send log message with file and addition of timestamp to embed. */
	public static void sendLog(final Guild guild, final EmbedBuilder embed, final byte[] file) {
		embed.setTimestamp(new Date().toInstant());

		final GuildManager.GuildSettings manager = new GuildManager().getSettings(guild);
		if (Objects.requireNonNull(manager).getLogs() == 0) return;

		final TextChannel textChannel = guild.getTextChannelById(manager.getLogs());

		if (textChannel == null) return;

		if (file == null) {
			textChannel.sendMessageEmbeds(embed.build()).queue();
		} else {
			textChannel.sendMessageEmbeds(embed.build()).addFile(file, ".txt").queue();
		}
	}

	/** Add word to template. */
	public static String getTemplate(String template, String word) {
		return String.format(template, word);
	}
}
