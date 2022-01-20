package org.module.commands.utilities;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import org.module.service.MessageService;
import org.module.util.ArgsUtil;
import org.module.util.PropertyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class RandomCommand extends Command {
	@Autowired
	private MessageService messageService;

	public RandomCommand() {
		this.name = PropertyUtil.getProperty("command.random.name");
		this.help = PropertyUtil.getProperty("command.random.help");
		this.arguments = PropertyUtil.getProperty("command.random.arguments");
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
				System.out.println(e.getMessage());
				messageService.sendHelp(event, this);
			}
		}
	}
}
