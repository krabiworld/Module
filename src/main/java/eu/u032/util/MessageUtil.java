/*
 * Module Discord Bot.
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

package eu.u032.util;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import eu.u032.Constants;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class MessageUtil {
	private static final Properties PROPERTIES = new Properties();

	/** React and send error message from properties, after 10 seconds message has been deleted. */
	public static void sendError(CommandEvent event, String key, Object... args) {
		sendErrorMessage(event, getMessage(key, args));
	}

	/** React and send error message, after 10 seconds message has been deleted. */
	public static void sendErrorMessage(CommandEvent event, String content) {
		event.reactError();
		event.replyError(content, m -> m.delete().queueAfter(10, TimeUnit.SECONDS));
	}

	/** React and send success message from properties, after 20 seconds message has been deleted. */
	public static void sendSuccess(CommandEvent event, String key, Object... args) {
		sendSuccessMessage(event, getMessage(key, args));
	}

	/** React and send success message, after 20 seconds message has been deleted. */
	public static void sendSuccessMessage(CommandEvent event, String content) {
		event.reactSuccess();
		event.replySuccess(content, m -> m.delete().queueAfter(20, TimeUnit.SECONDS));
	}

	/** Send log message with addition of timestamp to embed. */
	public static void sendLog(Guild guild, EmbedBuilder embed) {
		sendLog(guild, embed, null);
    }

	/** Send log message with file and addition of timestamp to embed. */
	public static void sendLog(Guild guild, EmbedBuilder embed, byte[] file) {
		embed.setTimestamp(new Date().toInstant());

		TextChannel logsChannel = SettingsUtil.getLogsChannel(guild);
		if (logsChannel == null) return;

		if (file == null) {
			logsChannel.sendMessageEmbeds(embed.build()).queue();
		} else {
			logsChannel.sendMessageEmbeds(embed.build()).addFile(file, ".txt").queue();
		}
	}

	public static void sendHelp(CommandEvent event, Command command) {
		String prefix = SettingsUtil.getPrefix(event.getGuild());
		String arguments = command.getArguments().isEmpty() ? "" : " " + command.getArguments();
		EmbedBuilder embed = new EmbedBuilder()
			.setColor(Constants.COLOR)
			.setTitle("Information of command " + command.getName())
			.setDescription("`" + prefix + command.getName() + arguments + "`\n" + command.getHelp());
		event.reply(embed.build(), m -> m.delete().queueAfter(10, TimeUnit.SECONDS));
	}

	private static String getProperty(String key) {
		try {
			PROPERTIES.load(MessageUtil.class.getResourceAsStream("/messages.properties"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return PROPERTIES.getProperty(key);
	}

	/** Get message from properties file. */
	public static String getMessage(String key, Object... args) {
		if (args == null) {
			return getProperty(key);
		} else {
			return String.format(getProperty(key), args);
		}
	}
}
