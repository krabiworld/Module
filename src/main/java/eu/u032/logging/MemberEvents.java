package eu.u032.logging;

import eu.u032.Utils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateNicknameEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.List;

public class MemberEvents extends ListenerAdapter {
    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        Member member = event.getMember();

        EmbedBuilder embed = new EmbedBuilder()
                .setAuthor(member.getUser().getAsTag(), null, member.getEffectiveAvatarUrl())
                .setColor(Utils.getColorGreen())
                .setDescription(member.getAsMention() + " joined to server!")
                .addField("Registered at",
                        String.format("<t:%s>", member.getTimeCreated().toEpochSecond()), true)
                .addField("Member count",
                        String.valueOf(member.getGuild().getMemberCount()), true)
                .setFooter("ID: " + member.getId());
        Utils.sendLog(event.getGuild(), embed);
    }

    @Override
    public void onGuildMemberRemove(GuildMemberRemoveEvent event) {
        Member member = event.getMember();

        if (member == null) return;

        EmbedBuilder embed = new EmbedBuilder()
                .setAuthor(member.getUser().getAsTag(), null, member.getEffectiveAvatarUrl())
                .setColor(Utils.getColorRed())
                .setDescription(member.getAsMention() + " has left the server!")
                .addField("Joined at", 
                        String.format("<t:%s>", member.getTimeJoined().toEpochSecond()), true)
                .addField("Registered at",
                        String.format("<t:%s>", member.getTimeCreated().toEpochSecond()), true)
                .addField("Member count",
                        String.valueOf(event.getGuild().getMemberCount()), true)
                .setFooter("ID: " + member.getId());
        Utils.sendLog(event.getGuild(), embed);
    }

    @Override
    public void onGuildMemberUpdateNickname(GuildMemberUpdateNicknameEvent event) {
        Member member = event.getMember();
        String before = event.getOldNickname();
        String after = event.getNewNickname();
        String action = " was updated";

        if (before == null) return;
        if (after == null) action = " was reset";

        EmbedBuilder embed = new EmbedBuilder()
                .setAuthor("Nickname for " + member.getUser().getAsTag() + action,
                        null, member.getEffectiveAvatarUrl())
                .setColor(Utils.getColorYellow())
                .addField("Before", before, true)
                .setFooter("ID: " + member.getId());

        if (after != null) embed.addField("After", after, true);

        Utils.sendLog(event.getGuild(), embed);
    }

    @Override
    public void onGuildMemberRoleAdd(GuildMemberRoleAddEvent event) {
        List<Role> roles = event.getRoles();
        StringBuilder addedRoles = new StringBuilder();

        for (Role role : roles) {
            addedRoles.append(role.getAsMention()).append(" ");
        }

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Added role(s) for " + event.getUser().getName())
                .setColor(Utils.getColorGreen())
                .setDescription(addedRoles.toString())
                .setFooter("ID: " + event.getUser().getId());
        Utils.sendLog(event.getGuild(), embed);
    }

    @Override
    public void onGuildMemberRoleRemove(GuildMemberRoleRemoveEvent event) {
        List<Role> roles = event.getRoles();
        StringBuilder removedRoles = new StringBuilder();

        for (Role role : roles) {
            removedRoles.append(role.getAsMention()).append(" ");
        }

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Removed role(s) for " + event.getUser().getName())
                .setColor(Utils.getColorRed())
                .setDescription(removedRoles.toString())
                .setFooter("ID: " + event.getUser().getId());
        Utils.sendLog(event.getGuild(), embed);
    }
}
