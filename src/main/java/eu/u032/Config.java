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

import io.github.cdimascio.dotenv.Dotenv;
import org.jetbrains.annotations.NotNull;

public enum Config {;
    private static final Dotenv DOTENV = Dotenv.load();

    private static String get(@NotNull final String key) {
        return DOTENV.get(key);
    }

	/** Get value as {@link String} */
    public static String getString(@NotNull final String key) {
        return get(key);
    }
}
