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

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.module.Constants;
import org.module.Locale;
import org.module.service.MessageService;
import org.module.util.ArgsUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmojiCommand extends Command {
	private final MessageService messageService;

	@Autowired
	public EmojiCommand(MessageService messageService) {
		this.messageService = messageService;
		this.name = "emoji";
        this.category = Constants.UTILITIES;
    }

    @Override
    protected void execute(CommandEvent event) {
		Locale locale = messageService.getLocale(event.getGuild());
		if (event.getArgs().isEmpty()) {
			messageService.sendHelp(event, this, locale);
			return;
		}
        Emote emoji = ArgsUtil.getEmote(event, event.getArgs());
        if (emoji == null) {
			messageService.sendHelp(event, this, locale);
            return;
        }

		MessageEmbed embed = new EmbedBuilder()
			.setTitle("Emoji " + emoji.getName(), emoji.getImageUrl())
			.setColor(Constants.DEFAULT)
			.setImage(emoji.getImageUrl())
			.setFooter("ID: " + emoji.getId())
			.build();

        event.reply(embed);
    }
}
