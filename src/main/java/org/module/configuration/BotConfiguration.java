package org.module.configuration;

import jakarta.annotation.PostConstruct;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.module.listeners.MemberListener;
import org.module.listeners.MessageListener;
import org.module.structure.*;
import org.module.util.LogsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BotConfiguration {
	public static JDA jda;
	public static CommandClient commandClient;
	private final Command[] commands;
	private final DiscordConfiguration configuration;
	private final GuildProvider.Manager manager;
	private final CommandListenerAdapter listener;

	@Autowired
	public BotConfiguration(
		Command[] commands,
		DiscordConfiguration configuration,
		GuildProvider.Manager manager,
		CommandListenerAdapter listener
	) {
		this.commands = commands;
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
			.setCommands(commands)
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
}
