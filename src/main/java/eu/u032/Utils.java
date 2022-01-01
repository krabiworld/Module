package eu.u032;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Utils {

    public static void sendLog(Guild guild, EmbedBuilder embed) {
        embed.setTimestamp(new Date().toInstant());
        TextChannel textChannel = guild.getTextChannelById(Config.getString("LOGS_CHANNEL"));
        if (textChannel == null) return;
        textChannel.sendMessageEmbeds(embed.build()).queue();
    }

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

    public static String getReasonFromArgs(String[] args, int start) {
        StringBuilder argsString = new StringBuilder();

        for (int i = start; i < args.length; i++) {
            argsString.append(args[i]).append(" ");
        }

        return argsString.toString();
    }

    public static void help(CommandEvent event) {
        String args = event.getArgs();
        String prefix = event.getClient().getPrefix();
        EmbedBuilder embed = new EmbedBuilder().setColor(getColor()).setFooter(getCopyright());

        List<String> categoriesList = new LinkedList<>();

        for (Command command : event.getClient().getCommands()) {
            if (command.getCategory() == null) continue;
            categoriesList.add(command.getCategory().getName());
        }

        Set<String> categories = new LinkedHashSet<>(categoriesList);

        if (args.isEmpty()) {
            StringBuilder commands = new StringBuilder();
            embed.setTitle("Available commands:");

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
                embed.addField(category + " (" + prefix + "help " + category + ")", commands.toString(), false);
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
                    embed.setDescription(
                            "`" + prefix + cmd.getName() + (cmd.getArguments() == null ? "" : " " + cmd.getArguments()) + "`\n" + cmd.getHelp()
                    );
                    event.reply(embed.build());
                    return;
                }
            }
        }

        event.replyError("Command or category **" + args + "** not found.");
    }

    public static Color getColor() {
        return Color.decode(Config.getString("COLOR"));
    }

    public static Color getColorCreate() {
        return Color.decode(Config.getString("COLOR_CREATE"));
    }

    public static Color getColorDelete() {
        return Color.decode(Config.getString("COLOR_DELETE"));
    }

    public static Color getColorUpdate() {
        return Color.decode(Config.getString("COLOR_UPDATE"));
    }

    public static String getCopyright() {
        return "Copyright © 2022 — untled032, Headcrab";
    }

}
