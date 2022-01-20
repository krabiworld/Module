package org.module.service;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;

public interface MessageService {
	void sendError(CommandEvent event, String key, Object... args);

	void sendErrorMessage(CommandEvent event, String content);

	void sendSuccess(CommandEvent event, String key, Object... args);

	void sendSuccessMessage(CommandEvent event, String content);

	void sendLog(Guild guild, EmbedBuilder embed);

	void sendLog(Guild guild, EmbedBuilder embed, byte[] file);

	void sendHelp(CommandEvent event, Command command);
}
