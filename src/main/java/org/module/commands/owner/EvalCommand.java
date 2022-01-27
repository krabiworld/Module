/*
 * This file is part of Module.

 * Module is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * Module is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with Module. If not, see <https://www.gnu.org/licenses/>.
 */

package org.module.commands.owner;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import org.module.constants.Constants;
import org.module.enums.MessageType;
import org.module.service.MessageService;
import org.module.service.OwnerService;
import org.module.util.PropertyUtil;
import groovy.lang.GroovyShell;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EvalCommand extends Command {
	private final MessageService messageService;
	private final OwnerService ownerService;

	@Autowired
	public EvalCommand(MessageService messageService, OwnerService ownerService) {
		this.messageService = messageService;
		this.ownerService = ownerService;
		this.name = PropertyUtil.getProperty("command.eval.name");
		this.help = PropertyUtil.getProperty("command.eval.help");
		this.arguments = PropertyUtil.getProperty("command.eval.arguments");
		this.category = Constants.OWNER;
		this.hidden = true;
	}

	@Override
	protected void execute(CommandEvent event) {
		if (!ownerService.isOwner(event.getMember())) return;

		if (event.getArgs().isEmpty()) {
			messageService.sendHelp(event, this);
			return;
		}

		GroovyShell shell = new GroovyShell();
		shell.setProperty("e", event);

		event.getChannel().sendTyping().queue();
		event.async(() -> {
			try {
				event.replySuccess("Evaluated Successfully:\n```" + shell.evaluate(event.getArgs()) + "```");
			} catch (Exception e) {
				messageService.sendMessage(MessageType.ERROR, event, e.getMessage());
			}
		});
	}
}
