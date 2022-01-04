package eu.u032.Commands.Information;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import eu.u032.Utils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDAInfo;
import net.dv8tion.jda.api.entities.Guild;

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
        long totalMemory = Runtime.getRuntime().totalMemory();

        int channelsCount = 0;
        for (Guild guild : jda.getGuilds()) {
            channelsCount += guild.getChannels().size();
        }

        long uptime = ManagementFactory.getRuntimeMXBean().getUptime();
        String uptimeFormatted = String.format("%2d days, %2d hours, %2d min.",
                TimeUnit.MILLISECONDS.toDays(uptime),
                TimeUnit.MILLISECONDS.toHours(uptime) % TimeUnit.DAYS.toHours(1),
                TimeUnit.MILLISECONDS.toMinutes(uptime) % TimeUnit.HOURS.toMinutes(1));

        String common = String.format("**Servers:** %s\n**Users:** %s\n**Channels:** %s",
                jda.getGuilds().size(), jda.getUsers().size(), channelsCount);
        String platform = String.format("**Memory Usage:** %sMB / %sMB\n**Ping:** %s ms\n**Uptime:** %s",
                (totalMemory - Runtime.getRuntime().freeMemory()) / 1024 / 1024,
                totalMemory / 1024 / 1024,
                jda.getGatewayPing(),
                uptimeFormatted);

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Bot Statistics")
                .setColor(Utils.getColor())
                .addField("Common", common, true)
                .addField("Platform", platform, true)
                .setThumbnail(jda.getSelfUser().getEffectiveAvatarUrl())
                .setFooter(Utils.getCopyright() + " • Java: " + System.getProperty("java.version") + " • JDA: " + JDAInfo.VERSION);
        event.reply(embed.build());
    }
}
