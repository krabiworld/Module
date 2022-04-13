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

import org.module.Locale;
import org.module.structure.GuildSettingsProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class LocaleUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(LocaleUtil.class);
	private static Properties EN_PROPERTIES = null;
	private static Properties RU_PROPERTIES = null;

	private static void init() throws IOException {
		if (EN_PROPERTIES == null) {
			InputStream en = LocaleUtil.class.getResourceAsStream("/locales/en.properties");
			if (en == null) throw new NullPointerException("Locale EN is null.");
			EN_PROPERTIES = new Properties();
			EN_PROPERTIES.load(new InputStreamReader(en));
		}
		if (RU_PROPERTIES == null) {
			InputStream ru = LocaleUtil.class.getResourceAsStream("/locales/ru.properties");
			if (ru == null) throw new NullPointerException("Locale RU is null.");
			RU_PROPERTIES = new Properties();
			RU_PROPERTIES.load(new InputStreamReader(ru));
		}
	}

	public static Locale getLocale(String lang) {
		try {
			init();
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}

		return new Locale(lang.equals("ru") ? RU_PROPERTIES : EN_PROPERTIES);
	}

	public static Locale getLocale(GuildSettingsProvider settings) {
		return getLocale(settings.getLang());
	}
}
