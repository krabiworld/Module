/*
 * This file is part of Module.

 * Module is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * Module is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with Module. If not, see <https://www.gnu.org/licenses/>.
 */

package org.module.events;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.command.CommandListener;
import com.jagrosh.jdautilities.command.SlashCommand;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import org.module.service.StatsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommandEvents implements CommandListener {
	private final Logger logger = LoggerFactory.getLogger(CommandEvents.class);
	private final StatsService statsService;

	@Autowired
	public CommandEvents(StatsService statsService) {
		this.statsService = statsService;
	}

	@Override
	public void onCommand(CommandEvent event, Command command) {
		statsService.incrementCommandsExecuted();
	}

	@Override
	public void onSlashCommand(SlashCommandEvent event, SlashCommand command) {
		statsService.incrementCommandsExecuted();
	}

	@Override
	public void onCommandException(CommandEvent event, Command command, Throwable throwable) {
		logger.error(String.format("Exception in command %s.", command.getName()));
		throwable.printStackTrace();
	}

	@Override
	public void onSlashCommandException(SlashCommandEvent event, SlashCommand command, Throwable throwable) {
		logger.error(String.format("Exception in slash command %s.", command.getName()));
		throwable.printStackTrace();
	}
}
