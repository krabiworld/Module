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

import com.jagrosh.jdautilities.command.CommandClientBuilder;
import eu.u032.logging.*;
import eu.u032.commands.*;
import eu.u032.commands.information.*;
import eu.u032.commands.moderation.*;
import eu.u032.commands.utilities.*;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;

public class Bot {
	public static void main(String[] args) throws LoginException {
		final CommandClientBuilder builder = new CommandClientBuilder()
			.setOwnerId(Config.getString("OWNER_ID"))
			.setPrefix(Config.getString("PREFIX"))
			.setActivity(Activity.playing("JDA"))
			.setStatus(OnlineStatus.IDLE)
			.setEmojis("✅", "⚠", "❌")
			.useHelpBuilder(false)
			.addCommands(
				new EvalCommand(),
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
				// Utilities
				new AvatarCommand(),
				new EmojiCommand());

        JDABuilder
			.createDefault(Config.getString("TOKEN"),
				GatewayIntent.GUILD_MEMBERS,
				GatewayIntent.GUILD_MESSAGES,
				GatewayIntent.GUILD_INVITES,
				GatewayIntent.GUILD_PRESENCES,
				GatewayIntent.DIRECT_MESSAGES,
				GatewayIntent.GUILD_EMOJIS)
			.enableCache(CacheFlag.ONLINE_STATUS, CacheFlag.ACTIVITY, CacheFlag.EMOTE)
			.disableCache(CacheFlag.VOICE_STATE)
			.setBulkDeleteSplittingEnabled(false)
			.setMemberCachePolicy(MemberCachePolicy.ALL)
			.addEventListeners(builder.build(),
				new InviteEvents(),
				new ChannelEvents(),
				new MemberEvents(),
				new MessageEvents())
			.build();
    }
}
