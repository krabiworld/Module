package eu.u032;

import net.dv8tion.jda.api.entities.Message;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;

// Author: https://github.com/Starrysparklez/
// Code: https://gist.github.com/Starrysparklez/3da0d67241d8185315e4fdc012f8aca7
public class MessageCache {
    public static final ArrayList<Message> messages = new ArrayList<>();

    public static void addMessage(@Nonnull Message message) {
        for (Message msg : messages)
            if (msg.getIdLong() == message.getIdLong()) messages.set(messages.indexOf(msg), message);
        if (messages.size() + 1 > Config.getInt("MAX_MSG_CACHE")) messages.remove(0);
        messages.add(message);
    }

    @Nullable
    public static Message getMessage(long messageId) {
        Message result = null;
        for (Message message : messages) if (message.getIdLong() == messageId) result = message;
        return result;
    }
}
