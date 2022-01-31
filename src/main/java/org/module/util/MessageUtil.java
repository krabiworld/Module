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

package org.module.util;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import org.module.Constants;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class MessageUtil {
	private static final Consumer<Message> errorDelay = m -> m.delete().queueAfter(10, TimeUnit.SECONDS);
	private static final Consumer<Message> successDelay = m -> m.delete().queueAfter(20, TimeUnit.SECONDS);

	public static void sendError(CommandEvent event, String key, Object... args) {
		event.reactError();
		event.reply(new EmbedUtil(Constants.ERROR, PropertyUtil.getProperty(key, args)).build(),
			errorDelay);
	}

	public static void sendError(SlashCommandEvent event, String key, Object... args) {
		event.replyEmbeds(new EmbedUtil(Constants.ERROR, PropertyUtil.getProperty(key, args)).build())
			.setEphemeral(true)
			.queue();
	}

	public static void sendSuccess(CommandEvent event, String key, Object... args) {
		event.reactSuccess();
		event.reply(new EmbedUtil(Constants.SUCCESS, PropertyUtil.getProperty(key, args)).build(),
			successDelay);
	}

	public static void sendSuccess(SlashCommandEvent event, String key, Object... args) {
		event.replyEmbeds(new EmbedUtil(Constants.SUCCESS, PropertyUtil.getProperty(key, args)).build())
			.queue();
	}

	public static void sendLog(Guild guild, EmbedBuilder embed) {
		sendLog(guild, embed, null);
	}

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
		EmbedUtil embed = new EmbedUtil(command, SettingsUtil.getPrefix(event.getGuild()));
		event.reply(embed.build(), successDelay);
	}

	public static void sendEphemeralHelp(SlashCommandEvent event, Command command) {
		EmbedUtil embed = new EmbedUtil(command, "/");
		event.replyEmbeds(embed.build()).setEphemeral(true).queue();
	}
}
