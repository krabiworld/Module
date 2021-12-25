package eu.u032.BotEvents;

import eu.u032.Utils.Config;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateNicknameEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.util.Date;

public class MemberEvents extends ListenerAdapter {

    // Member join
    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        Member member = event.getMember();

        EmbedBuilder embed = new EmbedBuilder()
                .setAuthor(member.getUser().getAsTag(), member.getEffectiveAvatarUrl(), member.getEffectiveAvatarUrl())
                .setColor(Color.decode("#f7d724"))
                .setDescription(String.format("%s joined to server!", member.getAsMention()))
                .addField("Registered at", String.format("<t:%s>", member.getTimeCreated().toEpochSecond()), true)
                .addField("Member count", String.valueOf(member.getGuild().getMemberCount()), true)
                .setFooter("User ID: " + member.getId())
                .setTimestamp(new Date().toInstant());
        event.getJDA()
                .getTextChannelById(Config.getString("LOGS_CHANNEL"))
                .sendMessageEmbeds(embed.build())
                .queue();
    }

    // Member leave
    @Override
    public void onGuildMemberRemove(GuildMemberRemoveEvent event) {
        Member member = event.getMember();

        if (member == null) return;

        EmbedBuilder embed = new EmbedBuilder()
                .setAuthor(member.getUser().getAsTag(), member.getEffectiveAvatarUrl(), member.getEffectiveAvatarUrl())
                .setColor(Color.decode("#e94b3e"))
                .setDescription(String.format("%s has left the server!", member.getAsMention()))
                .addField("Joined at", String.format("<t:%s>", member.getTimeJoined().toEpochSecond()), true)
                .addField("Registered at", String.format("<t:%s>", member.getTimeCreated().toEpochSecond()), true)
                .addField("Member count", String.valueOf(event.getGuild().getMemberCount()), true)
                .setFooter("User ID: " + member.getId())
                .setTimestamp(new Date().toInstant());
        event.getJDA()
                .getTextChannelById(Config.getString("LOGS_CHANNEL"))
                .sendMessageEmbeds(embed.build())
                .queue();
    }

    // Member nickname update
    @Override
    public void onGuildMemberUpdateNickname(GuildMemberUpdateNicknameEvent event) {
        Member member = event.getMember();
        String before = event.getOldNickname();
        String after = event.getNewNickname();
        String action = " was updated";

        if (before == null) return;
        if (after == null) action = " was reset";

        EmbedBuilder embed = new EmbedBuilder()
                .setAuthor("Nickname for " + member.getUser().getAsTag() + action, null, member.getEffectiveAvatarUrl())
                .setColor(member.getColor())
                .addField("Before", before, true)
                .setFooter("User ID: " + member.getId())
                .setTimestamp(new Date().toInstant());

        if (after != null) embed.addField("After", after, true);

        event.getJDA()
                .getTextChannelById(Config.getString("LOGS_CHANNEL"))
                .sendMessageEmbeds(embed.build())
                .queue();
    }

}
