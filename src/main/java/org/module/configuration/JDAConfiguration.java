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

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.module.constants.Constants;
import org.module.events.CommandEvents;
import org.module.manager.GuildManager;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.security.auth.login.LoginException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.reflections.scanners.Scanners.SubTypes;

@Configuration
public class JDAConfiguration {
	private final Logger logger = LoggerFactory.getLogger(JDAConfiguration.class);
	private final ApplicationContext ctx;

	@Autowired
	public JDAConfiguration(ApplicationContext ctx, GuildManager manager, DiscordConfig config) {
		this.ctx = ctx;
	}

	@Bean
	public JDA jda(GuildManager manager, DiscordConfig config, CommandEvents commandEvents) throws LoginException {
		EventWaiter eventWaiter = new EventWaiter();
		CommandClient builder = new CommandClientBuilder()
			.setOwnerId(config.getOwnerId())
			.setPrefix(Constants.DEFAULT_PREFIX)
			.setPrefixes(new String[]{"<@!" + config.getBotId() + "> "})
			.setActivity(null)
			.setEmojis("✅", "⚠", "❌")
			.useHelpBuilder(false)
			.setGuildSettingsManager(manager)
			.addCommands(getCommands())
			.addSlashCommands(getSlashCommands())
			.setListener(commandEvents)
			.forceGuildOnly(config.getGuildId())
			.build();

		return JDABuilder
			.createDefault(config.getToken())
			.enableIntents(GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS))
			.enableCache(CacheFlag.ONLINE_STATUS, CacheFlag.ACTIVITY, CacheFlag.EMOTE)
			.disableCache(CacheFlag.VOICE_STATE)
			.setBulkDeleteSplittingEnabled(false)
			.setMemberCachePolicy(MemberCachePolicy.ALL)
			.useSharding(0, 1)
			.addEventListeners(eventWaiter, builder, manager)
			.addEventListeners(getEvents())
			.build();
	}

	private Command[] getCommands() {
		Reflections reflections = new Reflections(new ConfigurationBuilder().forPackage("org.module.commands"));
		Set<Class<?>> subTypes = reflections.get(SubTypes.of(Command.class).asClass());

		List<Command> commands = new ArrayList<>();
		for (Class<?> command : subTypes) {
			try {
				commands.add((Command) ctx.getBean(command));
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
		logger.info("Loaded " + commands.size() + " commands.");
		return commands.toArray(new Command[0]);
	}

	private SlashCommand[] getSlashCommands() {
		Reflections reflections = new Reflections(new ConfigurationBuilder().forPackage("org.module.commands"));
		Set<Class<?>> subTypes = reflections.get(SubTypes.of(SlashCommand.class).asClass());

		List<SlashCommand> slashCommands = new ArrayList<>();
		for (Class<?> slashCommand : subTypes) {
			try {
				slashCommands.add((SlashCommand) ctx.getBean(slashCommand));
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
		logger.info("Loaded " + slashCommands.size() + " slash commands.");
		return slashCommands.toArray(new SlashCommand[0]);
	}

	private Object[] getEvents() {
		Reflections reflections = new Reflections(new ConfigurationBuilder().forPackage("org.module.events"));
		Set<Class<?>> subTypes = reflections.get(SubTypes.of(ListenerAdapter.class).asClass());

		List<Object> events = new ArrayList<>();
		for (Class<?> event : subTypes) {
			if (!event.getName().startsWith("org.module.events.")) continue;
			try {
				events.add(ctx.getBean(event));
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
		logger.info("Loaded " + events.size() + " events.");
		return events.toArray(new Object[0]);
	}
}
