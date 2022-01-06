package eu.u032.commands.information;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import eu.u032.Utils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDAInfo;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.lang.management.ManagementFactory;
import java.util.concurrent.TimeUnit;

public class StatsCommand extends Command {
    public StatsCommand() {
        this.name = "stats";
        this.help = "Bot statistics";
        this.category = new Category("Information");
    }

    @Override
    protected void execute(CommandEvent event) {
        JDA jda = event.getJDA();
        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Bot Statistics")
                .setColor(Utils.getColor())
                .setThumbnail(jda.getSelfUser().getEffectiveAvatarUrl())
                .setFooter(Utils.getCopyright() +
                        " • Java: " + System.getProperty("java.version") +
                        " • JDA: " + JDAInfo.VERSION)

                .addField(getCommonField(jda))
                .addField(getPlatformField(jda));
        event.reply(embed.build());
    }

    private MessageEmbed.Field getCommonField(JDA jda) {
        long channelsCount = 0;
        for (Guild guild : jda.getGuilds()) {
            channelsCount += guild.getChannels().size();
        }
        String common = String.format("**Servers:** %s\n**Users:** %s\n**Channels:** %s",
                jda.getGuilds().size(), jda.getUsers().size(), channelsCount);
        return new MessageEmbed.Field("Common", common, true);
    }

    private MessageEmbed.Field getPlatformField(JDA jda) {
        long totalMemory = Runtime.getRuntime().totalMemory();
        long uptime = ManagementFactory.getRuntimeMXBean().getUptime();

        String uptimeFormatted = String.format("%2d days, %2d hours, %2d min.",
                TimeUnit.MILLISECONDS.toDays(uptime),
                TimeUnit.MILLISECONDS.toHours(uptime) % TimeUnit.DAYS.toHours(1),
                TimeUnit.MILLISECONDS.toMinutes(uptime) % TimeUnit.HOURS.toMinutes(1));

        String platform = String.format("**Memory Usage:** %sMB / %sMB\n**Ping:** %s ms\n**Uptime:** %s",
                (totalMemory - Runtime.getRuntime().freeMemory()) / 1024 / 1024,
                totalMemory / 1024 / 1024,
                jda.getGatewayPing(),
                uptimeFormatted);
        return new MessageEmbed.Field("Platform", platform, true);
    }
}
