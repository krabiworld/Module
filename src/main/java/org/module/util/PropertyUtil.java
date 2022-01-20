/*
 * Module Discord Bot.
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

package org.module.util;

import java.io.InputStream;
import java.util.Properties;

public class PropertyUtil {
	private static final Properties PROPERTIES = new Properties();

	private static final InputStream MESSAGES = PropertyUtil.class.getResourceAsStream("/messages.properties");

	static {
		try {
			PROPERTIES.load(MESSAGES);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** Get message from properties file. */
	public static String getProperty(String key, Object... args) {
		if (args == null) {
			return PROPERTIES.getProperty(key);
		} else {
			return String.format(PROPERTIES.getProperty(key), args);
		}
	}
}
