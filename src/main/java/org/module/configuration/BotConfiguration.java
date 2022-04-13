/*
 * This file is part of Module.
 *
 * Module is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Module is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Module. If not, see <https://www.gnu.org/licenses/>.
 */

package org.module.configuration;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.module.command.information.HelpCommand;
import org.module.command.information.ServerinfoCommand;
import org.module.command.information.StatsCommand;
import org.module.command.information.UserCommand;
import org.module.command.moderation.*;
import org.module.command.owner.EvalCommand;
import org.module.command.settings.LangCommand;
import org.module.command.settings.LogsCommand;
import org.module.command.settings.ModRoleCommand;
import org.module.command.settings.PrefixCommand;
import org.module.command.utilities.AvatarCommand;
import org.module.command.utilities.EmojiCommand;
import org.module.command.utilities.RandomCommand;
import org.module.listeners.MemberListener;
import org.module.listeners.MessageListener;
import org.module.structure.*;
import org.module.util.LogsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.security.auth.login.LoginException;

@Configuration
public class BotConfiguration {
	public static JDA jda;
	public static CommandClient commandClient;
	private final ApplicationContext ctx;
	private final DiscordConfiguration configuration;
	private final GuildManagerProvider manager;
	private final CommandListenerAdapter listener;

	@Autowired
	public BotConfiguration(
		ApplicationContext ctx,
		DiscordConfiguration configuration,
		GuildManagerProvider manager,
		CommandListenerAdapter listener
	) {
		this.ctx = ctx;
		this.configuration = configuration;
		this.manager = manager;
		this.listener = listener;
	}

	@Bean
	public void configure() throws LoginException {
		LogsUtil.setManager(manager);

		commandClient = CommandClientBuilder
			.builder()
			.setOwnerId(configuration.getOwnerId())
			.setCommands(getCommands())
			.setGuildManager(manager)
			.setListener(listener)
			.build();
		jda = JDABuilder
			.createDefault(configuration.getToken())
			.setActivity(Activity.playing("!help"))
			.enableIntents(GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS))
			.enableCache(CacheFlag.ONLINE_STATUS, CacheFlag.ACTIVITY, CacheFlag.EMOTE)
			.disableCache(CacheFlag.VOICE_STATE)
			.setBulkDeleteSplittingEnabled(false)
			.setMemberCachePolicy(MemberCachePolicy.ALL)
			.useSharding(0, 1)
			.addEventListeners(commandClient, manager, new MessageListener(), new MemberListener())
			.build();
	}

	private AbstractCommand[] getCommands() {
		return new AbstractCommand[]{
			// Information
			ctx.getBean(HelpCommand.class),
			ctx.getBean(ServerinfoCommand.class),
			ctx.getBean(StatsCommand.class),
			ctx.getBean(UserCommand.class),
			// Moderation
			ctx.getBean(BanCommand.class),
			ctx.getBean(ClearCommand.class),
			ctx.getBean(KickCommand.class),
			ctx.getBean(MuteCommand.class),
			ctx.getBean(RemwarnCommand.class),
			ctx.getBean(SlowmodeCommand.class),
			ctx.getBean(UnbanCommand.class),
			ctx.getBean(UnmuteCommand.class),
			ctx.getBean(WarnCommand.class),
			ctx.getBean(WarnsCommand.class),
			// Owner
			ctx.getBean(EvalCommand.class),
			// Settings
			ctx.getBean(LogsCommand.class),
			ctx.getBean(ModRoleCommand.class),
			ctx.getBean(LangCommand.class),
			ctx.getBean(PrefixCommand.class),
			// Utilities
			ctx.getBean(AvatarCommand.class),
			ctx.getBean(EmojiCommand.class),
			ctx.getBean(RandomCommand.class)
		};
	}
}
