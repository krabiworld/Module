/*
 * Module Discord Bot.
 * Copyright (C) 2022 untled032, Headcrab

 * Module is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * Module is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with Module. If not, see https://www.gnu.org/licenses/.
 */

package org.module.service.impl;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import org.module.constants.Constants;
import org.module.service.MessageService;
import org.module.util.EmbedUtil;
import org.module.util.PropertyUtil;
import org.module.util.SettingsUtil;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
public class MessageServiceImpl implements MessageService {
	@Override
	public void sendError(CommandEvent event, String key, Object... args) {
		event.reactError();
		event.reply(new EmbedUtil(EmbedUtil.Type.ERROR, PropertyUtil.getProperty(key, args)).build(),
			m -> m.delete().queueAfter(10, TimeUnit.SECONDS));
	}

	@Override
	public void sendErrorMessage(CommandEvent event, String content) {
		event.reactError();
		event.replyError(content, m -> m.delete().queueAfter(10, TimeUnit.SECONDS));
	}

	@Override
	public void sendSuccess(CommandEvent event, String key, Object... args) {
		event.reactSuccess();
		event.reply(new EmbedUtil(EmbedUtil.Type.SUCCESS, PropertyUtil.getProperty(key, args)).build(),
			m -> m.delete().queueAfter(20, TimeUnit.SECONDS));
	}

	@Override
	public void sendLog(Guild guild, EmbedBuilder embed) {
		sendLog(guild, embed, null);
	}

	@Override
	public void sendLog(Guild guild, EmbedBuilder embed, byte[] file) {
		embed.setTimestamp(new Date().toInstant());

		TextChannel logsChannel = SettingsUtil.getLogsChannel(guild);
		if (logsChannel == null) return;

		if (file == null) {
			logsChannel.sendMessageEmbeds(embed.build()).queue();
		} else {
			logsChannel.sendMessageEmbeds(embed.build()).addFile(file, ".txt").queue();
		}
	}

	@Override
	public void sendHelp(CommandEvent event, Command command) {
		String prefix = SettingsUtil.getPrefix(event.getGuild());
		String arguments = command.getArguments().isEmpty() ? "" : " " + command.getArguments();
		EmbedBuilder embed = new EmbedBuilder()
			.setColor(Constants.COLOR)
			.setTitle("Information of command " + command.getName())
			.setDescription("`" + prefix + command.getName() + arguments + "`\n" + command.getHelp());
		event.reply(embed.build(), m -> m.delete().queueAfter(10, TimeUnit.SECONDS));
	}
}