package eu.u032;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;

import java.util.*;

public class Utils {

    public static String[] splitArgs(String args) {
        return args.split("\\s+");
    }

    public static Member getMemberFromArgs(CommandEvent event) {
        String[] args = splitArgs(event.getArgs());
        Member member = null;

        if (!event.getMessage().getMentionedMembers().isEmpty()) {
            member = event.getMessage().getMentionedMembers().get(0);
        } else if (args[0].length() == 18) {
            member = event.getGuild().retrieveMemberById(args[0]).complete();
        }

        return member;
    }

    public static String getArgsAsString(String[] args, int start) {
        return getArgsAsString(args, start, false);
    }

    public static String getArgsAsString(String[] args, int start, boolean isOneArg) {
        StringBuilder argsString = new StringBuilder();

        for (int i = start; i < args.length; i++) {
            argsString.append(args[i]).append(" ");
            if (isOneArg) break;
        }

        return argsString.toString();
    }

    public static void help(CommandEvent event) {
        String args = event.getArgs();
        String prefix = event.getClient().getPrefix();
        EmbedBuilder embed = new EmbedBuilder().setColor(event.getMember().getColorRaw());

        List<String> categoriesList = new LinkedList<>();

        for (Command command : event.getClient().getCommands()) {
            if (command.getCategory() == null) continue;
            categoriesList.add(command.getCategory().getName());
        }

        Set<String> categories = new LinkedHashSet<>(categoriesList);

        if (args.isEmpty()) {
            StringBuilder commands = new StringBuilder();

            for (String category : categories) {
                for (Command command : event.getClient().getCommands()) {
                    if (command.isHidden()) continue;
                    if (command.getCategory().getName().equals(category)) {
                        commands.append("`")
                                .append(prefix)
                                .append(command.getName())
                                .append("` ");
                    }
                }
                embed.addField(category, commands.toString(), false);
                commands = new StringBuilder();
            }

            event.reply(embed.build());
            return;
        } else {
            for (String category : categories) {
                if (category.toLowerCase().startsWith(args.toLowerCase())) {
                    for (Command cmd : event.getClient().getCommands()) {
                        if (cmd.isHidden()) continue;
                        if (cmd.getCategory().getName().equals(category))
                            embed.addField(prefix + cmd.getName(), cmd.getHelp(), false);
                    }
                    embed.setTitle("Commands of category " + category);
                    event.reply(embed.build());
                    return;
                }
            }
            for (Command cmd : event.getClient().getCommands()) {
                if (cmd.getName().toLowerCase().startsWith(args.toLowerCase()) && !cmd.isHidden()) {
                    embed.setTitle("Information of command " + cmd.getName());
                    embed.setDescription("`" + prefix + cmd.getName() + " " + cmd.getArguments() + "`\n" + cmd.getHelp());
                    event.reply(embed.build());
                    return;
                }
            }
        }

        event.replyError("Command or category **" + args + "** not found.");
    }

}
