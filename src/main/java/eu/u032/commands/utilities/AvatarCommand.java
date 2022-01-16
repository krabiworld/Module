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
import eu.u032.Constants;
import eu.u032.util.ArgsUtil;
import eu.u032.util.MessageUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;

public class AvatarCommand extends Command {
    public AvatarCommand() {
		this.name = MessageUtil.getMessage("command.avatar.name");
		this.help = MessageUtil.getMessage("command.avatar.help");
		this.arguments = MessageUtil.getMessage("command.avatar.arguments");
        this.category = Constants.UTILITIES;
    }

    @Override
    protected void execute(final CommandEvent event) {
		Member member = event.getMember();

        if (!event.getArgs().isEmpty()) {
            member = ArgsUtil.getMember(event, event.getArgs());
        }
        if (member == null) {
            MessageUtil.sendError(event, "error.member.not.found");
            return;
        }

        final EmbedBuilder embed = new EmbedBuilder()
                .setAuthor("Avatar of " + member.getUser().getName())
                .setColor(member.getColor())
                .setImage(member.getEffectiveAvatarUrl() + "?size=512");
        event.reply(embed.build());
    }
}
