package org.module.listeners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.module.Constants;
import org.module.util.LogsUtil;

public class MemberListener extends ListenerAdapter {
	@Override
	public void onGuildMemberJoin(GuildMemberJoinEvent event) {
		Member member = event.getMember();

		EmbedBuilder embed = new EmbedBuilder()
			.setAuthor(member.getUser().getAsTag(), null, member.getEffectiveAvatarUrl())
			.setColor(Constants.SUCCESS)
			.setDescription(member.getAsMention() + " joined to server!")
			.addField(getRegisteredAtField(member))
			.addField(getMemberCount(member.getGuild()))
			.setFooter("ID: " + member.getId());
		LogsUtil.sendLog(event.getGuild(), embed);
	}

	@Override
	public void onGuildMemberRemove(GuildMemberRemoveEvent event) {
		Member member = event.getMember();

		if (member == null) return;

		EmbedBuilder embed = new EmbedBuilder()
			.setAuthor(member.getUser().getAsTag(), null, member.getEffectiveAvatarUrl())
			.setColor(Constants.ERROR)
			.setDescription(member.getAsMention() + " has left the server!")
			.addField("Joined at",
				String.format("<t:%s>", member.getTimeJoined().toEpochSecond()), true)
			.addField(getRegisteredAtField(event.getMember()))
			.addField(getMemberCount(event.getGuild()))
			.setFooter("ID: " + member.getId());
		LogsUtil.sendLog(event.getGuild(), embed);
	}

	private MessageEmbed.Field getRegisteredAtField(Member member) {
		long registeredAt = member.getTimeCreated().toEpochSecond();
		return new MessageEmbed.Field("Registered at",
			String.format("<t:%s:D> (<t:%s:R>)", registeredAt, registeredAt), true);
	}

	private MessageEmbed.Field getMemberCount(Guild guild) {
		return new MessageEmbed.Field("Member count", String.valueOf(guild.getMemberCount()), true);
	}
}
