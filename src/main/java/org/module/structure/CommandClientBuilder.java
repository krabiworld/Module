package org.module.structure;

import java.util.Collections;
import java.util.LinkedList;

public class CommandClientBuilder {
	private final LinkedList<Command> commands = new LinkedList<>();
	private String ownerId;
	private String forceGuildId = null;
	private GuildProvider.Manager manager;
	private CommandListenerAdapter listener;

	public static CommandClientBuilder builder() {
		return new CommandClientBuilder();
	}

	public CommandClient build() {
		return new CommandClientImpl(ownerId, forceGuildId, commands, manager, listener);
	}

	public CommandClientBuilder setOwnerId(String ownerId) {
		this.ownerId = ownerId;
		return this;
	}

	public CommandClientBuilder forceGuildOnly(String guildId) {
		this.forceGuildId = guildId;
		return this;
	}

	public CommandClientBuilder setCommands(Command... commands) {
		Collections.addAll(this.commands, commands);
		return this;
	}

	public CommandClientBuilder setGuildManager(GuildProvider.Manager manager) {
		this.manager = manager;
		return this;
	}

	public CommandClientBuilder setListener(CommandListenerAdapter listener) {
		this.listener = listener;
		return this;
	}
}
