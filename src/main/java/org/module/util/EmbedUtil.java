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
