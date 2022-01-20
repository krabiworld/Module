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

package org.module.commands.utilities;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import org.module.Constants;
import org.module.service.MessageService;
import org.module.util.ArgsUtil;
import org.module.util.PropertyUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmojiCommand extends Command {
	@Autowired
	private MessageService messageService;

    public EmojiCommand() {
		this.name = PropertyUtil.getProperty("command.emoji.name");
		this.help = PropertyUtil.getProperty("command.emoji.help");
		this.arguments = PropertyUtil.getProperty("command.emoji.arguments");
        this.category = Constants.UTILITIES;
    }

    @Override
    protected void execute(CommandEvent event) {
		if (event.getArgs().isEmpty()) {
			messageService.sendHelp(event, this);
			return;
		}

        Emote emoji = ArgsUtil.getEmote(event, event.getArgs());

        if (emoji == null) {
			messageService.sendHelp(event, this);
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
