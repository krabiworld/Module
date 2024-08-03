package org.module.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.module.configuration.BotConfiguration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommandsController {
	@GetMapping(value = "/commands", produces = MediaType.APPLICATION_JSON_VALUE)
	public String commands() {
		var client = BotConfiguration.commandClient;

		JsonObject json = new JsonObject();
		JsonObject jsonCategories = new JsonObject();

		var commands = client.getCommands().stream()
			.filter(cmd -> cmd.getCategory() != null)
			.toList();
		var categories = client.getCategories();

		for (var category : categories) {
			var jsonCommands = new JsonObject();
			var jsonCategory = new JsonObject();
			jsonCategory.addProperty("name", category.getName());

			commands.stream()
				.filter(cmd -> cmd.getCategory().getName().equalsIgnoreCase(category.getName()))
				.forEach(cmd -> {
					var command = new JsonObject();
					command.addProperty("name", cmd.getName());
					command.addProperty("description", cmd.getDescription());
					command.addProperty("dialog", false);

					jsonCommands.add(cmd.getName().toLowerCase(), command);
				});

			jsonCategory.add("commands", jsonCommands);
			jsonCategories.add(category.getName().toLowerCase(), jsonCategory);
		}

		json.addProperty("commands", client.getCommands().size());
		json.add("categories", jsonCategories);

		return new Gson().toJson(json);
	}
}
