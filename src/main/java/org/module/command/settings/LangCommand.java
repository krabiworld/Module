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

package org.module.command.settings;

import net.dv8tion.jda.api.Permission;
import org.module.Constants;
import org.module.structure.AbstractCommand;
import org.module.structure.Command;
import org.module.structure.CommandContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Command(
	name = "command.lang.name",
	args = "command.lang.args",
	help = "command.lang.help",
	category = "category.settings",
	userPermissions = {Permission.MANAGE_SERVER}
)
public class LangCommand extends AbstractCommand {
	@Autowired
	public LangCommand() {
		this.children = new AbstractCommand[]{new EnSubCommand(), new RuSubCommand()};
	}

	@Override
	protected void execute(CommandContext ctx) {
		ctx.sendHelp();
	}

	@Command(name = "command.lang.en.name")
	private static class EnSubCommand extends AbstractCommand {
		@Override
		protected void execute(CommandContext ctx) {
			ctx.getManager().setLang(ctx.getGuild(), Constants.Language.EN);
			ctx.sendSuccess("command.lang.success.changed", "en");
		}
	}

	@Command(name = "command.lang.ru.name")
	private static class RuSubCommand extends AbstractCommand {
		@Override
		protected void execute(CommandContext ctx) {
			ctx.getManager().setLang(ctx.getGuild(), Constants.Language.RU);
			ctx.sendSuccess("command.lang.success.changed", "ru");
		}
	}
}
