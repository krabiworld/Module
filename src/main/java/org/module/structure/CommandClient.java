package org.module.structure;

import java.util.List;

public interface CommandClient {
	String getOwnerId();

	List<Command> getCommands();

	List<Category> getCategories();

	GuildProvider.Manager getManager();
}
