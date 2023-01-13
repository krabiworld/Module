package org.module.command.utilities;

import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.module.structure.Category;
import org.module.structure.Command;
import org.module.structure.CommandContext;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class RandomCommand extends Command {
	public RandomCommand() {
		this.name = "random";
		this.description = "Returns a random number";
		this.category = Category.UTILITIES;
		this.options.add(
			new OptionData(OptionType.NUMBER, "min", "First number", false)
		);
		this.options.add(
			new OptionData(OptionType.NUMBER, "max", "Second number", false)
		);
	}

	@Override
	protected void execute(CommandContext ctx) {
		int min = ctx.getOptionAsInt("min");
		int max = ctx.getOptionAsInt("max");

		if (min == -1 && max == -1) {
			ctx.reply(random(false, false, 0, 0));
		} else {
			try {
				if (min != -1 && max != -1) {
					ctx.reply(random(true, true, min, max));
					return;
				}
				ctx.reply(random(true, false, min, 0));
			} catch (Exception e) {
				ctx.replyHelp();
			}
		}
	}

	private String random(boolean min, boolean max, int minNumber, int maxNumber) {
		Random random = new Random();
		long result;
		if (min && !max) {
			result = random.nextLong();
		} else if (min) {
			result = Math.round(Math.floor(Math.random() * (maxNumber - minNumber + 1) + minNumber));
		} else {
			result = random.nextInt(1000);
		}
		return String.valueOf(result);
	}
}
