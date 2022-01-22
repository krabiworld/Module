/*
 * Module Discord Bot.
 * Copyright (C) 2022 untled032, Headcrab

 * Module is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * Module is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with Module. If not, see https://www.gnu.org/licenses/.
 */

package org.module.commands.utilities;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import org.module.constants.Constants;
import org.module.service.MessageService;
import org.module.util.ArgsUtil;
import org.module.util.PropertyUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AvatarCommand extends Command {
	@Autowired
	private MessageService messageService;

    public AvatarCommand() {
		this.name = PropertyUtil.getProperty("command.avatar.name");
		this.help = PropertyUtil.getProperty("command.avatar.help");
		this.arguments = PropertyUtil.getProperty("command.avatar.arguments");
        this.category = Constants.UTILITIES;
    }

    @Override
    protected void execute(CommandEvent event) {
		Member member = event.getMember();

        if (!event.getArgs().isEmpty()) {
            member = ArgsUtil.getMember(event, event.getArgs());
        }
        if (member == null) {
			messageService.sendHelp(event, this);
            return;
        }

        EmbedBuilder embed = new EmbedBuilder()
                .setAuthor("Avatar of " + member.getUser().getName())
                .setColor(member.getColor())
                .setImage(member.getEffectiveAvatarUrl() + "?size=512");
        event.reply(embed.build());
    }
}
