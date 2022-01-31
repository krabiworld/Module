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
import net.dv8tion.jda.api.entities.Member;

import java.util.Collections;

public class AvatarCommand extends SlashCommand {
	public AvatarCommand() {
		this.name = PropertyUtil.getProperty("command.avatar.name");
		this.help = PropertyUtil.getProperty("command.avatar.help");
		this.arguments = PropertyUtil.getProperty("command.avatar.arguments");
		this.options = Collections.singletonList(new OptionData(
			OptionType.USER, "user", "User to get avatar."
		));
        this.category = Constants.UTILITIES;
    }

    @Override
    protected void execute(CommandEvent event) {
		Member member = event.getMember();

        if (!event.getArgs().isEmpty()) {
            member = ArgsUtil.getMember(event, event.getArgs());
        }
        if (member == null) {
			MessageUtil.sendHelp(event, this);
            return;
        }

        event.reply(command(member));
    }

	@Override
	protected void execute(SlashCommandEvent event) {
		OptionMapping option = event.getOption("user");

		if (option == null) {
			event.replyEmbeds(command(event.getMember())).queue();
			return;
		}

		event.replyEmbeds(command(option.getAsMember())).queue();
	}

	private MessageEmbed command(Member member) {
		return new EmbedBuilder()
			.setAuthor("Avatar of " + member.getUser().getName())
			.setColor(member.getColor())
			.setImage(member.getEffectiveAvatarUrl() + "?size=512")
			.build();
	}
}
