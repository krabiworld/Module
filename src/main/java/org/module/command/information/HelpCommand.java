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

package org.module.command.information;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import org.module.Constants;
import org.module.Locale;
import org.module.manager.GuildManager;
import org.module.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class HelpCommand extends Command {
	private final GuildManager manager;
	private final MessageService messageService;

	@Autowired
	public HelpCommand(GuildManager manager, MessageService messageService) {
		this.manager = manager;
		this.messageService = messageService;
        this.name = "help";
        this.category = Constants.INFORMATION;
    }

    @Override
    protected void execute(CommandEvent event) {
		Locale locale = messageService.getLocale(event.getGuild());
		String args = event.getArgs();
		GuildManager.GuildSettings settings = manager.getSettings(event.getGuild());
		if (settings == null) return;
		String prefix = settings.getPrefix();
        List<Command> commands = event.getClient().getCommands();
        EmbedBuilder embed = new EmbedBuilder().setColor(Constants.DEFAULT);
        List<String> categories = new LinkedList<>();

        categoriesLoop:
        for (Command cmd : commands) {
			if (cmd.isHidden()) continue;
            for (String category : categories) {
                // if command already exists in "categories" - continue
                if (category.equals(cmd.getCategory().getName())) continue categoriesLoop;
            }
            categories.add(cmd.getCategory().getName());
        }

        // if "args" is empty - getTemplate all commands
        if (args.isEmpty()) {
            StringBuilder commandsBuilder = new StringBuilder();
            embed.setTitle(locale.get("command.help.title"));
			embed.setDescription(locale.get("command.help.description", prefix, prefix));

            for (String category : categories) {
                for (Command cmd : commands) {
                    if (cmd.isHidden()) continue;
                    if (cmd.getCategory().getName().equals(category)) {
                        commandsBuilder.append("`")
							.append(prefix)
							.append(cmd.getName())
							.append("` ");
                    }
                }
                embed.addField(category + " (" + prefix + "help " + category + ")",
					commandsBuilder.toString(), false);
                commandsBuilder = new StringBuilder();
            }

            event.reply(embed.build());
        } else {
            for (String category : categories) {
                // if match found with name of category
                if (category.toLowerCase().startsWith(args.toLowerCase())) {
                    for (Command cmd : commands) {
                        if (cmd.isHidden()) continue;
                        if (cmd.getCategory().getName().equals(category)) {
							String help = locale.get(String.format("command.%s.help", cmd.getName()));
							embed.addField(prefix + cmd.getName(), help, false);
						}
                    }
                    embed.setTitle(locale.get("command.help.category.title", category));
                    event.reply(embed.build());
                    return;
                }
            }
            for (Command cmd : commands) {
                // if match found with name of command
                if (cmd.getName().toLowerCase().startsWith(args.toLowerCase()) && !cmd.isHidden()) {
					messageService.sendHelp(event, cmd, locale);
					return;
                }
            }

			messageService.sendError(event, locale, "command.help.error.not.found", args);
        }
    }
}
