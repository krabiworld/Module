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

package org.module.commands.settings;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.module.Constants;
import org.module.manager.GuildManager;
import org.module.util.MessageUtil;
import org.module.util.PropertyUtil;
import net.dv8tion.jda.api.Permission;

import java.util.Collections;

public class PrefixCommand extends SlashCommand {
	private final GuildManager manager = new GuildManager();

	public PrefixCommand() {
		this.name = PropertyUtil.getProperty("command.prefix.name");
		this.help = PropertyUtil.getProperty("command.prefix.help");
		this.arguments = PropertyUtil.getProperty("command.prefix.arguments");
		this.options = Collections.singletonList(new OptionData(
			OptionType.STRING, "prefix", "New prefix", true
		));
		this.category = Constants.SETTINGS;
		this.userPermissions = new Permission[]{Permission.MANAGE_SERVER};
	}

	@Override
	protected void execute(CommandEvent event) {
		if (changePrefix(event.getGuild(), event.getArgs())) {
			MessageUtil.sendSuccess(event, "command.prefix.success.changed", event.getArgs());
			return;
		}
		MessageUtil.sendError(event, "command.prefix.error.length");
	}

	@Override
	protected void execute(SlashCommandEvent event) {
		String prefix = event.getOption("prefix").getAsString();

		if (changePrefix(event.getGuild(), prefix)) {
			MessageUtil.sendSuccess(event, "command.prefix.success.changed", prefix);
			return;
		}

		MessageUtil.sendError(event, "command.prefix.error.length");
	}

	private boolean changePrefix(Guild guild, String prefix) {
		if (prefix.length() < 1 || prefix.length() > 4) {
			return false;
		}
		manager.setPrefix(guild, prefix);
		return true;
	}
}
