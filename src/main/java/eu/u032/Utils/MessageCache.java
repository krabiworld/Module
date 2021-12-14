// from https://gist.github.com/Starrysparklez/3da0d67241d8185315e4fdc012f8aca7
package eu.u032.Utils;

import net.dv8tion.jda.api.entities.Message;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;

public class MessageCache {
    public static final ArrayList<Message> messages = new ArrayList<>();

    public static void addMessage(@Nonnull Message msg) {
        for (Message m : messages)
            if (m.getIdLong() == msg.getIdLong()) messages.set(messages.indexOf(m), msg);
        if (messages.size() + 1 > Config.getInt("MAX_MSG_CACHE")) messages.remove(0);
        messages.add(msg);
    }

    @Nullable
    public static Message getMessage(@Nonnull long msgid) {
        Message result = null;
        for (Message m : messages) if (m.getIdLong() == msgid) result = m;
        return result;
    }
}