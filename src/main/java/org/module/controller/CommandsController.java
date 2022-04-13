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

import org.json.JSONObject;
import org.module.Locale;
import org.module.configuration.BotConfiguration;
import org.module.structure.Command;
import org.module.structure.CommandClient;
import org.module.util.LocaleUtil;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.List;

@RestController
public class CommandsController {
	@GetMapping(value = "/commands/{lang}", produces = MediaType.APPLICATION_JSON_VALUE)
	public String commands(@PathVariable String lang) {
		Locale locale = LocaleUtil.getLocale(!lang.equals("ru") ? "en" : "ru");

		CommandClient client = BotConfiguration.commandClient;

		JSONObject json = new JSONObject();
		JSONObject jsonCategories = new JSONObject();
		List<Command> commands = new LinkedList<>();
		List<String> categories = new LinkedList<>();

		client.getCommands().forEach(cmd -> commands.add(cmd.getAnnotation()));

		categoriesLoop:
		for (Command cmd : commands) {
			if (cmd.hidden()) continue;
			for (var category : categories) {
				if (category.equals(cmd.category())) continue categoriesLoop;
			}
			categories.add(cmd.category());
		}

		for (var category : categories) {
			JSONObject jsonCategory = new JSONObject().put("name", category);
			JSONObject jsonCommands = new JSONObject();

			List<Command> filteredCommands = commands.stream()
				.filter(cmd -> cmd.category().equalsIgnoreCase(category)).toList();
			for (Command command : filteredCommands) {
				jsonCommands.put(command.name().toLowerCase(), new JSONObject()
					.put("name", locale.get(command.name()))
					.put("help", locale.get(command.help()))
					.put("args", locale.get(command.args()))
					.put("dialog", false));
			}

			jsonCategory.put("commands", jsonCommands);
			jsonCategories.put(category.toLowerCase(), jsonCategory);
		}

		json.put("commands", client.getCommands().size());
		json.put("categories", jsonCategories);

		return json.toString();
	}
}
