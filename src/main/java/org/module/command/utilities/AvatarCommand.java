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
