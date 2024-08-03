package org.module.command.information;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.module.Constants;
import org.module.service.StatsService;
import org.module.structure.Category;
import org.module.structure.Command;
import org.module.structure.CommandContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;

@Component
public class StatsCommand extends Command {
	private final StatsService statsService;

	@Autowired
    public StatsCommand(StatsService statsService) {
		this.name = "stats";
		this.description = "Bot statistics";
		this.category = Category.INFORMATION;
		this.statsService = statsService;
    }

    @Override
	protected void execute(CommandContext ctx) {
		JDA jda = ctx.getJDA();

		MessageEmbed embed = new EmbedBuilder()
			.setTitle(this.getDescription())
			.setColor(Constants.DEFAULT)
			.setThumbnail(jda.getSelfUser().getEffectiveAvatarUrl())
			.addField(getMainField(jda))
			.addField(getPlatformField(jda))
			.build();

        ctx.reply(embed);
    }

	private MessageEmbed.Field getMainField(JDA jda) {
		long channelsCount = jda.getGuilds().stream().mapToLong(g -> g.getChannels().size()).sum();

        String main = String.format("**Servers:** %s\n**Users:** %s\n**Channels:** %s",
			jda.getGuilds().size(), jda.getUsers().size(), channelsCount);
        return new MessageEmbed.Field("Main", main, true);
    }

    private MessageEmbed.Field getPlatformField(JDA jda) {
		long uptime = OffsetDateTime.ofInstant(Instant.ofEpochMilli(
			ManagementFactory.getRuntimeMXBean().getStartTime()), ZoneId.systemDefault()
		).toEpochSecond();

        String platform = String.format("**Executed commands:** %s\n**Ping:** %s ms\n**Uptime:** <t:%s:R>",
			statsService.getStats().getExecutedCommands(), jda.getGatewayPing(), uptime);
        return new MessageEmbed.Field("Platform", platform, true);
    }
}
