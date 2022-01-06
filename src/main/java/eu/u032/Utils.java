package eu.u032;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    private static final int YEAR = Calendar.getInstance().get(Calendar.YEAR);

    public static final Pattern EMOJI = Pattern.compile(":(\\d+)>");
    public static final Pattern MEMBER = Pattern.compile("<@!(\\d+)>");
    // public static final Pattern CHANNEL = Pattern.compile("<#(\\d+)>");

    public static void sendLog(Guild guild, EmbedBuilder embed) {
        embed.setTimestamp(new Date().toInstant());
        TextChannel textChannel = guild.getTextChannelById(Config.getString("LOGS_CHANNEL"));
        if (textChannel == null) return;
        textChannel.sendMessageEmbeds(embed.build()).queue();
    }

    public static String[] splitArgs(String args) {
        return args.split("\\s+");
    }

    public static String getId(String arg, Pattern pattern) {
        Matcher matcher = pattern.matcher(arg);

        if (matcher.find()) {
            return matcher.group(1);
        } else if (arg.length() == 18) {
            return arg;
        }

        return "";
    }

    public static String getGluedArg(String[] args, int start) {
        StringBuilder arg = new StringBuilder();

        for (int i = start; i < args.length; i++) {
            arg.append(args[i]).append(" ");
        }

        return arg.toString();
    }

    public static boolean hasRole(Role checkRole, Member member) {
        for (Role role : member.getRoles()) {
            if (role == checkRole) return true;
        }
        return false;
    }

    public static Color getColor() {
        return Color.decode(Config.getString("COLOR"));
    }

    public static Color getColorGreen() {
        return Color.decode(Config.getString("COLOR_GREEN"));
    }

    public static Color getColorRed() {
        return Color.decode(Config.getString("COLOR_RED"));
    }

    public static Color getColorYellow() {
        return Color.decode(Config.getString("COLOR_YELLOW"));
    }

    public static String getCopyright() {
        return "© " + YEAR + " " + Config.getString("DEVS");
    }
}
