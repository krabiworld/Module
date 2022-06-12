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

package org.module.command.moderation;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.module.structure.Category;
import org.module.structure.Command;
import org.module.structure.CommandContext;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
public class SlowmodeCommand extends Command {
	public SlowmodeCommand() {
		this.name = "slowmode";
		this.description = "Set slowmode in current channel";
		this.category = Category.MODERATION;
		this.moderationCommand = true;
		this.options.add(new OptionData(
			OptionType.INTEGER, "duration", "Duration", true
		));
		this.botPermissions = new Permission[]{Permission.MANAGE_CHANNEL};
		this.userPermissions = new Permission[]{Permission.MANAGE_CHANNEL};
	}

    @Override
    protected void execute(CommandContext ctx) {
		int interval = ctx.getOptionAsInt("duration");

        if (interval < 0 || interval > 21600) {
			ctx.replyHelp();
            return;
        }
        if (ctx.getTextChannel().getSlowmode() == interval) {
			ctx.replyError("This value already set.");
            return;
        }

        ctx.getTextChannel().getManager().setSlowmode(interval).queue();
		ctx.replySuccess(MessageFormat.format("Slowmode for channel {0} changed to **{1}**.",
			ctx.getTextChannel().getAsMention(), interval));
    }
}
