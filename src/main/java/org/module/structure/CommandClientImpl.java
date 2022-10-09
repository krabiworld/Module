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

import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class CommandClientImpl extends ListenerAdapter implements CommandClient {
	private final String ownerId;
	private final String forceGuildId;
	private final List<Command> commands;
	private final GuildProvider.Manager manager;
	private final CommandListenerAdapter listener;

	public CommandClientImpl(
		String ownerId,
		String forceGuildId,
		List<Command> commands,
		GuildProvider.Manager manager,
		CommandListenerAdapter listener
	) {
		this.ownerId = ownerId;
		this.forceGuildId = forceGuildId;
		this.commands = commands;
		this.manager = manager;
		this.listener = listener;
	}

	@Override
	public void onReady(@NotNull ReadyEvent event) {
		List<CommandData> data = new ArrayList<>();

		for (Command command : commands) {
			data.add(command.buildCommandData());
		}

		if (forceGuildId == null) {
			event.getJDA().updateCommands().addCommands(data).queue();
		} else {
			Objects.requireNonNull(event.getJDA().getGuildById(forceGuildId))
				.updateCommands().addCommands(data).queue();
		}
	}

	@Override
	public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
		Command command = commands
			.stream()
			.filter(cmd -> cmd.getName().equals(event.getName()))
			.findFirst()
			.orElse(null);

		if (command == null) return;

		ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

		executor.submit(listener::onCommand);
		executor.submit(() -> command.run(new CommandContext(event, this, command)));
	}

	@Override
	public String getOwnerId() {
		return ownerId;
	}

	@Override
	public List<Command> getCommands() {
		return commands;
	}

	@Override
	public List<Category> getCategories() {
		return Arrays.asList(Category.values());
	}

	@Override
	public GuildProvider.Manager getManager() {
		return manager;
	}
}
