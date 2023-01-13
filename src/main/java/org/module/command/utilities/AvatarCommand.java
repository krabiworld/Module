package org.module.command.utilities;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.module.structure.Category;
import org.module.structure.Command;
import org.module.structure.CommandContext;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.text.MessageFormat;

@Component
public class AvatarCommand extends Command {
	public AvatarCommand() {
		this.name = "avatar";
		this.description = "Show avatar of member";
		this.category = Category.UTILITIES;
		this.options.add(
			new OptionData(OptionType.USER, "user", "User to show avatar", false)
		);
	}

	@Override
    protected void execute(CommandContext ctx) {
		User user = ctx.getOptionAsUser("user", ctx.getUser());

        if (user == null) {
			ctx.replyHelp();
            return;
        }

		User.Profile profile = user.retrieveProfile().complete();
		Color color;
		if (profile.getAccentColor() == null) {
			color = ctx.getGuild().retrieveMember(user).complete().getColor();
		} else {
			color = profile.getAccentColor();
		}

		MessageEmbed embed = new EmbedBuilder()
			.setAuthor(MessageFormat.format("Avatar of {0}", user.getName()))
			.setColor(color)
			.setImage(user.getEffectiveAvatarUrl() + "?size=512")
			.build();

        ctx.reply(embed);
    }
}
