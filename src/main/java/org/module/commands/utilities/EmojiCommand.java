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

package org.module.commands.utilities;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.module.Constants;
import org.module.util.ArgsUtil;
import org.module.util.MessageUtil;
import org.module.util.PropertyUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emote;

import java.util.Collections;

public class EmojiCommand extends SlashCommand {
	public EmojiCommand() {
		this.name = PropertyUtil.getProperty("command.emoji.name");
		this.help = PropertyUtil.getProperty("command.emoji.help");
		this.arguments = PropertyUtil.getProperty("command.emoji.arguments");
		this.options = Collections.singletonList(new OptionData(
			OptionType.STRING, "emoji", "Only custom emojis!", true
		));
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
        event.reply(command(emoji));
    }

	@Override
	protected void execute(SlashCommandEvent event) {
		OptionMapping option = event.getOption("emoji");
		Emote emoji = ArgsUtil.getEmote(event, option.getAsString());
		if (emoji == null) {
			MessageUtil.sendEphemeralHelp(event, this);
			return;
		}
		event.replyEmbeds(command(emoji)).queue();
	}

	private MessageEmbed command(Emote emoji) {
		return new EmbedBuilder()
			.setTitle("Emoji " + emoji.getName(), emoji.getImageUrl())
			.setColor(Constants.DEFAULT)
			.setImage(emoji.getImageUrl())
			.setFooter("ID: " + emoji.getId())
			.build();
	}
}
