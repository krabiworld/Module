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

package eu.u032.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import eu.u032.Utils;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class EvalCommand extends Command {
    public EvalCommand() {
        this.name = "eval";
        this.ownerCommand = true;
		this.aliases = new String[]{"e"};
        this.hidden = true;
    }

    @Override
    protected void execute(final CommandEvent event) {
		if (event.getArgs().isEmpty()) {
			Utils.sendError(event, "Required arguments are missing!");
			return;
		}

		final ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
		engine.put("event", event);

		event.async(() -> {
            try {
                event.replySuccess("Evaluated Successfully:\n```" + engine.eval(event.getArgs()) + "```");
            } catch (final Exception e) {
                event.replyError(e.getMessage());
            }
        });
    }
}
