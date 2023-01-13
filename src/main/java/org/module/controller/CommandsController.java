package org.module.controller;

import org.json.JSONObject;
import org.module.configuration.BotConfiguration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommandsController {
	@GetMapping(value = "/commands", produces = MediaType.APPLICATION_JSON_VALUE)
	public String commands() {
		var client = BotConfiguration.commandClient;

		var json = new JSONObject();
		var jsonCategories = new JSONObject();
		var commands = client.getCommands().stream()
			.filter(cmd -> cmd.getCategory() != null)
			.toList();
		var categories = client.getCategories();

		for (var category : categories) {
			var jsonCommands = new JSONObject();
			var jsonCategory = new JSONObject().put("name", category.getName());

			commands.stream()
				.filter(cmd -> cmd.getCategory().getName().equalsIgnoreCase(category.getName()))
				.forEach(cmd -> jsonCommands
					.put(cmd.getName().toLowerCase(), new JSONObject()
					.put("name", cmd.getName())
					.put("description", cmd.getDescription())
					.put("dialog", false)));

			jsonCategory.put("commands", jsonCommands);
			jsonCategories.put(category.getName().toLowerCase(), jsonCategory);
		}

		json.put("commands", client.getCommands().size());
		json.put("categories", jsonCategories);

		return json.toString();
	}
}
