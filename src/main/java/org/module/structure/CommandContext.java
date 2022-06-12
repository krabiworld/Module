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

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.module.Constants;
import org.module.util.EmbedUtil;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public record CommandContext(SlashCommandInteractionEvent event, CommandClient client, Command command) {
	private static final Consumer<InteractionHook> SUCCESS = i -> i.deleteOriginal().queueAfter(20, TimeUnit.SECONDS);

	private static GuildProvider.Settings settings = null;

	public void reply(String message) {
		event.reply(message).queue();
	}

	public void reply(MessageEmbed embed) {
		event.replyEmbeds(embed).queue();
	}

	public void replySuccess(String message) {
		MessageEmbed embed = new EmbedUtil(Constants.SUCCESS, message).build();
		event.replyEmbeds(embed).queue(SUCCESS);
	}

	public void replyError(String message) {
		MessageEmbed embed = new EmbedUtil(Constants.ERROR, message).build();
		event.replyEmbeds(embed).setEphemeral(true).queue();
	}

	public void replyHelp() {
		EmbedUtil embed = new EmbedUtil(command);
		event.replyEmbeds(embed.build()).setEphemeral(true).queue();
	}

	public boolean isOwner() {
		return getUser().getId().equals(getClient().getOwnerId());
	}

	public boolean isModerator() {
		Member member = event.getMember();

		if (member == null) return false;
		if (member.hasPermission(Permission.ADMINISTRATOR) || member.isOwner()) {
			return true;
		}

		return member.getRoles().contains(settings.getModeratorRole());
	}

	public Member getOptionAsMember(String key) {
		return getOptionAsMember(key, null);
	}

	public Member getOptionAsMember(String key, Member defaultValue) {
		return event.getOption(key, defaultValue, OptionMapping::getAsMember);
	}

	public User getOptionAsUser(String key, User defaultValue) {
		return event.getOption(key, defaultValue, OptionMapping::getAsUser);
	}

	public Role getOptionAsRole(String key) {
		return event.getOption(key, null, OptionMapping::getAsRole);
	}

	public TextChannel getOptionAsTextChannel(String key) {
		return event.getOption(key, null, OptionMapping::getAsTextChannel);
	}

	public int getOptionAsInt(String key) {
		return event.getOption(key, -1, OptionMapping::getAsInt);
	}

	public String getOptionAsString(String key) {
		return event.getOption(key, "", OptionMapping::getAsString);
	}

	public String getSubcommandName() {
		return event.getSubcommandName();
	}

	public JDA getJDA() {
		return event.getJDA();
	}

	public User getUser() {
		return event.getUser();
	}

	public Member getMember() {
		return event.getMember();
	}

	public Member getSelfMember() {
		return Objects.requireNonNull(event.getGuild()).getSelfMember();
	}

	public Guild getGuild() {
		return event.getGuild();
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

	public GuildProvider.Settings getSettings() {
		if (settings == null) {
			settings = getClient().getManager().getSettings(getGuild());
		}
		return settings;
	}

	public CommandClient getClient() {
		return client;
	}
}
