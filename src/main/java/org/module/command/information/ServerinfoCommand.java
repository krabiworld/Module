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
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.module.Locale;
import org.module.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static org.module.Constants.*;

@Component
public class ServerinfoCommand extends Command {
	private final MessageService messageService;

	@Autowired
	public ServerinfoCommand(MessageService messageService) {
		this.messageService = messageService;
        this.name = "serverinfo";
        this.category = INFORMATION;
    }

    @Override
    protected void execute(CommandEvent event) {
		Guild guild = event.getGuild();
		Locale locale = messageService.getLocale(guild);
		MessageEmbed embed = new EmbedBuilder()
			.setTitle(locale.get("command.serverinfo.title", guild.getName()))
			.setColor(DEFAULT)
			.setThumbnail(guild.getIconUrl())
			.setImage(guild.getBannerUrl())
			.setFooter("ID: " + guild.getId())

			.addField(getMembersField(locale, guild))
			.addField(getChannelsField(locale, guild))
			.addField(getByStatusField(locale, guild))
			.addField(getOwnerField(guild))
			.addField(getVerificationLevelField(guild.getVerificationLevel()))
			.addField(getCreatedAtField(guild))
			.build();

		event.reply(embed);
    }

	private MessageEmbed.Field getMembersField(Locale locale, Guild guild) {
        long botCount = 0, memberCount = 0;
        for (Member member : guild.getMembers()) {
            if (member.getUser().isBot()) botCount++;
            else memberCount++;
        }
		String members = locale.get("command.serverinfo.members", memberCount, botCount);
        return new MessageEmbed.Field(
			locale.get("command.serverinfo.members.title", guild.getMemberCount()),
			members, true);
    }

    private MessageEmbed.Field getChannelsField(Locale locale, Guild guild) {
        long channelCount = guild.getChannels().size() - guild.getCategories().size();
        StringBuilder channels = new StringBuilder();
        if (!guild.getTextChannels().isEmpty()) {
            channels.append(locale.get("command.serverinfo.text"))
				.append(guild.getTextChannels().size()).append("**\n");
        }
        if (!guild.getVoiceChannels().isEmpty()) {
            channels.append(locale.get("command.serverinfo.voice"))
				.append(guild.getVoiceChannels().size()).append("**\n");
        }
        if (!guild.getStageChannels().isEmpty()) {
            channels.append(locale.get("command.serverinfo.stage"))
				.append(guild.getStageChannels().size()).append("**\n");
        }
        return new MessageEmbed.Field(
			locale.get("command.serverinfo.channels", channelCount),
			channels.toString(), true);
    }

    private MessageEmbed.Field getByStatusField(Locale locale, Guild guild) {
        long online = 0, dnd = 0, idle = 0, offline = 0;
		StringBuilder byStatus = new StringBuilder();

		for (Member member : guild.getMembers()) {
            switch (member.getOnlineStatus()) {
                case ONLINE -> online++;
                case OFFLINE, INVISIBLE -> offline++;
                case IDLE -> idle++;
                case DO_NOT_DISTURB -> dnd++;
            }
		}

		if (online > 0) {
            byStatus.append(ONLINE)
				.append(locale.get("command.serverinfo.online"))
				.append(online).append("**\n");
        }
        if (idle > 0) {
            byStatus.append(IDLE)
				.append(locale.get("command.serverinfo.idle"))
				.append(idle).append("**\n");
        }
        if (dnd > 0) {
            byStatus.append(DND)
				.append(locale.get("command.serverinfo.dnd"))
				.append(dnd).append("**\n");
        }
        if (offline > 0) {
            byStatus.append(OFFLINE)
				.append(locale.get("command.serverinfo.offline"))
				.append(offline).append("**\n");
        }
        return new MessageEmbed.Field(
			locale.get("command.serverinfo.byStatus"),
			byStatus.toString(), true);
    }

    private MessageEmbed.Field getOwnerField(Guild guild) {
        return new MessageEmbed.Field("OwnerModel",
			Objects.requireNonNull(guild.getOwner()).getUser().getAsMention(), true);
    }

    private MessageEmbed.Field getVerificationLevelField(Guild.VerificationLevel level) {
        String verificationLevel = level.name().charAt(0) + level.name().substring(1).toLowerCase()
			.replace("_", " ");
        return new MessageEmbed.Field("Verification Level", verificationLevel, true);
    }

    private MessageEmbed.Field getCreatedAtField(Guild guild) {
        long timeCreated = guild.getTimeCreated().toEpochSecond();
        String createdAt = "<t:" + timeCreated + ":D> (<t:" + timeCreated + ":R>)";
        return new MessageEmbed.Field("Created at", createdAt, true);
    }
}
