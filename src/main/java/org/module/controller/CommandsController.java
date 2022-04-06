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

package org.module.controller;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandClient;
import org.json.JSONObject;
import org.module.Locale;
import org.module.configuration.BotConfiguration;
import org.module.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.List;

@RestController
public class CommandsController {
	private final MessageService messageService;

	@Autowired
	public CommandsController(MessageService messageService) {
		this.messageService = messageService;
	}

	@GetMapping(value = "/commands/{lang}", produces = MediaType.APPLICATION_JSON_VALUE)
	public String commands(@PathVariable String lang) {
		Locale locale = messageService.getLocale(!lang.equals("ru") ? "en" : "ru");

		CommandClient client = BotConfiguration.commandClient;

		JSONObject json = new JSONObject();
		JSONObject jsonCategories = new JSONObject();
		List<Command> commands = client.getCommands();
		List<Command.Category> categories = new LinkedList<>();

		categoriesLoop:
		for (Command cmd : commands) {
			if (cmd.isHidden()) continue;
			for (var category : categories) {
				if (category.getName().equals(cmd.getCategory().getName())) continue categoriesLoop;
			}
			categories.add(cmd.getCategory());
		}

		for (var category : categories) {
			JSONObject jsonCategory = new JSONObject().put("name", category.getName());
			JSONObject jsonCommands = new JSONObject();

			List<Command> filteredCommands = commands.stream()
				.filter(cmd -> cmd.getCategory().getName().equalsIgnoreCase(category.getName())).toList();
			for (Command command : filteredCommands) {
				jsonCommands.put(command.getName().toLowerCase(), new JSONObject()
					.put("name", command.getName())
					.put("help", locale.get(String.format("command.%s.help", command.getName())))
					.put("arguments", locale.get(String.format("command.%s.arguments", command.getName())))
					.put("dialog", false));
			}

			jsonCategory.put("commands", jsonCommands);
			jsonCategories.put(category.getName().toLowerCase(), jsonCategory);
		}

		json.put("commands", client.getCommands().size());
		json.put("slashCommands", client.getSlashCommands().size());
		json.put("categories", jsonCategories);

		return json.toString();
	}
}
