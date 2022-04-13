/*
 * This file is part of Module.
 *
 * Module is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Module is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Module. If not, see <https://www.gnu.org/licenses/>.
 */

package org.module.structure;

import com.jagrosh.jdautilities.commons.utils.FinderUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.module.Constants;
import org.module.Locale;
import org.module.util.EmbedUtil;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class CommandContext {
	private static final Consumer<Message> errorDelay = m -> m.delete().queueAfter(10, TimeUnit.SECONDS);
	private static final Consumer<Message> successDelay = m -> m.delete().queueAfter(20, TimeUnit.SECONDS);

	private final MessageReceivedEvent event;
	private final CommandClient client;
	private final Command command;
	private final EmbedUtil helpEmbed;
	private final GuildSettingsProvider settings;
	private final Locale locale;
	private String args;

	public CommandContext(
		MessageReceivedEvent event,
		CommandClient client,
		Command command,
		EmbedUtil helpEmbed,
		GuildSettingsProvider settings,
		Locale locale,
		String args
	) {
		this.event = event;
		this.client = client;
		this.command = command;
		this.helpEmbed = helpEmbed;
		this.settings = settings;
		this.locale = locale;
		this.args = args;
	}

	public void send(String message) {
		sendMessage(message);
	}

	public void send(MessageEmbed embed) {
		sendMessage(embed);
	}

	public void send(EmbedBuilder embedBuilder) {
		sendMessage(embedBuilder.build());
	}

	public void sendSuccess(String key, Object... args) {
		react("✅");
		MessageEmbed embed = new EmbedUtil(Constants.SUCCESS, locale.get(key, args)).build();
		sendMessage(embed, successDelay);
	}

	public void sendError(String key, Object... args) {
		react("❌");
		MessageEmbed embed = new EmbedUtil(Constants.ERROR, locale.get(key, args)).build();
		sendMessage(embed, errorDelay);
	}

	public void sendHelp() {
		sendMessage(helpEmbed.build(), successDelay);
	}

	public boolean isModerator() {
		Member member = event.getMember();

		if (member == null) return false;
		if (member.hasPermission(Permission.ADMINISTRATOR) || member.isOwner()) {
			return true;
		}

		return member.getRoles().contains(settings.getModeratorRole());
	}

	public boolean isBanned(String user) {
		return !FinderUtil.findBannedUsers(user, event.getGuild()).isEmpty();
	}

	public String get(String key, Object... args) {
		return locale.get(key, args);
	}

	public Locale getLocale() {
		return locale;
	}

	public JDA getJDA() {
		return event.getJDA();
	}

	public String getPrefix() {
		return settings.getPrefix();
	}

	public User getUser() {
		return event.getAuthor();
	}

	public Member getMember() {
		return event.getMember();
	}

	public Member getSelfMember() {
		return event.getGuild().getSelfMember();
	}

	public Guild getGuild() {
		return event.getGuild();
	}

	public String getArgs() {
		return args;
	}

	public void setArgs(String args) {
		this.args = args;
	}

	public Message getMessage() {
		return event.getMessage();
	}

	public MessageChannel getChannel() {
		return event.getChannel();
	}

	public GuildChannel getGuildChannel() {
		return event.getGuildChannel();
	}

	public TextChannel getTextChannel() {
		return event.getTextChannel();
	}

	public GuildSettingsProvider getSettings() {
		return settings;
	}

	public List<AbstractCommand> getCommands() {
		return client.getCommands();
	}

	public CommandClient getClient() {
		return client;
	}

	public GuildManagerProvider getManager() {
		return client.getManager();
	}

	public Command getCommand() {
		return command;
	}

	public String[] splitArgs() {
		return args.split("\\s+");
	}

	public String getGluedArg(String[] args, int start) {
		StringBuilder arg = new StringBuilder();

		for (int i = start; i < args.length; i++) {
			arg.append(args[i]).append(" ");
		}

		return arg.toString().trim();
	}

	public Member findMember(String query) {
		List<Member> members = FinderUtil.findMembers(query, event.getGuild());
		return members.stream().findFirst().orElse(null);
	}

	public User findUser(String query) {
		List<User> users = FinderUtil.findUsers(query, event.getJDA());
		return users.stream().findFirst().orElse(null);
	}

	public Role findRole(String query) {
		List<Role> roles = FinderUtil.findRoles(query, event.getGuild());
		return roles.stream().findFirst().orElse(null);
	}

	public TextChannel findTextChannel(String query) {
		List<TextChannel> channels = FinderUtil.findTextChannels(query, event.getGuild());
		return channels.stream().findFirst().orElse(null);
	}

	public Emote findEmote(String query) {
		List<Emote> emotes = FinderUtil.findEmotes(query, event.getGuild());
		return emotes.stream().findFirst().orElse(null);
	}

	// private methods
	private void sendMessage(String message) {
		event.getChannel().sendMessage(message).queue();
	}

	private void sendMessage(MessageEmbed embed) {
		event.getChannel().sendMessageEmbeds(embed).queue();
	}

	private void sendMessage(MessageEmbed embed, Consumer<Message> consumer) {
		event.getChannel().sendMessageEmbeds(embed).queue(consumer);
	}

	private void react(String reaction) {
		event.getMessage().addReaction(reaction.replaceAll("<a?:(.+):(\\d+)>", "$1:$2")).queue();
	}
}
