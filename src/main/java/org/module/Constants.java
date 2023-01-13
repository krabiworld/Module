package org.module;

import java.awt.Color;

public class Constants {
	public static final int MAX_MESSAGE_CACHE = 8000;
	public static final int MAX_EXECUTED_COMMANDS_CACHE = 100;

	/** Main color. */
	public static final Color DEFAULT = new Color(46, 122, 213);
	/** Green color. Use if something has been created or in success action. */
	public static final Color SUCCESS = new Color(84, 222, 60);
	/** Red color. Use if something has been deleted or in error action. */
	public static final Color ERROR = new Color(235, 16, 16);
	/** Yellow color. Use if something has been updated or in warning action. */
	public static final Color WARN = new Color(212, 190, 51);

	public static final String ONLINE = "<:online:948619010788962324>";
	public static final String IDLE = "<:idle:948619010575065131>";
	public static final String DND = "<:dnd:948619010352750603>";
	public static final String OFFLINE = "<:offline:948619010440843387>";
}
