/*
 * Module Discord Bot.
 * Copyright (C) 2022 untled032, Headcrab

 * Module is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * Module is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with Module. If not, see https://www.gnu.org/licenses/.
 */

package org.module.configuration;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.api.JDA;
import org.module.commands.information.*;
import org.module.commands.moderation.*;
import org.module.commands.owner.*;
import org.module.commands.settings.*;
import org.module.commands.utilities.*;
import org.module.constants.Constants;
import org.module.events.MemberEvent;
import org.module.events.MessageEvent;
import org.module.events.CommandsEvent;
import org.module.manager.GuildManager;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.security.auth.login.LoginException;

@Configuration
public class JDAConfiguration {
	private final Logger logger = LoggerFactory.getLogger(JDAConfiguration.class);

	@Autowired
	private ApplicationContext ctx;

	@Value("${discord.token}")
	private String token;

	@Value("${discord.owner}")
	private String owner;

	@Bean
	public JDA jda(ApplicationContext ctx, GuildManager manager) throws LoginException {
		EventWaiter eventWaiter = new EventWaiter();
		CommandClient builder = new CommandClientBuilder()
			.setOwnerId(owner)
			.setPrefix(Constants.PREFIX)
			.setActivity(null)
			.setEmojis("✅", "⚠", "❌")
			.useHelpBuilder(false)
			.setGuildSettingsManager(manager)
			.addCommands(
				// Information
				get(HelpCommand.class),
				get(ServerinfoCommand.class),
				get(StatsCommand.class),
				get(UserCommand.class),
				// Moderation
				get(BanCommand.class),
				get(ClearCommand.class),
				get(KickCommand.class),
				get(MuteCommand.class),
				get(RemwarnCommand.class),
				get(SlowmodeCommand.class),
				get(UnbanCommand.class),
				get(UnmuteCommand.class),
				get(WarnCommand.class),
				get(WarnsCommand.class),
				// Owner
				get(EvalCommand.class),
				get(OwnerCommand.class),
				// Settings
				get(LogsCommand.class),
				get(ModRoleCommand.class),
				get(MuteRoleCommand.class),
				get(PrefixCommand.class),
				// Utilities
				get(AvatarCommand.class),
				get(EmojiCommand.class),
				get(RandomCommand.class)
			)
			.setListener(ctx.getBean(CommandsEvent.class))
			.build();

		logger.info("Loaded " + builder.getCommands().size() + " commands");

		return JDABuilder
			.createDefault(token)
			.enableIntents(GatewayIntent.GUILD_MEMBERS,
				GatewayIntent.GUILD_MESSAGES,
				GatewayIntent.GUILD_PRESENCES,
				GatewayIntent.DIRECT_MESSAGES)
			.enableCache(CacheFlag.ONLINE_STATUS, CacheFlag.ACTIVITY, CacheFlag.EMOTE)
			.disableCache(CacheFlag.VOICE_STATE)
			.setBulkDeleteSplittingEnabled(false)
			.setMemberCachePolicy(MemberCachePolicy.ALL)
			.useSharding(0, 1)
			.addEventListeners(eventWaiter, builder, manager,
				ctx.getBean(MemberEvent.class), ctx.getBean(MessageEvent.class))
			.build();
	}

	/** Alias to {@link ApplicationContext#getBean(Class)}. */
	private Command get(Class<? extends Command> command) {
		return ctx.getBean(command);
	}
}
