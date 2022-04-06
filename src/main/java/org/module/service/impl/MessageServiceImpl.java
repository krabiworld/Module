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

package org.module.service.impl;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import org.module.Constants;
import org.module.Locale;
import org.module.manager.GuildManager;
import org.module.service.MessageService;
import org.module.util.EmbedUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@Service
public class MessageServiceImpl implements MessageService {
	private final GuildManager manager;

	private static final Logger LOGGER = LoggerFactory.getLogger(MessageServiceImpl.class);
	private static final Properties PROPERTIES = new Properties();
	private static final InputStream EN = MessageServiceImpl.class.getResourceAsStream("/locales/en.properties");
	private static final InputStream RU = MessageServiceImpl.class.getResourceAsStream("/locales/ru.properties");
	private static final Consumer<Message> errorDelay = m -> m.delete().queueAfter(10, TimeUnit.SECONDS);
	private static final Consumer<Message> successDelay = m -> m.delete().queueAfter(20, TimeUnit.SECONDS);

	@Autowired
	public MessageServiceImpl(GuildManager manager) {
		this.manager = manager;
	}

	@Override
	public void sendError(CommandEvent event, Locale locale, String key, Object... args) {
		event.reactError();
		event.reply(new EmbedUtil(Constants.ERROR, locale.get(key, args)).build(),
			errorDelay);
	}

	@Override
	public void sendSuccess(CommandEvent event, Locale locale, String key, Object... args) {
		event.reactSuccess();
		event.reply(new EmbedUtil(Constants.SUCCESS, locale.get(key, args)).build(),
			successDelay);
	}

	@Override
	public void sendLog(Guild guild, EmbedBuilder embed) {
		sendLog(guild, embed, null);
	}

	@Override
	public void sendLog(Guild guild, EmbedBuilder embed, byte[] file) {
		embed.setTimestamp(new Date().toInstant());

		GuildManager.GuildSettings settings = manager.getSettings(guild);
		if (settings == null) return;
		TextChannel logsChannel = settings.getLogsChannel();
		if (logsChannel == null) return;

		if (file == null) {
			logsChannel.sendMessageEmbeds(embed.build()).queue();
		} else {
			logsChannel.sendMessageEmbeds(embed.build()).addFile(file, ".txt").queue();
		}
	}

	@Override
	public void sendHelp(CommandEvent event, Command command, Locale locale) {
		GuildManager.GuildSettings settings = manager.getSettings(event.getGuild());
		if (settings == null) return;

		EmbedUtil embed = new EmbedUtil(command, locale, settings.getPrefix());
		event.reply(embed.build(), successDelay);
	}

	@Override
	public Locale getLocale(String lang) {
		if (EN == null || RU == null) throw new NullPointerException("Locale en or ru is null!");

		try {
			InputStreamReader reader = new InputStreamReader(lang.equals("ru") ? RU : EN);

			PROPERTIES.load(reader);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}

		return new Locale(PROPERTIES);
	}

	@Override
	public Locale getLocale(Guild guild) {
		GuildManager.GuildSettings settings = manager.getSettings(guild);
		if (settings == null) return null;

		return getLocale(settings.getLang());
	}
}
