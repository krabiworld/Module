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
import eu.u032.Constants;
import eu.u032.util.MessageUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.lang.management.ManagementFactory;
import java.time.*;

public class StatsCommand extends Command {
    public StatsCommand() {
        this.name = MessageUtil.getMessage("command.stats.name");
        this.help = MessageUtil.getMessage("command.stats.help");
        this.category = Constants.INFORMATION;
    }

    @Override
	protected void execute(final CommandEvent event) {
		final JDA jda = event.getJDA();

        final EmbedBuilder embed = new EmbedBuilder()
			.setTitle("Bot Statistics")
			.setColor(Constants.COLOR)
			.setThumbnail(jda.getSelfUser().getEffectiveAvatarUrl())

			.addField(getMainField(jda))
			.addField(getPlatformField(jda));
        event.reply(embed.build());
    }

    private MessageEmbed.Field getMainField(final JDA jda) {
        long channelsCount = 0;

        for (final Guild guild : jda.getGuilds()) {
            channelsCount += guild.getChannels().size();
        }
        final String common = String.format("**Servers:** %s\n**Users:** %s\n**Channels:** %s",
			jda.getGuilds().size(), jda.getUsers().size(), channelsCount);
        return new MessageEmbed.Field("Main", common, true);
    }

    private MessageEmbed.Field getPlatformField(final JDA jda) {
		final long uptime = OffsetDateTime.ofInstant(
			Instant.ofEpochMilli(ManagementFactory.getRuntimeMXBean().getStartTime()), ZoneId.systemDefault())
			.toEpochSecond();

        final String platform = String.format("**Ping:** %s ms\n**Uptime:** <t:%s:R>",
			jda.getGatewayPing(), uptime);
        return new MessageEmbed.Field("Platform", platform, true);
    }
}
