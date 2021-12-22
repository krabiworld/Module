package eu.u032.Commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;

import java.awt.*;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

public class StatsCommand extends Command {

    public StatsCommand() {
        this.name = "stats";
        this.help = "Bot statistics";
        this.category = new Category("Information");
    }

    @Override
    protected void execute(CommandEvent event) {
        JDA jda = event.getJDA();
        RuntimeMXBean mxBean = ManagementFactory.getRuntimeMXBean();
        Runtime runtime = Runtime.getRuntime();

        String common = String.format("**Servers:** %s\n**Users:** %s",
                jda.getGuilds().size(), jda.getUsers().size()
        );

        String platform = String.format("**OS:** %s\n**Architecture:** %s\n**RAM Usage:** %sMB / %sMB\n**Ping:** %s ms\n**Uptime:** %s m",
                System.getProperty("os.name"),
                System.getProperty("os.arch"),
                (runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024,
                runtime.totalMemory() / 1024 / 1024,
                jda.getGatewayPing(),
                (mxBean.getUptime() / 1000) / 60
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
