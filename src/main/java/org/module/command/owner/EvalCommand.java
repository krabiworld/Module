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

import groovy.lang.GroovyShell;
import org.module.structure.AbstractCommand;
import org.module.structure.Command;
import org.module.structure.CommandContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@Component
@Command(
	name = "command.eval.name",
	args = "command.eval.args",
	help = "command.eval.help",
	category = "category.owner",
	hidden = true
)
public class EvalCommand extends AbstractCommand {
	private final ApplicationContext appCtx;

	@Autowired
	public EvalCommand(ApplicationContext appCtx) {
		this.appCtx = appCtx;
	}

	@Override
	protected void execute(CommandContext ctx) {
		if (!ctx.getUser().getId().equals(ctx.getClient().getOwnerId())) {
			return;
		}

		if (ctx.getArgs().isEmpty()) {
			ctx.sendHelp();
			return;
		}

		GroovyShell shell = new GroovyShell();
		shell.setProperty("e", ctx);
		shell.setProperty("ctx", appCtx);

		ctx.getChannel().sendTyping().queue();
		try {
			Instant start = Instant.now();

			Object eval = shell.evaluate(ctx.getArgs());

			Instant finish = Instant.now();

			ctx.sendSuccess(String.format(
				"Evaluated successfully! %s ms\n```\n%s\n```", Duration.between(start, finish).toMillis(), eval
			));
		} catch (Exception e) {
			ctx.send(e.getMessage());
		}
	}
}

