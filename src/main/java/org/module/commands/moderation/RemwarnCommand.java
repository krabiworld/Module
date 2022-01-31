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

package org.module.commands.moderation;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import org.module.model.Warn;
import org.module.service.ModerationService;
import org.module.service.impl.ModerationServiceImpl;
import org.module.util.MessageUtil;
import org.module.util.PropertyUtil;
import net.dv8tion.jda.api.entities.Member;

import static org.module.Constants.*;

public class RemwarnCommand extends Command {
	private final ModerationService moderationService = new ModerationServiceImpl();

	public RemwarnCommand() {
		this.name = PropertyUtil.getProperty("command.remwarn.name");
		this.help = PropertyUtil.getProperty("command.remwarn.help");
		this.arguments = PropertyUtil.getProperty("command.remwarn.arguments");
		this.category = MODERATION;
	}

	@Override
	protected void execute(CommandEvent event) {
		if (!moderationService.isModerator(event.getMember())) {
			MessageUtil.sendError(event, "error.not.mod");
			return;
		}
		if (event.getArgs().isEmpty()) {
			MessageUtil.sendHelp(event, this);
			return;
		}

		Warn warn = moderationService.getWarn(Long.parseLong(event.getArgs()));

		if (warn == null || warn.getGuild() != event.getGuild().getIdLong()) {
			MessageUtil.sendError(event, "command.remwarn.error.not.found");
			return;
		}

		Member member = event.getGuild().getMemberById(warn.getUser());

		if (member == null) {
			MessageUtil.sendHelp(event, this);
			return;
		}

		moderationService.removeWarn(warn);

		MessageUtil.sendSuccess(event, "command.remwarn.success.removed.warn",
			event.getMember().getEffectiveName(),
			warn.getId(),
			member.getUser().getAsTag());
	}
}
