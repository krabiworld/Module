package eu.u032;

import eu.u032.Commands.*;
import eu.u032.Utils.Config;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

import com.jagrosh.jdautilities.command.CommandClientBuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;

public class Bot {
    public static void main(String[] args) throws LoginException {
        CommandClientBuilder utils = new CommandClientBuilder()
                .setOwnerId(Config.getString("OWNER_ID"))
                .setPrefix(Config.getString("PREFIX"))
                .setActivity(Activity.competing("JDA 4.4.0_350"))
                .setStatus(OnlineStatus.IDLE)
                .useHelpBuilder(false);
        utils.addCommands(
                new GuildCommand()
        );

        JDA jda = JDABuilder
            .createDefault(Config.getString("DISCORD_TOKEN"),
                    GatewayIntent.GUILD_MEMBERS,
                    GatewayIntent.GUILD_MESSAGES,
                    GatewayIntent.GUILD_INVITES,
                    GatewayIntent.GUILD_EMOJIS)
            .setActivity(Activity.competing("titled"))
            .addEventListeners(
                    new Events(),
                    utils.build()
            )
            .build();
    }

}
