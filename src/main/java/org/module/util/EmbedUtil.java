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

package org.module.util;

import com.jagrosh.jdautilities.command.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import org.module.constants.Constants;
import org.module.enums.MessageType;

public class EmbedUtil extends EmbedBuilder {
	/** Embed with description */
	public EmbedUtil(MessageType type, String description) {
		setMessageColor(type);
		this.setDescription(description);
	}

	/** Embed for command help. */
	public EmbedUtil(Command command, String prefix) {
		String arguments = command.getArguments().isEmpty() ? "" : " " + command.getArguments();
		setMessageColor(MessageType.INFO);
		this.setTitle("Information of command " + command.getName());
		this.setDescription("`" + prefix + command.getName() + arguments + "`\n" + command.getHelp());
	}

	private void setMessageColor(MessageType type) {
		switch (type) {
			case INFO -> this.setColor(Constants.COLOR);
			case WARN -> this.setColor(Constants.COLOR_YELLOW);
			case ERROR -> this.setColor(Constants.COLOR_RED);
			case SUCCESS -> this.setColor(Constants.COLOR_GREEN);
		}
	}
}
