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
