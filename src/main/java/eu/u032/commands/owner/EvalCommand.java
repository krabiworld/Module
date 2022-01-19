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

package eu.u032.commands.owner;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import eu.u032.service.OwnerService;
import eu.u032.util.MessageUtil;
import groovy.lang.GroovyShell;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EvalCommand extends Command {
	@Autowired
	private OwnerService ownerService;

	public EvalCommand() {
		this.name = MessageUtil.getMessage("command.eval.name");
		this.help = MessageUtil.getMessage("command.eval.help");
		this.arguments = MessageUtil.getMessage("command.eval.arguments");
		this.hidden = true;
	}

	@Override
	protected void execute(CommandEvent event) {
		if (!event.isOwner() || ownerService.findById(event.getMember().getIdLong()) == null) {
			return;
		}

		String args = event.getArgs();

		if (args.isEmpty()) {
			MessageUtil.sendHelp(event, this);
			return;
		}

		GroovyShell shell = new GroovyShell();

		shell.setProperty("event", event);
		shell.setProperty("jda", event.getJDA());
		shell.setProperty("guild", event.getGuild());
		shell.setProperty("channel", event.getChannel());
		shell.setProperty("member", event.getMember());
		shell.setProperty("client", event.getClient());

		event.getChannel().sendTyping().queue();
		event.async(() -> {
			try {
				event.replySuccess("Evaluated Successfully:\n" + shell.evaluate(args));
			} catch (Exception e) {
				MessageUtil.sendErrorMessage(event, e.getMessage());
			}
		});
	}
}
