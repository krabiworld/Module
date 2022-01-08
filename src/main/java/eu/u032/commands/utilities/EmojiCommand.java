/*
 * UASM Discord Bot.
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
import eu.u032.Utils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emote;

public class EmojiCommand extends Command {
    public EmojiCommand() {
        this.name = "emoji";
        this.help = "Information about emoji";
        this.arguments = "<@Emoji | ID>";
        this.category = new Category("Utilities");
    }

    @Override
    protected void execute(final CommandEvent event) {
        final String emojiId = Utils.getId(event.getArgs(), Utils.EMOJI);
        final Emote emoji = emojiId.isEmpty() ? null : event.getGuild().getEmoteById(emojiId);

        if (event.getArgs().isEmpty()) {
			Utils.sendError(event, "Required arguments are missing!");
            return;
        }
        if (emoji == null) {
			Utils.sendError(event, "Emoji is not found.");
            return;
        }

        final EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Emoji " + emoji.getName(), emoji.getImageUrl())
                .setColor(Utils.getColor())
                .setImage(emoji.getImageUrl())
                .setFooter("ID: " + emoji.getId());
        event.reply(embed.build());
    }
}
