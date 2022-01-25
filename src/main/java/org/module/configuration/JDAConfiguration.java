/*
 * This file is part of Module.

 * Module is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * Module is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with Module. If not, see <https://www.gnu.org/licenses/>.
 */

package org.module.configuration;

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
import org.module.events.message.MessageReactionAdd;
import org.module.events.member.*;
import org.module.events.message.MessageBulkDelete;
import org.module.events.command.Command;
import org.module.events.message.MessageDelete;
import org.module.events.message.MessageReceived;
import org.module.events.message.MessageUpdate;
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
	private final ApplicationContext ctx;
	private final GuildManager manager;

	@Value("${discord.token}")
	private String token;

	@Value("${discord.owner}")
	private String owner;

	@Autowired
	public JDAConfiguration(ApplicationContext ctx, GuildManager manager) {
		this.ctx = ctx;
		this.manager = manager;
	}

	@Bean
	public JDA jda() throws LoginException {
		EventWaiter eventWaiter = new EventWaiter();
		CommandClient builder = new CommandClientBuilder()
			.setOwnerId(owner)
			.setPrefix(Constants.DEFAULT_PREFIX)
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
			.setListener(get(Command.class))
			.build();

		logger.info("Added " + builder.getCommands().size() + " commands.");

		JDA jda = JDABuilder
			.createDefault(token)
			.enableIntents(GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS))
			.enableCache(CacheFlag.ONLINE_STATUS, CacheFlag.ACTIVITY, CacheFlag.EMOTE)
			.disableCache(CacheFlag.VOICE_STATE)
			.setBulkDeleteSplittingEnabled(false)
			.setMemberCachePolicy(MemberCachePolicy.ALL)
			.useSharding(0, 1)
			.addEventListeners(eventWaiter, builder, manager,
				// Member
				get(MemberUnban.class),
				get(MemberBan.class),
				get(MemberJoin.class),
				get(MemberRemove.class),
				get(MemberRoleAdd.class),
				get(MemberRoleRemove.class),
				get(MemberUpdateNickname.class),
				// Message
				get(MessageBulkDelete.class),
				get(MessageUpdate.class),
				get(MessageDelete.class),
				get(MessageReceived.class),
				get(MessageReactionAdd.class))
			.build();

		logger.info("Added " + jda.getRegisteredListeners().size() + " events.");
		return jda;
	}

	/** Alias to {@link ApplicationContext#getBean(Class)}. */
	private <T> T get(Class<? extends T> clazz) {
		return ctx.getBean(clazz);
	}
}
