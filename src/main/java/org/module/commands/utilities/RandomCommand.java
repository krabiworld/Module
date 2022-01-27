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

package org.module.commands.utilities;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import org.module.constants.Constants;
import org.module.service.MessageService;
import org.module.util.ArgsUtil;
import org.module.util.PropertyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class RandomCommand extends Command {
	private final Logger logger = LoggerFactory.getLogger(RandomCommand.class);
	private final MessageService messageService;

	@Autowired
	public RandomCommand(MessageService messageService) {
		this.messageService = messageService;
		this.name = PropertyUtil.getProperty("command.random.name");
		this.help = PropertyUtil.getProperty("command.random.help");
		this.arguments = PropertyUtil.getProperty("command.random.arguments");
		this.category = Constants.UTILITIES;
	}

	@Override
	protected void execute(CommandEvent event) {
		String[] args = ArgsUtil.split(event.getArgs());
		Random random = new Random();

		if (event.getArgs().isEmpty()) {
			event.reply(String.valueOf(random.nextInt(1000)));
		} else {
			try {
				if (args.length > 1) {
					long min = Long.parseLong(args[0]);
					long max = Long.parseLong(args[1]);
					event.reply(String.valueOf(Math.round(Math.floor(Math.random() * (max - min + 1) + min))));
					return;
				}
				event.reply(String.valueOf(random.nextLong(Long.parseLong(args[0]))));
			} catch (Exception e) {
				logger.error(e.getMessage());
				messageService.sendHelp(event, this);
			}
		}
	}
}
