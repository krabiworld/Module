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

import java.util.Properties;

public record Locale(Properties properties) {
	public String get(String key, Object... args) {
		try {
			if (args == null) {
				return properties.getProperty(key);
			} else {
				return String.format(properties.getProperty(key), args);
			}
		} catch (NullPointerException e) {
			return "";
		}
	}
}
