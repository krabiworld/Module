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

package org.module.command.utilities;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.module.Constants;
import org.module.structure.AbstractCommand;
import org.module.structure.Command;
import org.module.structure.CommandContext;
import org.springframework.stereotype.Component;

@Component
@Command(
	name = "command.emoji.name",
	args = "command.emoji.args",
	help = "command.emoji.help",
	category = "category.utilities"
)
public class EmojiCommand extends AbstractCommand {
    @Override
    protected void execute(CommandContext ctx) {
		if (ctx.getArgs().isEmpty()) {
			ctx.sendHelp();
			return;
		}

        Emote emoji = ctx.findEmote(ctx.getArgs());
        if (emoji == null) {
			ctx.sendHelp();
            return;
        }

		MessageEmbed embed = new EmbedBuilder()
			.setTitle(ctx.get("command.emoji.title", emoji.getName()), emoji.getImageUrl())
			.setColor(Constants.DEFAULT)
			.setImage(emoji.getImageUrl())
			.setFooter("ID: " + emoji.getId())
			.build();

        ctx.send(embed);
    }
}
