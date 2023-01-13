package org.module.listeners;

import org.module.manager.CacheManager;
import org.module.service.StatsService;
import org.module.structure.CommandListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommandListener implements CommandListenerAdapter {
	private final StatsService statsService;

	@Autowired
	public CommandListener(StatsService statsService) {
		this.statsService = statsService;
	}

	@Override
	public void onCommand() {
		CacheManager.incrementExecutedCommands();

		if (CacheManager.checkExecutedCommands()) {
			statsService.incrementExecutedCommands(CacheManager.getExecutedCommands());
			CacheManager.resetExecutedCommands();
		}
	}
}
