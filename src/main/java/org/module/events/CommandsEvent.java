package org.module.events;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.command.CommandListener;
import org.module.service.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommandsEvent implements CommandListener {
	@Autowired
	private StatsService statsService;

	@Override
	public void onCommand(CommandEvent event, Command command) {
		statsService.incrementExecutedCommands();
	}
}
