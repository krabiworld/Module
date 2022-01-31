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

package org.module;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.module.events.CommandEvents;
import org.module.manager.GuildManager;
import org.module.util.PropertyUtil;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import static org.reflections.scanners.Scanners.SubTypes;

public class Module {
	private static final Logger logger = LoggerFactory.getLogger(Module.class);

	public static void main(String[] args) throws LoginException {
		EventWaiter eventWaiter = new EventWaiter();
		GuildManager guildManager = new GuildManager();
		Properties config = PropertyUtil.getProperties("application");
		if (config == null) throw new NullPointerException("Config is null!");

		CommandClient builder = new CommandClientBuilder()
			.setOwnerId(config.getProperty("ownerId"))
			.setPrefix(Constants.DEFAULT_PREFIX)
			.setPrefixes(new String[]{"<@!" + config.getProperty("botId") + "> "})
			.setActivity(Activity.playing("!help"))
			.setEmojis("✅", null, "❌")
			.useHelpBuilder(false)
			.setGuildSettingsManager(guildManager)
			.addCommands(getCommands())
			.addSlashCommands(getSlashCommands())
			.setListener(new CommandEvents())
			.forceGuildOnly(config.getProperty("guildId"))
			.build();

		JDABuilder
			.createDefault(config.getProperty("token"))
			.enableIntents(GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS))
			.enableCache(CacheFlag.ONLINE_STATUS, CacheFlag.ACTIVITY, CacheFlag.EMOTE)
			.disableCache(CacheFlag.VOICE_STATE)
			.setBulkDeleteSplittingEnabled(false)
			.setMemberCachePolicy(MemberCachePolicy.ALL)
			.useSharding(0, 1)
			.addEventListeners(eventWaiter, builder, guildManager)
			.addEventListeners(getEvents())
			.build();
	}

	private static Command[] getCommands() {
		Reflections reflections = new Reflections(new ConfigurationBuilder().forPackage("org.module.commands"));
		Set<Class<?>> subTypes = reflections.get(SubTypes.of(Command.class).asClass());

		List<Command> commands = new ArrayList<>();
		for (Class<?> command : subTypes) {
			if (!command.getName().startsWith("org.module.commands.")) continue;
			try {
				commands.add((Command) command.getDeclaredConstructor().newInstance());
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
		logger.info("Loaded " + commands.size() + " commands.");
		return commands.toArray(new Command[0]);
	}

	private static SlashCommand[] getSlashCommands() {
		Reflections reflections = new Reflections(new ConfigurationBuilder().forPackage("org.module.commands"));
		Set<Class<?>> subTypes = reflections.get(SubTypes.of(SlashCommand.class).asClass());

		List<SlashCommand> slashCommands = new ArrayList<>();
		for (Class<?> slashCommand : subTypes) {
			if (!slashCommand.getName().startsWith("org.module.commands.")) continue;
			try {
				slashCommands.add((SlashCommand) slashCommand.getDeclaredConstructor().newInstance());
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
		logger.info("Loaded " + slashCommands.size() + " slash commands.");
		return slashCommands.toArray(new SlashCommand[0]);
	}

	private static Object[] getEvents() {
		Reflections reflections = new Reflections(new ConfigurationBuilder().forPackage("org.module.events"));
		Set<Class<?>> subTypes = reflections.get(SubTypes.of(ListenerAdapter.class).asClass());

		List<Object> events = new ArrayList<>();
		for (Class<?> event : subTypes) {
			if (!event.getName().startsWith("org.module.events.")) continue;
			try {
				events.add(event.getDeclaredConstructor().newInstance());
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
		logger.info("Loaded " + events.size() + " events.");
		return events.toArray(new Object[0]);
	}
}
