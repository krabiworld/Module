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
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.module.Constants;
import org.module.structure.Category;
import org.module.structure.Command;
import org.module.structure.CommandContext;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class EmojiCommand extends Command {
	public EmojiCommand() {
		this.name = "emoji";
		this.description = "Information about emoji (Only custom emojis)";
		this.category = Category.UTILITIES;
		this.options.add(
			new OptionData(OptionType.STRING, "emoji", "Only custom emoji", true)
		);
	}

    @Override
    protected void execute(CommandContext ctx) {
		String args = ctx.getOptionAsString("emoji");

        Emote emoji = findEmote(args, ctx.getGuild());
        if (emoji == null) {
			ctx.replyHelp();
            return;
        }

		MessageEmbed embed = new EmbedBuilder()
			.setTitle(MessageFormat.format("Emoji {0}", emoji.getName()), emoji.getImageUrl())
			.setColor(Constants.DEFAULT)
			.setImage(emoji.getImageUrl())
			.setFooter("ID: " + emoji.getId())
			.build();

        ctx.reply(embed);
    }

	private static Emote findEmote(String query, Guild guild) {
		Matcher mentionMatcher = Pattern.compile("<:(.{2,32}):(\\d{17,20})>").matcher(query);

		if (Pattern.compile("\\d{17,20}").matcher(query).matches()) {
			Emote emote = guild.getEmoteById(query);
			if (emote != null) return emote;
		} else if (mentionMatcher.matches()) {
			String emoteName = mentionMatcher.group(1);
			String emoteId = mentionMatcher.group(2);
			Emote emote = guild.getEmoteById(emoteId);
			if (emote != null && emote.getName().equals(emoteName)) return emote;
		}

		ArrayList<Emote> exact = new ArrayList<>();
		ArrayList<Emote> wrongCase = new ArrayList<>();
		ArrayList<Emote> startsWith = new ArrayList<>();
		ArrayList<Emote> contains = new ArrayList<>();
		String lowerQuery = query.toLowerCase();

		guild.getEmoteCache().forEach(emote -> {
			String name = emote.getName();
			if (name.equals(query)) exact.add(emote);
			else if (name.equalsIgnoreCase(query) && exact.isEmpty()) wrongCase.add(emote);
			else if (name.toLowerCase().startsWith(lowerQuery) && wrongCase.isEmpty()) startsWith.add(emote);
			else if (name.toLowerCase().contains(lowerQuery) && startsWith.isEmpty()) contains.add(emote);
		});

		if (!exact.isEmpty()) return exact.stream().findFirst().orElse(null);
		if (!wrongCase.isEmpty()) return wrongCase.stream().findFirst().orElse(null);
		if (!startsWith.isEmpty()) return startsWith.stream().findFirst().orElse(null);
		return contains.stream().findFirst().orElse(null);
	}
}
