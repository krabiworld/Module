/*
 * Module Discord Bot.
 * Copyright (C) 2022 untled032, Headcrab

 * UASM is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * UASM is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with UASM. If not, see https://www.gnu.org/licenses/.
 */

package eu.u032.commands.utilities;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import eu.u032.Constants;
import eu.u032.util.ArgsUtil;
import eu.u032.util.MessageUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emote;

public class EmojiCommand extends Command {
    public EmojiCommand() {
		this.name = MessageUtil.getMessage("command.emoji.name");
		this.help = MessageUtil.getMessage("command.emoji.help");
		this.arguments = MessageUtil.getMessage("command.emoji.arguments");
        this.category = Constants.UTILITIES;
    }

    @Override
    protected void execute(CommandEvent event) {
		if (event.getArgs().isEmpty()) {
			MessageUtil.sendHelp(event, this);
			return;
		}

        Emote emoji = ArgsUtil.getEmote(event, event.getArgs());

        if (emoji == null) {
			MessageUtil.sendHelp(event, this);
            return;
        }

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Emoji " + emoji.getName(), emoji.getImageUrl())
                .setColor(Constants.COLOR)
                .setImage(emoji.getImageUrl())
                .setFooter("ID: " + emoji.getId());
        event.reply(embed.build());
    }
}
