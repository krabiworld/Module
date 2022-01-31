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
import org.module.Constants;
import org.module.service.ModerationService;
import org.module.service.impl.ModerationServiceImpl;
import org.module.util.ArgsUtil;
import org.module.util.MessageUtil;
import org.module.util.PropertyUtil;
import net.dv8tion.jda.api.entities.Member;

public class WarnCommand extends Command {
	private final ModerationService moderationService = new ModerationServiceImpl();

	public WarnCommand() {
		this.name = PropertyUtil.getProperty("command.warn.name");
		this.help = PropertyUtil.getProperty("command.warn.help");
		this.arguments = PropertyUtil.getProperty("command.warn.arguments");
		this.category = Constants.MODERATION;
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

		String[] args = ArgsUtil.split(event.getArgs());
		Member member = ArgsUtil.getMember(event, args[0]);
		String reason = ArgsUtil.getGluedArg(args, 1);

		if (member == null || member.getUser().isBot()) {
			MessageUtil.sendHelp(event, this);
			return;
		}
		if (!event.getSelfMember().canInteract(member)) {
			MessageUtil.sendError(event, "command.warn.error.role.position");
			return;
		}
		if (member == event.getMember()) {
			MessageUtil.sendError(event, "command.warn.error.cannot.yourself");
			return;
		}

		long warnId = moderationService.warn(member, reason);

		MessageUtil.sendSuccess(event, "command.warn.success.warned",
			member.getUser().getAsTag(), warnId,
			event.getMember().getEffectiveName(),
			reason.isEmpty() ? "." : " with reason: " + reason);
	}
}
