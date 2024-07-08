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
			new OptionData(OptionType.INTEGER, "min", "Minimum number", false)
		);
		this.options.add(
			new OptionData(OptionType.INTEGER, "max", "Maximum number", false)
		);
	}

	@Override
	protected void execute(CommandContext ctx) {
		int min = ctx.getOptionAsInt("min", 1);
		int max = ctx.getOptionAsInt("max", 100);

		if (min >= max) {
			ctx.replyError("Min should be less than Max.");
			return;
		}

		long number = new Random().nextLong(min, max);

		ctx.reply(String.valueOf(number));
	}
}
