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

package eu.u032;

import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import eu.u032.commands.information.HelpCommand;
import eu.u032.commands.information.ServerinfoCommand;
import eu.u032.commands.information.StatsCommand;
import eu.u032.commands.information.UserCommand;
import eu.u032.commands.moderation.*;
import eu.u032.commands.owner.EvalCommand;
import eu.u032.commands.owner.OwnerCommand;
import eu.u032.commands.settings.LogsCommand;
import eu.u032.commands.settings.ModroleCommand;
import eu.u032.commands.settings.MuteroleCommand;
import eu.u032.commands.settings.PrefixCommand;
import eu.u032.commands.utilities.AvatarCommand;
import eu.u032.commands.utilities.EmojiCommand;
import eu.u032.config.Config;
import eu.u032.events.MemberEvents;
import eu.u032.events.MessageEvents;
import eu.u032.manager.GuildManager;
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
	public Bot(GuildManager manager, Config config, WarnCommand warnCommand,
			   RemwarnCommand remwarnCommand, WarnsCommand warnsCommand) throws LoginException {
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
				new UserCommand(),
				new StatsCommand(),
				new HelpCommand(),
				// Moderation
				new MuteCommand(),
				new UnmuteCommand(),
				new ClearCommand(),
				new SlowmodeCommand(),
				new KickCommand(),
				new BanCommand(),
				new UnbanCommand(),
				warnCommand,
				remwarnCommand,
				warnsCommand,
				// Settings
				new PrefixCommand(manager),
				new LogsCommand(manager),
				new MuteroleCommand(manager),
				new ModroleCommand(manager),
				// Owner
				new EvalCommand(),
				new OwnerCommand(),
				// Utilities
				new AvatarCommand(),
				new EmojiCommand())
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
				new MemberEvents(),
				new MessageEvents())
			.build();
	}
}
