package eu.u032.Commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;

import java.util.List;

public class ClearCommand extends Command {

    public ClearCommand() {
        this.name = "clear";
        this.help = "Clear messages";
        this.userPermissions = new Permission[]{Permission.MESSAGE_MANAGE};
        this.category = new Category("Moderation");
    }

    @Override
    protected void execute(CommandEvent event) {
        int count = Integer.parseInt(event.getArgs());
        if (count < 2 || count > 100) {
            event.reply("The number of messages must be no less than 2 and no more than 100.");
            return;
        }

        try {
            List<Message> messages = event.getChannel().getHistory().retrievePast(count).complete();

            event.getTextChannel().deleteMessages(messages).queue();
        } catch (Exception e) {
            event.reply(e.getMessage());
            event.getMessage().addReaction("U+274C").queue();
        }
    }

}
