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

package org.module.commands.owner;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import org.module.Constants;
import org.module.service.OwnerService;
import org.module.service.impl.OwnerServiceImpl;
import org.module.util.MessageUtil;
import org.module.util.PropertyUtil;
import groovy.lang.GroovyShell;

public class EvalCommand extends Command {
	private final OwnerService ownerService = new OwnerServiceImpl();

	public EvalCommand() {
		this.name = PropertyUtil.getProperty("command.eval.name");
		this.help = PropertyUtil.getProperty("command.eval.help");
		this.arguments = PropertyUtil.getProperty("command.eval.arguments");
		this.category = Constants.OWNER;
		this.hidden = true;
	}

	@Override
	protected void execute(CommandEvent event) {
		if (ownerService.isNotOwner(event.getMember())) return;

		if (event.getArgs().isEmpty()) {
			MessageUtil.sendHelp(event, this);
			return;
		}

		GroovyShell shell = new GroovyShell();
		shell.setProperty("e", event);

		event.getChannel().sendTyping().queue();
		event.async(() -> {
			try {
				event.replySuccess("Evaluated Successfully:\n```" + shell.evaluate(event.getArgs()) + "```");
			} catch (Exception e) {
				event.reactError();
				event.replyError(e.getMessage());
			}
		});
	}
}
