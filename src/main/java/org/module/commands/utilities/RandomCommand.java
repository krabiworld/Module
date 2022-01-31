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

package org.module.commands.utilities;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.module.Constants;
import org.module.util.ArgsUtil;
import org.module.util.MessageUtil;
import org.module.util.PropertyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomCommand extends SlashCommand {
	private final Logger logger = LoggerFactory.getLogger(RandomCommand.class);

	public RandomCommand() {
		this.name = PropertyUtil.getProperty("command.random.name");
		this.help = PropertyUtil.getProperty("command.random.help");
		this.arguments = PropertyUtil.getProperty("command.random.arguments");
		List<OptionData> options = new ArrayList<>();
		options.add(new OptionData(OptionType.NUMBER, "minimum", "Minimum number"));
		options.add(new OptionData(OptionType.NUMBER, "maximum", "Maximum number"));
		this.options = options;
		this.category = Constants.UTILITIES;
	}

	@Override
	protected void execute(CommandEvent event) {
		String[] args = ArgsUtil.split(event.getArgs());

		if (event.getArgs().isEmpty()) {
			event.reply(String.valueOf(command(false, false, 0, 0)));
		} else {
			try {
				if (args.length > 1) {
					long min = Long.parseLong(args[0]);
					long max = Long.parseLong(args[1]);
					event.reply(String.valueOf(command(true, true, min, max)));
					return;
				}
				event.reply(String.valueOf(command(true, false, Long.parseLong(args[0]), 0)));
			} catch (Exception e) {
				logger.error(e.getMessage());
				MessageUtil.sendHelp(event, this);
			}
		}
	}

	@Override
	protected void execute(SlashCommandEvent event) {
		OptionMapping minimum = event.getOption("minimum");
		OptionMapping maximum = event.getOption("maximum");

		try {
			if (minimum != null && maximum == null) {
				event.reply(String.valueOf(
					command(true, false, Long.parseLong(minimum.getAsString()), 0))).queue();
				return;
			} else if (minimum == null && maximum != null) {
				event.reply(String.valueOf(
					command(true, false, Long.parseLong(maximum.getAsString()), 0))).queue();
				return;
			} else if (minimum != null) {
				event.reply(String.valueOf(
					command(true, true,
						Long.parseLong(minimum.getAsString()), Long.parseLong(maximum.getAsString())))).queue();
				return;
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			MessageUtil.sendEphemeralHelp(event, this);
		}

		event.reply(String.valueOf(command(false, false, 0, 0))).queue();
	}

	private long command(boolean min, boolean max, long minNumber, long maxNumber) {
		Random random = new Random();
		if (min && !max) {
			return random.nextLong(minNumber);
		} else if (min) {
			return Math.round(Math.floor(Math.random() * (maxNumber - minNumber + 1) + minNumber));
		}
		return random.nextInt(1000);
	}
}
