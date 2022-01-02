package eu.u032;

import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.util.*;

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
