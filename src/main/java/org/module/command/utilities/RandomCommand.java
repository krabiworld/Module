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

package org.module.command.utilities;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import org.module.Constants;
import org.module.Locale;
import org.module.service.MessageService;
import org.module.util.ArgsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class RandomCommand extends Command {
	private final MessageService messageService;

	@Autowired
	public RandomCommand(MessageService messageService) {
		this.messageService = messageService;
		this.name = "random";
		this.category = Constants.UTILITIES;
	}

	@Override
	protected void execute(CommandEvent event) {
		Locale locale = messageService.getLocale(event.getGuild());
		String[] args = ArgsUtil.split(event.getArgs());

		if (event.getArgs().isEmpty()) {
			event.reply(random(false, false, 0, 0));
		} else {
			try {
				if (args.length > 1) {
					long min = Long.parseLong(args[0]);
					long max = Long.parseLong(args[1]);
					event.reply(random(true, true, min, max));
					return;
				}
				event.reply(random(true, false, Long.parseLong(args[0]), 0));
			} catch (Exception e) {
				messageService.sendHelp(event, this, locale);
			}
		}
	}

	private String random(boolean min, boolean max, long minNumber, long maxNumber) {
		Random random = new Random();
		long result;
		if (min && !max) {
			result = random.nextLong(minNumber);
		} else if (min) {
			result = Math.round(Math.floor(Math.random() * (maxNumber - minNumber + 1) + minNumber));
		} else {
			result = random.nextInt(1000);
		}
		return String.valueOf(result);
	}
}
