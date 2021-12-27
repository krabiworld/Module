package eu.u032;

import eu.u032.BotEvents.ChannelEvents;
import eu.u032.BotEvents.InviteEvents;
import eu.u032.BotEvents.MemberEvents;
import eu.u032.BotEvents.MessageEvents;
import eu.u032.Commands.*;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;

public class Bot {

    public static void main(String[] args) throws LoginException {
        CommandClientBuilder builder = new CommandClientBuilder()
                .setOwnerId(Config.getString("OWNER_ID"))
                .setPrefix(Config.getString("PREFIX"))
                .setAlternativePrefix(Config.getString("ALT_PREFIX"))
                .setActivity(Activity.competing("JDA"))
                .setStatus(OnlineStatus.IDLE)
                .setEmojis("✅", "⚠️", "❌")
                .setHelpConsumer(Utils::help)
                .addCommands(
                        new ServerinfoCommand(), new MuteCommand(), new UnmuteCommand(),
                        new ClearCommand(), new SlowmodeCommand(), new UserCommand(),
                        new AvatarCommand(), new KickCommand(), new StatsCommand(),
                        new ShutdownCommand(), new BanCommand(), new UnbanCommand()
                );

        JDABuilder
                .createDefault(Config.getString("DISCORD_TOKEN"),
                        GatewayIntent.GUILD_MEMBERS,
                        GatewayIntent.GUILD_MESSAGES,
                        GatewayIntent.GUILD_INVITES,
                        GatewayIntent.GUILD_PRESENCES,
                        GatewayIntent.GUILD_EMOJIS,
                        GatewayIntent.DIRECT_MESSAGES)
                .enableCache(CacheFlag.ONLINE_STATUS, CacheFlag.CLIENT_STATUS)
                .disableCache(CacheFlag.VOICE_STATE)
                .setBulkDeleteSplittingEnabled(false)
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .addEventListeners(builder.build(),
                        new InviteEvents(),
                        new ChannelEvents(),
                        new MemberEvents(),
                        new MessageEvents())
                .build();
    }

}
