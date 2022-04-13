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

import net.dv8tion.jda.api.EmbedBuilder;
import org.module.Constants;
import org.module.structure.AbstractCommand;
import org.module.structure.Command;
import org.module.structure.CommandContext;
import org.module.util.EmbedUtil;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
@Command(
	name = "command.help.name",
	args = "command.help.args",
	help = "command.help.help",
	category = "category.information"
)
public class HelpCommand extends AbstractCommand {
    @Override
    protected void execute(CommandContext ctx) {
		String args = ctx.getArgs();
		String prefix = ctx.getPrefix();
        EmbedBuilder embed = new EmbedBuilder().setColor(Constants.DEFAULT);
		List<Command> commands = new LinkedList<>();
		List<String> categories = new LinkedList<>();

        categoriesLoop:
        for (AbstractCommand abstractCommand : ctx.getCommands()) {
			Command command = abstractCommand.getAnnotation();
			commands.add(command);

			if (command.hidden()) continue;
            for (String category : categories) {
                // if command already exists in "categories" - continue
                if (category.equals(ctx.get(command.category()))) continue categoriesLoop;
            }
            categories.add(ctx.get(command.category()));
        }

        // if "args" is empty - getTemplate all commands
        if (args.isEmpty()) {
            StringBuilder commandsBuilder = new StringBuilder();
            embed.setTitle(ctx.get("command.help.title"));
			embed.setDescription(ctx.get("command.help.description", prefix, prefix));

            for (String category : categories) {
                for (Command cmd : commands) {
                    if (cmd.hidden()) continue;
                    if (ctx.get(cmd.category()).equals(category)) {
                        commandsBuilder.append("`")
							.append(prefix)
							.append(ctx.get(cmd.name()))
							.append("` ");
                    }
                }
                embed.addField(category + " (" + prefix + "help " + category + ")",
					commandsBuilder.toString(), false);
                commandsBuilder = new StringBuilder();
            }

            ctx.send(embed);
        } else {
            for (String category : categories) {
                // if match found with name of category
                if (category.toLowerCase().startsWith(args.toLowerCase())) {
                    for (Command cmd : commands) {
                        if (cmd.hidden()) continue;
                        if (ctx.get(cmd.category()).equals(category)) {
							String name = ctx.get(cmd.name());
							String help = ctx.get(cmd.help());
							embed.addField(prefix + name, help, false);
						}
                    }
                    embed.setTitle(ctx.get("command.help.category.title", category));
                    ctx.send(embed);
                    return;
                }
            }
            for (Command cmd : commands) {
                // if match found with name of command
                if (ctx.get(cmd.name()).toLowerCase().startsWith(args.toLowerCase()) && !cmd.hidden()) {
					EmbedUtil helpEmbed = new EmbedUtil(cmd, ctx.getLocale(), prefix);
					ctx.send(helpEmbed);
					return;
                }
            }

			ctx.sendError("command.help.error.not.found", args);
        }
    }
}
