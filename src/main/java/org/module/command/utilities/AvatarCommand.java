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
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.module.structure.AbstractCommand;
import org.module.structure.Command;
import org.module.structure.CommandContext;
import org.springframework.stereotype.Component;

@Component
@Command(
	name = "command.avatar.name",
	args = "command.avatar.args",
	help = "command.avatar.help",
	category = "category.utilities"
)
public class AvatarCommand extends AbstractCommand {
	@Override
    protected void execute(CommandContext ctx) {
		Member member = ctx.getMember();

        if (!ctx.getArgs().isEmpty()) {
            member = ctx.findMember(ctx.getArgs());
        }
        if (member == null) {
			ctx.sendHelp();
            return;
        }

		MessageEmbed embed = new EmbedBuilder()
			.setAuthor(ctx.get("command.avatar.title", member.getUser().getName()))
			.setColor(member.getColor())
			.setImage(member.getEffectiveAvatarUrl() + "?size=512")
			.build();

        ctx.send(embed);
    }
}
