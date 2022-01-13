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

package eu.u032.commands.information;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.JDAUtilitiesInfo;
import eu.u032.Constants;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDAInfo;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.lang.management.ManagementFactory;
import java.time.*;

public class StatsCommand extends Command {
    public StatsCommand() {
        this.name = "stats";
        this.help = "Bot statistics";
        this.category = Constants.INFORMATION;
    }

    @Override
	protected void execute(final CommandEvent event) {
		final JDA jda = event.getJDA();

        final EmbedBuilder embed = new EmbedBuilder()
			.setTitle("Bot Statistics")
			.setColor(Constants.COLOR)
			.setThumbnail(jda.getSelfUser().getEffectiveAvatarUrl())

			.addField(getCommonField(jda))
			.addField(getPlatformField(jda))
			.addField(getVersionField());
        event.reply(embed.build());
    }

    private MessageEmbed.Field getCommonField(final JDA jda) {
        long channelsCount = 0;
        for (final Guild guild : jda.getGuilds()) {
            channelsCount += guild.getChannels().size();
        }
        final String common = String.format("**Servers:** %s\n**Users:** %s\n**Channels:** %s",
			jda.getGuilds().size(), jda.getUsers().size(), channelsCount);
        return new MessageEmbed.Field("Common", common, true);
    }

    private MessageEmbed.Field getPlatformField(final JDA jda) {
        final long totalMemory = Runtime.getRuntime().totalMemory();
		final long uptime = OffsetDateTime.ofInstant(
			Instant.ofEpochMilli(ManagementFactory.getRuntimeMXBean().getStartTime()), ZoneId.systemDefault())
			.toEpochSecond();

        final String platform = String.format("**Memory Usage:** %sMB / %sMB\n**Ping:** %s ms\n**Uptime:** <t:%s:R>",
			(totalMemory - Runtime.getRuntime().freeMemory()) / 1024 / 1024,
			totalMemory / 1024 / 1024,
			jda.getGatewayPing(),
			uptime);
        return new MessageEmbed.Field("Platform", platform, true);
    }

	private MessageEmbed.Field getVersionField() {
		final String version = String.format("**Java:** %s\n**JDA:** %s\n**JDA-Utilities:** %s",
			System.getProperty("java.version"), JDAInfo.VERSION, JDAUtilitiesInfo.VERSION);
		return new MessageEmbed.Field("Version", version, true);
	}
}
