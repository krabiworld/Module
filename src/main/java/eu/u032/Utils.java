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
	public static final Pattern EMOJI = Pattern.compile(":(\\d+)>");
    public static final Pattern MEMBER = Pattern.compile("<@!(\\d+)>");
    // public static final Pattern CHANNEL = Pattern.compile("<#(\\d+)>");

	public static void sendLog(final Guild guild, final EmbedBuilder embed) {
		embed.setTimestamp(new Date().toInstant());

		final String logsChannelId = Config.getString("LOGS_CHANNEL");
        final TextChannel textChannel = logsChannelId.isEmpty() ? null : guild.getTextChannelById(logsChannelId);

		if (textChannel == null) return;

        textChannel.sendMessageEmbeds(embed.build()).queue();
    }

    public static String[] splitArgs(final String args) {
        return args.split("\\s+");
    }

    public static String getId(final String arg, final Pattern pattern) {
        final Matcher matcher = pattern.matcher(arg);

        if (matcher.find()) {
            return matcher.group(1);
        } else if (arg.length() == 18) {
            return arg;
        }

        return "";
    }

    public static String getGluedArg(final String[] args, final int start) {
        final StringBuilder arg = new StringBuilder();

        for (int i = start; i < args.length; i++) {
            arg.append(args[i]).append(" ");
        }

        return arg.toString();
    }

    public static boolean hasRole(final Member member, final Role role) {
        for (final Role memberRole : member.getRoles()) {
            if (memberRole == role) return true;
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
}
