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

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.module.Constants;
import org.module.structure.Command;

import java.awt.*;
import java.text.MessageFormat;

public class EmbedUtil extends EmbedBuilder {
	/** Embed with description */
	public EmbedUtil(Color color, String description) {
		this.setColor(color);
		this.setDescription(description);
	}

	/** Embed for command help. */
	public EmbedUtil(Command command) {
		Command[] children = command.getChildren();
		String name = command.getName();
		StringBuilder argsBuilder = new StringBuilder();
		String args;
		String description = command.getDescription();

		if (children.length != 0) {
			argsBuilder.append("[");
			for (int i = 0; i < children.length; i++) {
				Command child = children[i];
				argsBuilder.append(child.getName()).append(children.length - 1 == i ? "" : " | ");
			}
			argsBuilder.append("]");
		} else {
			for (OptionData option : command.getOptions()) {
				argsBuilder.append(MessageFormat.format(option.isRequired() ? "<{0}> " : "[{0}] ", option.getName()));
			}
		}

		args = argsBuilder.isEmpty() ? "" : " " + argsBuilder.toString().trim();

		this.setColor(Constants.DEFAULT);
		this.setTitle(MessageFormat.format("Information of command {0}", name));
		this.setDescription(MessageFormat.format(
			"`/{0}{1}`\n{2}", name, args, description
		));
	}
}
