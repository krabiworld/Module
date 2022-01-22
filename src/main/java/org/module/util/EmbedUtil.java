package org.module.util;

import net.dv8tion.jda.api.EmbedBuilder;
import org.module.constants.Constants;

public class EmbedUtil extends EmbedBuilder {
	public EmbedUtil(Type type, String description) {
		if (type == Type.ERROR) this.setColor(Constants.COLOR_RED);
		else if (type == Type.SUCCESS) this.setColor(Constants.COLOR_GREEN);
		this.setDescription(description);
	}

	public enum Type {
		ERROR,
		SUCCESS
	}
}
