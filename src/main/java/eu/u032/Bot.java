package eu.u032;

import com.jagrosh.jdautilities.command.CommandClientBuilder;
import eu.u032.commands.utilities.EmoteCommand;
import eu.u032.logging.ChannelEvents;
import eu.u032.logging.InviteEvents;
import eu.u032.logging.MemberEvents;
import eu.u032.logging.MessageEvents;
import eu.u032.commands.*;
import eu.u032.commands.information.*;
import eu.u032.commands.moderation.*;
import eu.u032.commands.utilities.AvatarCommand;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;

public class Bot {
    public static void main(String[] args) throws LoginException {
        CommandClientBuilder builder = new CommandClientBuilder()
                .setOwnerId(Config.getString("OWNER_ID"))
                .setPrefix(Config.getString("PREFIX"))
                .setActivity(Activity.competing("JDA"))
                .setStatus(OnlineStatus.IDLE)
                .setEmojis("✅", "⚠️", "❌")
                .useHelpBuilder(false)
                .addCommands(
                        new EvalCommand(),
                        // Information
                        new ServerinfoCommand(),
                        new UserCommand(),
                        new StatsCommand(),
                        new HelpCommand(),
                        // Moderation
                        new MuteCommand(),
                        new UnmuteCommand(),
                        new ClearCommand(),
                        new SlowmodeCommand(),
                        new KickCommand(),
                        new BanCommand(),
                        new UnbanCommand(),
                        // Utilities
                        new AvatarCommand(),
                        new EmoteCommand()
                );

        JDABuilder
                .createDefault(Config.getString("TOKEN"),
                        GatewayIntent.GUILD_MEMBERS,
                        GatewayIntent.GUILD_MESSAGES,
                        GatewayIntent.GUILD_INVITES,
                        GatewayIntent.GUILD_PRESENCES,
                        GatewayIntent.DIRECT_MESSAGES,
                        GatewayIntent.GUILD_EMOJIS)
                .enableCache(CacheFlag.ONLINE_STATUS, CacheFlag.ACTIVITY, CacheFlag.EMOTE)
                .disableCache(CacheFlag.VOICE_STATE)
                .setBulkDeleteSplittingEnabled(false)
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .addEventListeners(builder.build(),
                        new InviteEvents(),
                        new ChannelEvents(),
                        new MemberEvents(),
                        new MessageEvents())
                .useSharding(0, 1)
                .build();
    }
}
