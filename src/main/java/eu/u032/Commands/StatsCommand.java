package eu.u032.Commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;

import java.awt.*;
import java.lang.management.ManagementFactory;
import java.util.concurrent.TimeUnit;

public class StatsCommand extends Command {

    public StatsCommand() {
        this.name = "stats";
    }

    @Override
    protected void execute(CommandEvent event) {
        JDA jda = event.getJDA();
        long totalMemory = Runtime.getRuntime().totalMemory();
        long freeMemory = Runtime.getRuntime().freeMemory();

        int channelsCount = 0;
        for (Guild guild : jda.getGuilds()) {
            channelsCount += guild.getChannels().size();
        }

        long uptime = ManagementFactory.getRuntimeMXBean().getUptime();
        String uptimeFormatted = String.format("%2d days %2d hours %2d minutes",
                TimeUnit.MILLISECONDS.toDays(uptime),
                TimeUnit.MILLISECONDS.toHours(uptime) % TimeUnit.DAYS.toHours(1),
                TimeUnit.MILLISECONDS.toMinutes(uptime) % TimeUnit.HOURS.toMinutes(1)
        );

        String common = String.format("**Servers:** %s\n**Users:** %s\n**Channels:** %s",
                jda.getGuilds().size(), jda.getUsers().size(), channelsCount
        );
        String platform = String.format("**RAM Usage:** %sMB / %sMB\n**Ping:** %s ms\n**Uptime:** %s",
                (totalMemory - freeMemory) / 1024 / 1024,
                totalMemory / 1024 / 1024,
                jda.getGatewayPing(),
                uptimeFormatted
        );

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("UASM Statistics")
                .setColor(Color.decode("#6196d5"))
                .addField("Common", common, true)
                .addField("Platform", platform, true)
                .setFooter("Java: " + System.getProperty("java.version"));
        event.reply(embed.build());
    }

}
