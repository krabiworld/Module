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

package org.module.util;

import com.jagrosh.jdautilities.command.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import org.module.Constants;

import java.awt.*;

public class EmbedUtil extends EmbedBuilder {
	/** Embed with description */
	public EmbedUtil(Color color, String description) {
		this.setColor(color);
		this.setDescription(description);
	}

	/** Embed for command help. */
	public EmbedUtil(Command command, String prefix) {
		String arguments = command.getArguments().isEmpty() ? "" : " " + command.getArguments();
		this.setColor(Constants.DEFAULT);
		this.setTitle("Information of command " + command.getName());
		this.setDescription("`" + prefix + command.getName() + arguments + "`\n" + command.getHelp());
	}
}
