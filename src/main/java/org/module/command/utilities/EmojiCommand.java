package org.module.command.utilities;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.emoji.RichCustomEmoji;
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

        RichCustomEmoji emoji = findEmote(args, ctx.getGuild());
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

	private static RichCustomEmoji findEmote(String query, Guild guild) {
		Matcher mentionMatcher = Pattern.compile("<:(.{2,32}):(\\d{17,20})>").matcher(query);

		if (Pattern.compile("\\d{17,20}").matcher(query).matches()) {
			RichCustomEmoji emote = guild.getEmojiById(query);
			if (emote != null) return emote;
		} else if (mentionMatcher.matches()) {
			String emoteName = mentionMatcher.group(1);
			String emoteId = mentionMatcher.group(2);
			RichCustomEmoji emote = guild.getEmojiById(emoteId);
			if (emote != null && emote.getName().equals(emoteName)) return emote;
		}

		ArrayList<RichCustomEmoji> exact = new ArrayList<>();
		ArrayList<RichCustomEmoji> wrongCase = new ArrayList<>();
		ArrayList<RichCustomEmoji> startsWith = new ArrayList<>();
		ArrayList<RichCustomEmoji> contains = new ArrayList<>();
		String lowerQuery = query.toLowerCase();

		guild.getEmojiCache().forEach(emote -> {
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
