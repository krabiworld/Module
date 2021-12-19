package eu.u032;

import com.jagrosh.jdautilities.examples.command.*;
import eu.u032.Commands.*;
import eu.u032.Interactions.VerifyButton;
import eu.u032.Utils.Config;
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
                .setEmojis("U+2705", "U+26A0", "U+274C")
                .useHelpBuilder(false);
        utils.addCommands(
                new GuildCommand(), new MuteCommand(), new UnmuteCommand(),
                new ClearCommand(), new SlowmodeCommand(), new UserCommand(),
                new AvatarCommand(), new KickCommand(),
                // From JDA Utilities
                new PingCommand(), new RoleinfoCommand(), new ShutdownCommand()
        );

        if(System.getProperty("os.name").startsWith("Windows")) return;

        JDABuilder
                .createDefault(Config.getString("DISCORD_TOKEN"),
                        GatewayIntent.GUILD_MEMBERS,
                        GatewayIntent.GUILD_MESSAGES,
                        GatewayIntent.GUILD_INVITES,
                        GatewayIntent.GUILD_PRESENCES,
                        GatewayIntent.GUILD_EMOJIS)
                .setActivity(Activity.competing("titled"))
                .addEventListeners(
                        new Events(),
                        new VerifyButton(),
                        utils.build()
                )
                .build();


    }

}
