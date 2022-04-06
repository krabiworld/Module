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

package org.module;

import com.jagrosh.jdautilities.command.Command.Category;

import java.awt.*;

public class Constants {
	public static final int MAX_MESSAGE_CACHE = 1000;
	public static final String DEFAULT_PREFIX = "module!";

	public static final Category INFORMATION = new Category("Information");
	public static final Category MODERATION = new Category("Moderation");
	public static final Category OWNER = new Category("OwnerModel");
	public static final Category SETTINGS = new Category("Settings");
	public static final Category UTILITIES = new Category("Utilities");

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

	public enum Language { RU, EN }
}
