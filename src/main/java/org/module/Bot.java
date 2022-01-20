/*
 * Module Discord Bot.
 * Copyright (C) 2022 untled032, Headcrab

 * UASM is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * UASM is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with UASM. If not, see https://www.gnu.org/licenses/.
 */

package org.module;

import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import org.module.commands.information.HelpCommand;
import org.module.commands.information.ServerinfoCommand;
import org.module.commands.information.StatsCommand;
import org.module.commands.information.UserCommand;
import org.module.commands.moderation.*;
import org.module.commands.owner.EvalCommand;
import org.module.commands.owner.OwnerCommand;
import org.module.commands.settings.LogsCommand;
import org.module.commands.settings.ModroleCommand;
import org.module.commands.settings.MuteroleCommand;
import org.module.commands.settings.PrefixCommand;
import org.module.commands.utilities.AvatarCommand;
import org.module.commands.utilities.EmojiCommand;
import org.module.commands.utilities.RandomCommand;
import org.module.config.Config;
import org.module.events.MemberEvents;
import org.module.events.MessageEvents;
import org.module.manager.GuildManager;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.security.auth.login.LoginException;

@Component
public class Bot {
	@Autowired
	public Bot(GuildManager manager, Config config,
			   HelpCommand helpCommand, UserCommand userCommand,
			   BanCommand banCommand, ClearCommand clearCommand,
			   KickCommand kickCommand, MuteCommand muteCommand,
			   RemwarnCommand remwarnCommand, SlowmodeCommand slowmodeCommand,
			   UnbanCommand unbanCommand, UnmuteCommand unmuteCommand,
			   WarnCommand warnCommand, WarnsCommand warnsCommand,
			   EvalCommand evalCommand, OwnerCommand ownerCommand,
			   LogsCommand logsCommand, ModroleCommand modroleCommand,
			   MuteroleCommand muteroleCommand, PrefixCommand prefixCommand,
			   AvatarCommand avatarCommand, EmojiCommand emojiCommand,
			   RandomCommand randomCommand,
			   MemberEvents memberEvents, MessageEvents messageEvents) throws LoginException {
		EventWaiter eventWaiter = new EventWaiter();
		CommandClient builder = new CommandClientBuilder()
			.setOwnerId(config.getOwner())
			.setPrefix(Constants.PREFIX)
			.setActivity(null)
			.setEmojis("✅", "⚠", "❌")
			.useHelpBuilder(false)
			.setGuildSettingsManager(manager)
			.addCommands(
				// Information
				new ServerinfoCommand(),
				userCommand,
				new StatsCommand(),
				helpCommand,
				// Moderation
				muteCommand,
				unmuteCommand,
				clearCommand,
				slowmodeCommand,
				kickCommand,
				banCommand,
				unbanCommand,
				warnCommand,
				remwarnCommand,
				warnsCommand,
				// Settings
				prefixCommand,
				logsCommand,
				muteroleCommand,
				modroleCommand,
				// Owner
				evalCommand,
				ownerCommand,
				// Utilities
				avatarCommand,
				emojiCommand,
				randomCommand)
			.build();

		JDABuilder
			.createDefault(config.getToken())
			.enableIntents(GatewayIntent.GUILD_MEMBERS,
				GatewayIntent.GUILD_MESSAGES,
				GatewayIntent.GUILD_PRESENCES,
				GatewayIntent.DIRECT_MESSAGES)
			.enableCache(CacheFlag.ONLINE_STATUS, CacheFlag.ACTIVITY, CacheFlag.EMOTE)
			.disableCache(CacheFlag.VOICE_STATE)
			.setBulkDeleteSplittingEnabled(false)
			.setMemberCachePolicy(MemberCachePolicy.ALL)
			.useSharding(0, 1)
			.addEventListeners(eventWaiter, builder, manager,
				memberEvents, messageEvents)
			.build();
	}
}
