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

package org.module.structure;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.module.Locale;
import org.module.util.EmbedUtil;
import org.module.util.LocaleUtil;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class CommandClientImpl extends ListenerAdapter implements CommandClient {
	private final String ownerId;
	private final List<AbstractCommand> commands;
	private final GuildManagerProvider manager;
	private final CommandListenerAdapter listener;
	private final ScheduledExecutorService executor;

	public CommandClientImpl(
		String ownerId,
		List<AbstractCommand> commands,
		GuildManagerProvider manager,
		CommandListenerAdapter listener
	) {
		this.ownerId = ownerId;
		this.commands = commands;
		this.manager = manager;
		this.listener = listener;
		this.executor = Executors.newSingleThreadScheduledExecutor();
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		if (event.getAuthor().isBot()) return;

		GuildSettingsProvider settings = manager.getSettings(event.getGuild());
		if (settings == null) return;

		String raw = event.getMessage().getContentRaw();

		String prefix = settings.getPrefix();

		if (!raw.startsWith(prefix)) {
			String mention = event.getJDA().getSelfUser().getAsMention();
			if (!raw.startsWith(mention)) return;
			prefix = mention.trim();
		}

		raw = raw.substring(prefix.length()).trim();
		int spaceIndex = raw.indexOf(' ');
		String cmdName = raw.substring(0, spaceIndex == -1 ? raw.length() : spaceIndex);
		String args = raw.substring(cmdName.length()).trim();
		AbstractCommand abstractCommand = null;
		Locale locale = LocaleUtil.getLocale(settings);

		for (AbstractCommand command : commands) {
			String name = locale.get(command.getAnnotation().name());

			if (name.equals(cmdName)) {
				abstractCommand = command;
				break;
			}
		}

		if (abstractCommand == null) return;

		Command command = abstractCommand.getAnnotation();
		EmbedUtil helpEmbed = new EmbedUtil(command, locale, settings.getPrefix());
		CommandContext context = new CommandContext(event, this, command, helpEmbed, settings, locale, args);

		executor.submit(listener::onCommand);

		AbstractCommand finalAbstractCommand = abstractCommand;
		executor.submit(() -> finalAbstractCommand.run(context));
	}

	@Override
	public String getOwnerId() {
		return ownerId;
	}

	@Override
	public List<AbstractCommand> getCommands() {
		return commands;
	}

	@Override
	public GuildManagerProvider getManager() {
		return manager;
	}

	@Override
	public ScheduledExecutorService getScheduleExecutor() {
		return executor;
	}
}
