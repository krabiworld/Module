package eu.u032.Commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;

import java.time.OffsetDateTime;
import java.util.LinkedList;
import java.util.List;

public class ClearCommand extends Command {

    public ClearCommand() {
        this.name = "clear";
        this.help = "Clear last messages in current channel";
        this.arguments = "<count>";
        this.category = new Category("Moderation");
        this.userPermissions = new Permission[]{Permission.MESSAGE_MANAGE};
        this.botPermissions = new Permission[]{Permission.MESSAGE_MANAGE};
    }

    @Override
    protected void execute(CommandEvent event) {
        int count = Integer.parseInt(event.getArgs());
        if (count < 2 || count > 1000 || event.getArgs().isEmpty()) {
            event.replyError("The number of messages must be no less than 2 and no more than 1000.");
            return;
        }

        try {
            List<Message> messages = new LinkedList<>();
            MessageHistory history = event.getChannel().getHistory();
            OffsetDateTime dateTime = event.getMessage().getTimeCreated().minusHours(335);

            while (count > 100) {
                messages.addAll(history.retrievePast(100).complete());
                count -= 100;
                if (messages.get(messages.size() - 1).getTimeCreated().isBefore(dateTime)) {
                    count = 0;
                    break;
                }
            }

            if (count > 0) messages.addAll(history.retrievePast(count).complete());

            List<Message> delMessages = new LinkedList<>();
            for (Message message : messages) {
                if (message.getTimeCreated().isBefore(dateTime)) break;
                if (message.isPinned()) continue;
                delMessages.add(message);
            }

            int index = 0;
            while (index < delMessages.size()) {
                if (index + 100 > delMessages.size()) {
                    if (index + 1 == delMessages.size()) delMessages.get(delMessages.size()).delete().complete();
                    else event.getTextChannel().deleteMessages(delMessages.subList(index, delMessages.size())).complete();
                } else event.getTextChannel().deleteMessages(delMessages.subList(index, index + 100)).complete();
                index += 100;
            }
        } catch (Exception e) {
            event.replyError(e.getMessage());
        }
    }

}
