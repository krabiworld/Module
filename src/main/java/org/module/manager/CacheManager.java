package org.module.manager;

import lombok.Getter;
import net.dv8tion.jda.api.entities.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.module.Constants;

import java.util.ArrayList;

/*
 * Author: https://github.com/Starrysparklez/
 * Code: https://gist.github.com/Starrysparklez/3da0d67241d8185315e4fdc012f8aca7
 */
public class CacheManager {
    public static final ArrayList<Message> MESSAGES = new ArrayList<>();

	@Getter
	public static int executedCommands = 0;

    public static void addMessage(@NotNull Message message) {
		MESSAGES.stream()
			.filter(msg -> msg.getIdLong() == message.getIdLong())
			.forEach(msg -> MESSAGES.set(MESSAGES.indexOf(msg), message));

		if (MESSAGES.size() + 1 > Constants.MAX_MESSAGE_CACHE) MESSAGES.removeFirst();

        MESSAGES.add(message);
    }

    @Nullable
    public static Message getMessage(long messageId) {
		return MESSAGES.stream()
			.filter(msg -> msg.getIdLong() == messageId)
			.findFirst()
			.orElse(null);
    }

	public static void incrementExecutedCommands() {
		executedCommands++;
	}

	public static boolean checkExecutedCommands() {
		return executedCommands >= Constants.MAX_EXECUTED_COMMANDS_CACHE;
	}

	public static void resetExecutedCommands() {
		executedCommands = 0;
	}
}
