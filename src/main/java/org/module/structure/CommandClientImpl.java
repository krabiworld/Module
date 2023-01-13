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
