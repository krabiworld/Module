package org.module.controller;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import org.json.JSONObject;
import org.module.configuration.BotConfiguration;
import org.module.service.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatsController {
	private final StatsService statsService;

	@Autowired
	public StatsController(StatsService statsService) {
		this.statsService = statsService;
	}

	@GetMapping(value = "/stats", produces = MediaType.APPLICATION_JSON_VALUE)
	public String stats() {
		JDA jda = BotConfiguration.jda;

		long channelsCount = 0;

		for (Guild guild : jda.getGuilds()) {
			channelsCount += guild.getChannels().size();
		}

		return new JSONObject()
			.put("guilds", jda.getGuilds().size())
			.put("users", jda.getUsers().size())
			.put("channels", channelsCount)
			.put("executedCommands", statsService.getStats().getExecutedCommands())
			.put("shards", jda.getShardInfo().getShardTotal())
			.toString();
	}
}
