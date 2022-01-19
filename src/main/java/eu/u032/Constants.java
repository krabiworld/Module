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

package eu.u032;

import com.jagrosh.jdautilities.command.Command.Category;

import java.awt.*;

public class Constants {
	// Bot
	public static final int MAX_MESSAGE_CACHE = 500;
	public static final String PREFIX = "m!";

	// Colors
	/** Main color. */
	public static final Color COLOR = new Color(46, 122, 213);
	/** Green color. Use if something has been created or in success action. */
	public static final Color COLOR_GREEN = new Color(84, 222, 60);
	/** Red color. Use if something has been deleted or in error action. */
	public static final Color COLOR_RED = new Color(235, 16, 16);
	/** Yellow color. Use if something has been updated or in warning action. */
	public static final Color COLOR_YELLOW = new Color(212, 190, 51);

	// Categories
	public static final Category INFORMATION = new Category("Information");
	public static final Category MODERATION = new Category("Moderation");
	public static final Category SETTINGS = new Category("Settings");
	public static final Category UTILITIES = new Category("Utilities");

	// Emojis
	public static final String ONLINE = "<:online:925113750598598736>";
	public static final String IDLE = "<:idle:925113750254682133>";
	public static final String DND = "<:dnd:925113750896398406>";
	public static final String OFFLINE = "<:offline:925113750581817354>";
	public static final String MEMBERS = "<:members:926844061707546654>";
	public static final String BOTS = "<:bots:926844061703364648>";
	public static final String TEXT = "<:text:926844062198276136>";
	public static final String VOICE = "<:voice:926844062504464444>";
	public static final String STAGE = "<:stage:926844062252818522>";
	public static final String STORE = "<:store:926844062160519178>";
}
