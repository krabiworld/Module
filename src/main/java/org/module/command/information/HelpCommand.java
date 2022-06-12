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
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.module.Constants;
import org.module.structure.Category;
import org.module.structure.Command;
import org.module.structure.CommandContext;
import org.module.util.EmbedUtil;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
public class HelpCommand extends Command {
	public HelpCommand() {
		this.name = "help";
		this.description = "List of all commands and category";
		this.category = Category.INFORMATION;
		this.options.add(new OptionData(
			OptionType.STRING, "query", "Command or category", false
		));
	}

    @Override
    protected void execute(CommandContext ctx) {
		var query = ctx.getOptionAsString("query");
        var embed = new EmbedBuilder().setColor(Constants.DEFAULT);
		var commands = ctx.getClient().getCommands();
		var categories = ctx.getClient().getCategories();

        // if "args" is empty - getTemplate all commands
        if (query.isEmpty()) {
            var commandsBuilder = new StringBuilder();
            embed.setTitle("Available commands:");
			embed.setDescription("For additional information enter `help category` to get information about category or `help command` to get information about command.");

            for (var category : categories) {
                for (var cmd : commands) {
                    if (cmd.isHidden()) continue;
                    if (cmd.getCategory().getName().equals(category.getName())) {
                        commandsBuilder.append("`")
							.append(cmd.getName())
							.append("` ");
                    }
                }
                embed.addField(MessageFormat.format("{0} (help {0})", category.getName()),
					commandsBuilder.toString(), false);
                commandsBuilder = new StringBuilder();
            }

            ctx.reply(embed.build());
        } else {
            for (var category : categories) {
                // if match found with name of category
                if (category.getName().toLowerCase().startsWith(query.toLowerCase())) {
                    for (var cmd : commands) {
                        if (cmd.isHidden()) continue;
                        if (cmd.getCategory().getName().equals(category.getName())) {
							var name = cmd.getName();
							var help = cmd.getDescription();
							embed.addField(name, help, false);
						}
                    }
                    embed.setTitle(MessageFormat.format("Commands of category {0}", category.getName()));
                    ctx.reply(embed.build());
                    return;
                }
            }
            for (var cmd : commands) {
                // if match found with name of command
                if (cmd.getName().toLowerCase().startsWith(query.toLowerCase()) && !cmd.isHidden()) {
					var helpEmbed = new EmbedUtil(cmd);
					ctx.reply(helpEmbed.build());
					return;
                }
            }

			ctx.replyError(MessageFormat.format("Command or category **{0}** not found.", query));
        }
    }
}
