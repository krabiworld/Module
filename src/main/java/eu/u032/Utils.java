package eu.u032;

import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.entities.Member;

public class Utils {

    public static Member getMemberFromArgs(CommandEvent event) {
        String[] args = event.getArgs().split("\\s+");
        Member member;

        if (!event.getMessage().getMentionedMembers().isEmpty()) {
            member = event.getMessage().getMentionedMembers().get(0);
        } else if (args[0].length() == 18) {
            member = event.getGuild().retrieveMemberById(args[0]).complete();
        } else {
            member = null;
        }

        return member;
    }

}
