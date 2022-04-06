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

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.module.Constants;
import org.module.command.information.HelpCommand;
import org.module.command.information.ServerinfoCommand;
import org.module.command.information.StatsCommand;
import org.module.command.information.UserCommand;
import org.module.command.moderation.*;
import org.module.command.owner.EvalCommand;
import org.module.command.owner.OwnerCommand;
import org.module.command.settings.LangCommand;
import org.module.command.settings.LogsCommand;
import org.module.command.settings.ModRoleCommand;
import org.module.command.settings.PrefixCommand;
import org.module.command.utilities.AvatarCommand;
import org.module.command.utilities.EmojiCommand;
import org.module.command.utilities.RandomCommand;
import org.module.event.CommandEvents;
import org.module.event.MemberEvents;
import org.module.event.MessageEvents;
import org.module.manager.GuildManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.security.auth.login.LoginException;

@Configuration
public class BotConfiguration {
	@Value("${discord.token}")
	private String token;

	@Value("${discord.ownerId}")
	private String ownerId;

	public static JDA jda;
	public static CommandClient commandClient;
	private final ApplicationContext ctx;
	private final GuildManager manager;
	private final MessageEvents messageEvents;
	private final MemberEvents memberEvents;

	@Autowired
	public BotConfiguration(
		ApplicationContext ctx,
		GuildManager manager,
		MessageEvents messageEvents,
		MemberEvents memberEvents
	) {
		this.ctx = ctx;
		this.manager = manager;
		this.messageEvents = messageEvents;
		this.memberEvents = memberEvents;
	}

	@Bean
	public void configure() throws LoginException {
		JDA jda = JDABuilder
			.createDefault(token)
			.enableIntents(GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS))
			.enableCache(CacheFlag.ONLINE_STATUS, CacheFlag.ACTIVITY, CacheFlag.EMOTE)
			.disableCache(CacheFlag.VOICE_STATE)
			.setBulkDeleteSplittingEnabled(false)
			.setMemberCachePolicy(MemberCachePolicy.ALL)
			.useSharding(0, 1)
			.addEventListeners(manager, messageEvents, memberEvents)
			.build();

		CommandClient commandClient = new CommandClientBuilder()
			.setOwnerId(ownerId)
			.setPrefix(Constants.DEFAULT_PREFIX)
			.setActivity(Activity.playing("!help"))
			.setPrefixes(new String[]{"<@!" + jda.getSelfUser().getId() + "> "})
			.setEmojis("✅", null, "❌")
			.useHelpBuilder(false)
			.setGuildSettingsManager(manager)
			.addCommands(getCommands())
			.setListener(ctx.getBean(CommandEvents.class))
			.build();

		jda.addEventListener(commandClient);

		BotConfiguration.jda = jda;
		BotConfiguration.commandClient = commandClient;
	}

	private Command[] getCommands() {
		return new Command[]{
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
			ctx.getBean(OwnerCommand.class),
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
