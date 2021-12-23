package eu.u032.Commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import eu.u032.Utils.Args;
import eu.u032.Utils.Property;
import net.dv8tion.jda.api.entities.Member;

public class TestCommand extends Command {

    public TestCommand() {
        this.name = "test";
        this.ownerCommand = true;
        this.hidden = true;
    }

    @Override
    protected void execute(CommandEvent event) {
        Member member = Args.getMemberFromArgs(event);

        if (member == null) {
            event.replyError(Property.getError("member_not_found"));
            return;
        }

        event.reply("Member: " + member.getUser().getName());
    }
}
