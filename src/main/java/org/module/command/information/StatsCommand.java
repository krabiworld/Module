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

package org.module.command.information;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.module.Constants;
import org.module.service.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;

@Component
public class StatsCommand extends Command {
	private final StatsService statsService;
	private final BuildProperties buildProperties;

	@Autowired
    public StatsCommand(StatsService statsService, BuildProperties buildProperties) {
		this.statsService = statsService;
		this.buildProperties = buildProperties;
        this.name = "stats";
        this.category = Constants.INFORMATION;
    }

    @Override
	protected void execute(CommandEvent event) {
		JDA jda = event.getJDA();
		long totalMemory = Runtime.getRuntime().totalMemory();
		long freeMemory = Runtime.getRuntime().freeMemory();

		String footer = String.format("Version: %s • Memory Usage: %s MB / %s MB",
			buildProperties.getVersion(),
			(totalMemory - freeMemory) / 1024 / 1024,
			totalMemory / 1024 / 1024);

		MessageEmbed embed = new EmbedBuilder()
			.setTitle("Bot Statistics")
			.setColor(Constants.DEFAULT)
			.setThumbnail(jda.getSelfUser().getEffectiveAvatarUrl())
			.setFooter(footer)

			.addField(getMainField(jda))
			.addField(getPlatformField(jda))
			.build();

        event.reply(embed);
    }

	private MessageEmbed.Field getMainField(JDA jda) {
        long channelsCount = 0;
        for (Guild guild : jda.getGuilds()) {
            channelsCount += guild.getChannels().size();
        }

        String main = String.format("**Servers:** %s\n**Users:** %s\n**Channels:** %s",
			jda.getGuilds().size(), jda.getUsers().size(), channelsCount);
        return new MessageEmbed.Field("Main", main, true);
    }

    private MessageEmbed.Field getPlatformField(JDA jda) {
		long uptime = OffsetDateTime.ofInstant(Instant
				.ofEpochMilli(ManagementFactory.getRuntimeMXBean().getStartTime()), ZoneId.systemDefault())
			.toEpochSecond();

        String platform = String.format("**Commands executed:** %s\n**Ping:** %s ms\n**Uptime:** <t:%s:R>",
			statsService.getStats().getExecutedCommands(), jda.getGatewayPing(), uptime);
        return new MessageEmbed.Field("Platform", platform, true);
    }
}
