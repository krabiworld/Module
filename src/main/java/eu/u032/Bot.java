/*
 * UASM Discord Bot.
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
import eu.u032.commands.settings.ModroleCommand;
import eu.u032.commands.settings.PrefixCommand;
import eu.u032.commands.settings.LogsCommand;
import eu.u032.commands.settings.MuteroleCommand;
import eu.u032.events.GuildEvents;
import eu.u032.logging.*;
import eu.u032.commands.information.*;
import eu.u032.commands.moderation.*;
import eu.u032.commands.utilities.*;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;

public class Bot {
	public static void main(String[] args) throws LoginException {
		final EventWaiter eventWaiter = new EventWaiter();
		final GuildManager manager = new GuildManager();
		final CommandClient builder = new CommandClientBuilder()
			.setOwnerId(Config.getString("OWNER_ID"))
			.setPrefix(Constants.PREFIX)
			.setActivity(null)
			.setStatus(OnlineStatus.IDLE)
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
				new WarnCommand(),
				new RemwarnCommand(),
				new WarnsCommand(),
				// Settings
				new PrefixCommand(manager),
				new LogsCommand(manager),
				new MuteroleCommand(manager),
				new ModroleCommand(manager),
				// Utilities
				new AvatarCommand(),
				new EmojiCommand())
			.build();

        JDABuilder
			.createDefault(Config.getString("TOKEN"))
			.enableIntents(GatewayIntent.GUILD_MEMBERS,
				GatewayIntent.GUILD_MESSAGES,
				GatewayIntent.GUILD_PRESENCES,
				GatewayIntent.DIRECT_MESSAGES,
				GatewayIntent.GUILD_EMOJIS)
			.enableCache(CacheFlag.ONLINE_STATUS, CacheFlag.ACTIVITY, CacheFlag.EMOTE)
			.disableCache(CacheFlag.VOICE_STATE)
			.setBulkDeleteSplittingEnabled(false)
			.setMemberCachePolicy(MemberCachePolicy.ALL)
			.useSharding(0, 1)
			.addEventListeners(eventWaiter, builder,
				new MemberEvents(),
				new MessageEvents(),
				new GuildEvents())
			.build();
    }
}
