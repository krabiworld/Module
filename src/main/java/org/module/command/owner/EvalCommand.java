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

package org.module.command.owner;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import org.module.Constants;
import org.module.Locale;
import org.module.service.MessageService;
import org.module.service.OwnerService;
import groovy.lang.GroovyShell;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@Component
public class EvalCommand extends Command {
	private final OwnerService ownerService;
	private final MessageService messageService;

	@Autowired
	public EvalCommand(OwnerService ownerService, MessageService messageService) {
		this.ownerService = ownerService;
		this.messageService = messageService;
		this.name = "eval";
		this.category = Constants.OWNER;
		this.hidden = true;
	}

	@Override
	protected void execute(CommandEvent event) {
		Locale locale = messageService.getLocale(event.getGuild());
		if (ownerService.isNotOwner(event.getMember())) return;

		if (event.getArgs().isEmpty()) {
			messageService.sendHelp(event, this, locale);
			return;
		}

		GroovyShell shell = new GroovyShell();
		shell.setProperty("e", event);

		event.getChannel().sendTyping().queue();
		event.async(() -> {
			try {
				Instant start = Instant.now();

				Object eval = shell.evaluate(event.getArgs());

				Instant finish = Instant.now();

				event.replySuccess(String.format(
					"Evaluated successfully! %s ms\n```\n%s\n```", Duration.between(start, finish).toMillis(), eval
				));
			} catch (Exception e) {
				event.reactError();
				event.replyError(e.getMessage());
			}
		});
	}
}
