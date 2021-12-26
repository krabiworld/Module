package eu.u032;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;

import java.util.*;

public class Utils {

    public static Member getMemberFromArgs(CommandEvent event) {
        String[] args = event.getArgs().split("\\s+");
        Member member = null;

        if (!event.getMessage().getMentionedMembers().isEmpty()) {
            member = event.getMessage().getMentionedMembers().get(0);
        } else if (args[0].length() == 18) {
            member = event.getGuild().retrieveMemberById(args[0]).complete();
        }

        return member;
    }

    public static void help(CommandEvent event) {
        EmbedBuilder embed = new EmbedBuilder().setColor(event.getMember().getColor());

        List<String> categories = new LinkedList<>();

        for (Command command : event.getClient().getCommands())
            categories.add(command.getCategory().getName());

        Set<String> categoriesSet = new LinkedHashSet<>(categories);

        if (event.getArgs().isEmpty()) {
            StringBuilder cmdStr = new StringBuilder();

            for (String category : categoriesSet) {
                for (Command command : event.getClient().getCommands()) {
                    if (command.isHidden()) continue;
                    if (command.getCategory().getName().equals(category)) {
                        cmdStr.append("`")
                                .append(event.getClient().getPrefix())
                                .append(command.getName())
                                .append("` ");
                    }
                }
                embed.addField(category, cmdStr.toString(), false);
                cmdStr = new StringBuilder();
            }
            event.reply(embed.build());
            return;
        } else {
            for (String category : categories) {
                if (category.toLowerCase().startsWith(event.getArgs().toLowerCase())) {
                    for (Command cmd : event.getClient().getCommands()) {
                        if (cmd.isHidden() || cmd.isOwnerCommand()) continue;
                        if (cmd.getCategory().getName().equals(category))
                            embed.addField(event.getClient().getPrefix() + cmd.getName(), cmd.getHelp(), false);
                    }
                    embed.setTitle("Commands of category " + category);
                    event.reply(embed.build());
                    return;
                }
            }
            for (Command cmd : event.getClient().getCommands()) {
                if (cmd.getName().toLowerCase().startsWith(event.getArgs().toLowerCase()) && !cmd.isHidden() && !cmd.isOwnerCommand()) {
                    embed.setTitle("Information of command " + cmd.getName());
                    embed.setDescription(String.format("`%s%s %s`\n%s",
                            event.getClient().getPrefix(),
                            cmd.getName(),
                            cmd.getArguments(),
                            cmd.getHelp()
                    ));
                    event.reply(embed.build());
                    return;
                }
            }
        }

        event.replyError("Command or category **" + event.getArgs() + "** not found.");
    }

}
