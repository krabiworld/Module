/*
 * Module Discord Bot.
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
import eu.u032.util.MessageUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.util.Objects;

import static eu.u032.Constants.*;

public class ServerinfoCommand extends Command {
    public ServerinfoCommand() {
        this.name = MessageUtil.getMessage("command.serverinfo.name");
        this.help = MessageUtil.getMessage("command.serverinfo.help");
        this.category = INFORMATION;
    }

    @Override
    protected void execute(CommandEvent event) {
		Guild guild = event.getGuild();

        EmbedBuilder embed = new EmbedBuilder()
			.setTitle("Information about " + guild.getName())
			.setColor(COLOR)
			.setThumbnail(guild.getIconUrl())
			.setImage(guild.getBannerUrl())
			.setFooter("ID: " + guild.getId())

			.addField(getMembersField(guild))
			.addField(getChannelsField(guild))
			.addField(getByStatusField(guild))
			.addField(getOwnerField(guild))
			.addField(getVerificationLevelField(guild.getVerificationLevel()))
			.addField(getCreatedAtField(guild));
        event.reply(embed.build());
    }

    private MessageEmbed.Field getMembersField(Guild guild) {
        long botCount = 0, memberCount = 0;
        for (Member member : guild.getMembers()) {
            if (member.getUser().isBot()) botCount++;
            else memberCount++;
        }
        String members = MEMBERS + " Members: **" + memberCount + "**\n" +
			BOTS + " Bots: **" + botCount + "**";
        return new MessageEmbed.Field("Members (" + guild.getMemberCount() + ")", members, true);
    }

    private MessageEmbed.Field getChannelsField(Guild guild) {
        long channelCount = guild.getChannels().size() - guild.getCategories().size();
        StringBuilder channels = new StringBuilder();
        if (!guild.getTextChannels().isEmpty()) {
            channels.append(TEXT + " Text: **")
				.append(guild.getTextChannels().size()).append("**\n");
        }
        if (!guild.getVoiceChannels().isEmpty()) {
            channels.append(VOICE + " Voice: **")
				.append(guild.getVoiceChannels().size()).append("**\n");
        }
        if (!guild.getStageChannels().isEmpty()) {
            channels.append(STAGE + " Stage: **")
				.append(guild.getStageChannels().size()).append("**\n");
        }
        if (!guild.getStoreChannels().isEmpty()) {
            channels.append(STORE + " Store: **")
				.append(guild.getStageChannels().size()).append("**\n");
        }
        return new MessageEmbed.Field("Channels (" + channelCount + ")", channels.toString(), true);
    }

    private MessageEmbed.Field getByStatusField(Guild guild) {
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
            byStatus.append(ONLINE + "Online: **").append(online).append("**\n");
        }
        if (idle > 0) {
            byStatus.append(IDLE + "Idle: **").append(idle).append("**\n");
        }
        if (dnd > 0) {
            byStatus.append(DND + "Do Not Disturb: **").append(dnd).append("**\n");
        }
        if (offline > 0) {
            byStatus.append(OFFLINE + "Offline: **").append(offline).append("**\n");
        }
        return new MessageEmbed.Field("By Status", byStatus.toString(), true);
    }

    private MessageEmbed.Field getOwnerField(Guild guild) {
        return new MessageEmbed.Field("Owner",
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
