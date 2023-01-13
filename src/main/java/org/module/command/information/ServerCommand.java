package org.module.command.information;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.module.structure.Category;
import org.module.structure.Command;
import org.module.structure.CommandContext;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

import static org.module.Constants.*;

@Component
public class ServerCommand extends Command {
	public ServerCommand() {
		this.name = "server";
		this.description = "Server information";
		this.category = Category.INFORMATION;
	}

    @Override
    protected void execute(CommandContext ctx) {
		Guild guild = ctx.getGuild();
		Member owner = guild.getOwner();

		if (owner == null) return;

		MessageEmbed embed = new EmbedBuilder()
			.setTitle(MessageFormat.format("Information about {0}", guild.getName()))
			.setColor(DEFAULT)
			.setThumbnail(guild.getIconUrl())
			.setImage(guild.getBannerUrl())
			.setFooter("ID: " + guild.getId())
			.addField(getMembersField(guild))
			.addField(getChannelsField(guild))
			.addField(getByStatusField(guild))
			.addField(getOwnerField(owner))
			.addField(getVerificationLevelField(guild.getVerificationLevel()))
			.addField(getCreatedAtField(guild))
			.build();

		ctx.reply(embed);
    }

	private MessageEmbed.Field getMembersField(Guild guild) {
        long botCount = 0, memberCount = 0;
        for (Member member : guild.getMembers()) {
            if (member.getUser().isBot()) botCount++;
            else memberCount++;
        }
		String members = MessageFormat.format("Members: **{0}**\nBots: **{1}**", memberCount, botCount);
        return new MessageEmbed.Field(
			MessageFormat.format("Members ({0})", guild.getMemberCount()),
			members,
			true
		);
    }

    private MessageEmbed.Field getChannelsField(Guild guild) {
        long channelCount = guild.getChannels().size() - guild.getCategories().size();
        StringBuilder channels = new StringBuilder();
        if (!guild.getTextChannels().isEmpty()) {
			channels.append(
				MessageFormat.format("Text: **{0}**", guild.getTextChannels().size())
			).append("\n");
        }
        if (!guild.getVoiceChannels().isEmpty()) {
			channels.append(
				MessageFormat.format("Voice: **{0}**", guild.getVoiceChannels().size())
			).append("\n");
        }
        if (!guild.getStageChannels().isEmpty()) {
			channels.append(
				MessageFormat.format("Stage: **{0}**", guild.getStageChannels().size())
			).append("\n");
        }
        return new MessageEmbed.Field(
			MessageFormat.format("Channels ({0})", channelCount),
			channels.toString(), true);
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
            byStatus.append(ONLINE)
				.append(MessageFormat.format("Online: **{0}**", online))
				.append("\n");
        }
        if (idle > 0) {
            byStatus.append(IDLE)
				.append(MessageFormat.format("Idle: **{0}**", idle))
				.append("\n");
        }
        if (dnd > 0) {
            byStatus.append(DND)
				.append(MessageFormat.format("Do Not Disturb: **{0}**", dnd))
				.append("\n");
        }
        if (offline > 0) {
            byStatus.append(OFFLINE)
				.append(MessageFormat.format("Offline: **{0}**", offline))
				.append("\n");
        }
        return new MessageEmbed.Field(
			"By Status",
			byStatus.toString(),
			true);
    }

    private MessageEmbed.Field getOwnerField(Member owner) {
        return new MessageEmbed.Field(
			"Owner",
			owner.getUser().getAsMention(),
			true
		);
    }

    private MessageEmbed.Field getVerificationLevelField(Guild.VerificationLevel level) {
        String verificationLevel = level.name().charAt(0) + level.name().substring(1).toLowerCase()
			.replace("_", " ");
        return new MessageEmbed.Field(
			"Verification Level",
			verificationLevel,
			true
		);
    }

    private MessageEmbed.Field getCreatedAtField(Guild guild) {
        long timeCreated = guild.getTimeCreated().toEpochSecond();
		String createdAt = MessageFormat.format("<t:{0,number,#}:D> (<t:{0,number,#}:R>)", timeCreated);
        return new MessageEmbed.Field(
			"Created at",
			createdAt,
			true
		);
    }
}
