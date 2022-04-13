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

import org.module.structure.AbstractCommand;
import org.module.structure.Command;
import org.module.structure.CommandContext;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
@Command(
	name = "command.random.name",
	args = "command.random.args",
	help = "command.random.help",
	category = "category.utilities"
)
public class RandomCommand extends AbstractCommand {
	@Override
	protected void execute(CommandContext ctx) {
		String[] args = ctx.splitArgs();

		if (ctx.getArgs().isEmpty()) {
			ctx.send(random(false, false, 0, 0));
		} else {
			try {
				if (args.length > 1) {
					long min = Long.parseLong(args[0]);
					long max = Long.parseLong(args[1]);
					ctx.send(random(true, true, min, max));
					return;
				}
				ctx.send(random(true, false, Long.parseLong(args[0]), 0));
			} catch (Exception e) {
				ctx.sendHelp();
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
