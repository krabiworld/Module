package org.module.util;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.requests.restaction.MessageCreateAction;
import net.dv8tion.jda.api.utils.FileUpload;
import org.module.structure.GuildProvider;

import java.util.Date;

public class LogsUtil {
	private static GuildProvider.Manager manager = null;

	public static void setManager(GuildProvider.Manager manager) {
		LogsUtil.manager = manager;
	}

	/** Send log without file */
	public static void sendLog(Guild guild, EmbedBuilder embed) {
		sendLog(guild, embed, null);
	}

	/** Send log with file */
	public static void sendLog(Guild guild, EmbedBuilder embed, byte[] file) {
		embed.setTimestamp(new Date().toInstant());

		GuildProvider.Settings settings = manager.getSettings(guild);
		if (settings == null) return;
		TextChannel logsChannel = settings.getLogsChannel();
		if (logsChannel == null) return;

		MessageCreateAction message = logsChannel.sendMessageEmbeds(embed.build());

		if (file != null) {
			message = message.addFiles(FileUpload.fromData(file, ".txt"));
		}

		message.queue();
	}
}
