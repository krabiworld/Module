/*
 * This file is part of Module.

 * Module is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * Module is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with Module. If not, see <https://www.gnu.org/licenses/>.
 */

package org.module.commands.information;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.command.SlashCommand;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import org.module.constants.Constants;
import org.module.service.StatsService;
import org.module.util.PropertyUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;
import java.time.*;

@Component
public class StatsCommand extends SlashCommand {
	private final StatsService statsService;

	@Value("${application.version}")
	private String version;

	@Autowired
    public StatsCommand(StatsService statsService) {
		this.statsService = statsService;
        this.name = PropertyUtil.getProperty("command.stats.name");
        this.help = PropertyUtil.getProperty("command.stats.help");
        this.category = Constants.INFORMATION;
    }

    @Override
	protected void execute(CommandEvent event) {
        event.reply(command(event.getJDA()));
    }

	@Override
	protected void execute(SlashCommandEvent event) {
		event.replyEmbeds(command(event.getJDA())).queue();
	}

	private MessageEmbed command(JDA jda) {
		return new EmbedBuilder()
			.setTitle("Bot Statistics")
			.setColor(Constants.COLOR)
			.setThumbnail(jda.getSelfUser().getEffectiveAvatarUrl())
			.setFooter("Version: " + version)

			.addField(getMainField(jda))
			.addField(getPlatformField(jda))
			.build();
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
			statsService.getStats().getCommandsExecuted(), jda.getGatewayPing(), uptime);
        return new MessageEmbed.Field("Platform", platform, true);
    }
}
