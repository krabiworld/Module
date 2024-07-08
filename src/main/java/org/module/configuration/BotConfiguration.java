package org.module.configuration;

import jakarta.annotation.PostConstruct;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.module.command.EvalCommand;
import org.module.command.information.*;
import org.module.command.moderation.*;
import org.module.command.music.PlayCommand;
import org.module.command.music.PauseCommand;
import org.module.command.music.ResumeCommand;
import org.module.command.music.SkipCommand;
import org.module.command.settings.*;
import org.module.command.utilities.*;
import org.module.listeners.MemberListener;
import org.module.listeners.MessageListener;
import org.module.structure.*;
import org.module.util.LogsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BotConfiguration {
	public static JDA jda;
	public static CommandClient commandClient;
	private final ApplicationContext ctx;
	private final DiscordConfiguration configuration;
	private final GuildProvider.Manager manager;
	private final CommandListenerAdapter listener;

	@Autowired
	public BotConfiguration(
		ApplicationContext ctx,
		DiscordConfiguration configuration,
		GuildProvider.Manager manager,
		CommandListenerAdapter listener
	) {
		this.ctx = ctx;
		this.configuration = configuration;
		this.manager = manager;
		this.listener = listener;
	}

	@PostConstruct
	public void configure() {
		LogsUtil.setManager(manager);

		commandClient = CommandClientBuilder
			.builder()
			.setOwnerId(configuration.getOwnerId())
			.forceGuildOnly(configuration.getGuildId())
			.setCommands(getCommands())
			.setGuildManager(manager)
			.setListener(listener)
			.build();

		jda = JDABuilder
			.createDefault(configuration.getToken())
			.setActivity(Activity.playing("/help"))
			.enableIntents(GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS))
			.enableCache(CacheFlag.ONLINE_STATUS, CacheFlag.ACTIVITY, CacheFlag.EMOJI, CacheFlag.VOICE_STATE)
			.setBulkDeleteSplittingEnabled(false)
			.setMemberCachePolicy(MemberCachePolicy.ALL)
			.useSharding(0, 1)
			.addEventListeners(
				commandClient,
				manager,
				new MessageListener(),
				new MemberListener()
			).build();
	}

	private Command[] getCommands() {
		return new Command[]{
			// Information
			ctx.getBean(HelpCommand.class),
			ctx.getBean(ServerCommand.class),
			ctx.getBean(StatsCommand.class),
			ctx.getBean(UserCommand.class),
			// Moderation
			ctx.getBean(ClearCommand.class),
			ctx.getBean(RemwarnCommand.class),
			ctx.getBean(SlowmodeCommand.class),
			ctx.getBean(WarnCommand.class),
			ctx.getBean(WarnsCommand.class),
			ctx.getBean(BanCommand.class),
			ctx.getBean(KickCommand.class),
			ctx.getBean(MuteCommand.class),
			ctx.getBean(UnbanCommand.class),
			ctx.getBean(UnmuteCommand.class),
			// Settings
			ctx.getBean(LogsCommand.class),
			ctx.getBean(ModRoleCommand.class),
			// Music
			ctx.getBean(PlayCommand.class),
			ctx.getBean(PauseCommand.class),
			ctx.getBean(ResumeCommand.class),
			ctx.getBean(SkipCommand.class),
			// Utilities
			ctx.getBean(AvatarCommand.class),
			ctx.getBean(EmojiCommand.class),
			ctx.getBean(RandomCommand.class),
			// Owner
			ctx.getBean(EvalCommand.class)
		};
	}
}
