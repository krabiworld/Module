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

//    public static int getInt(@NotNull final String key) {
//        return Integer.parseInt(getTemplate(key));
//    }

//    private static String getTemplate(@NotNull String key, String defaultValue) {
//        return dotenv.getTemplate(key, defaultValue);
//    }

//    public static String getString(@NotNull String key, String defaultValue) {
//        return dotenv.getTemplate(key, defaultValue);
//    }

//    public static int getInt(@NotNull String key, int defaultValue) {
//        return Integer.parseInt(getTemplate(key, Integer.toString(defaultValue)));
//    }

//    public static boolean getBoolean(@NotNull String key) {
//        return Boolean.parseBoolean(getTemplate(key));
//    }

//    public static boolean getBoolean(@NotNull String key, boolean defaultValue) {
//        return Boolean.parseBoolean(getTemplate(key, Boolean.toString(defaultValue)));
//    }

//    public static double getDouble(@NotNull String key) {
//        return Double.parseDouble(getTemplate(key));
//    }

//    public static double getDouble(@NotNull String key, double defaultValue) {
//        return Double.parseDouble(getTemplate(key, Double.toString(defaultValue)));
//    }

//    public static long getLong(@NotNull String key) {
//        return Long.parseLong(getTemplate(key));
//    }

//    public static long getLong(@NotNull String key, long defaultValue) {
//        return Long.parseLong(getTemplate(key, Long.toString(defaultValue)));
//    }

//    public static float getFloat(@NotNull String key) {
//        return Float.parseFloat(getTemplate(key));
//    }

//    public static float getFloat(@NotNull String key, float defaultValue) {
//        return Float.parseFloat(getTemplate(key, Float.toString(defaultValue)));
//    }

//    public static short getShort(@NotNull String key) {
//        return Short.parseShort(getTemplate(key));
//    }

//    public static short getShort(@NotNull String key, short defaultValue) {
//        return Short.parseShort(getTemplate(key, Short.toString(defaultValue)));
//    }

//    public static byte getByte(@NotNull String key) {
//        return Byte.parseByte(getTemplate(key));
//    }

//    public static byte getByte(@NotNull String key, byte defaultValue) {
//        return Byte.parseByte(getTemplate(key, Byte.toString(defaultValue)));
//    }

//    public static char getChar(@NotNull String key) {
//        return getTemplate(key).charAt(0);
//    }

//    public static char getChar(@NotNull String key, char defaultValue) {
//        return getTemplate(key, Character.toString(defaultValue)).charAt(0);
//    }

//    public static String @NotNull [] getStringArray(@NotNull String key) {
//        return getTemplate(key).split(",");
//    }

//    public static String @NotNull [] getStringArray(@NotNull String key, String[] defaultValue) {
//        return getTemplate(key, String.join(",", defaultValue)).split(",");
//    }

//    public static int @NotNull [] getIntArray(@NotNull String key) {
//        String[] stringArray = getStringArray(key);
//        int[] intArray = new int[stringArray.length];
//        for (int i = 0; i < stringArray.length; i++) {
//            intArray[i] = Integer.parseInt(stringArray[i]);
//        }
//        return intArray;
//    }

//    public static boolean @NotNull [] getBooleanArray(@NotNull String key) {
//        String[] stringArray = getStringArray(key);
//        boolean[] booleanArray = new boolean[stringArray.length];
//        for (int i = 0; i < stringArray.length; i++) {
//            booleanArray[i] = Boolean.parseBoolean(stringArray[i]);
//        }
//        return booleanArray;
//    }

//    public static double @NotNull [] getDoubleArray(@NotNull String key) {
//        String[] stringArray = getStringArray(key);
//        double[] doubleArray = new double[stringArray.length];
//        for (int i = 0; i < stringArray.length; i++) {
//            doubleArray[i] = Double.parseDouble(stringArray[i]);
//        }
//        return doubleArray;
//    }

//    public static long @NotNull [] getLongArray(@NotNull String key) {
//        String[] stringArray = getStringArray(key);
//        long[] longArray = new long[stringArray.length];
//        for (int i = 0; i < stringArray.length; i++) {
//            longArray[i] = Long.parseLong(stringArray[i]);
//        }
//        return longArray;
//    }

//    public static float @NotNull [] getFloatArray(@NotNull String key) {
//        String[] stringArray = getStringArray(key);
//        float[] floatArray = new float[stringArray.length];
//        for (int i = 0; i < stringArray.length; i++) {
//            floatArray[i] = Float.parseFloat(stringArray[i]);
//        }
//        return floatArray;
//    }

//    public static short @NotNull [] getShortArray(@NotNull String key) {
//        String[] stringArray = getStringArray(key);
//        short[] shortArray = new short[stringArray.length];
//        for (int i = 0; i < stringArray.length; i++) {
//            shortArray[i] = Short.parseShort(stringArray[i]);
//        }
//        return shortArray;
//    }

//    public static byte @NotNull [] getByteArray(@NotNull String key) {
//        String[] stringArray = getStringArray(key);
//        byte[] byteArray = new byte[stringArray.length];
//        for (int i = 0; i < stringArray.length; i++) {
//            byteArray[i] = Byte.parseByte(stringArray[i]);
//        }
//        return byteArray;
//    }

//    public static char @NotNull [] getCharArray(@NotNull String key) {
//        String[] stringArray = getStringArray(key);
//        char[] charArray = new char[stringArray.length];
//        for (int i = 0; i < stringArray.length; i++) {
//            charArray[i] = stringArray[i].charAt(0);
//        }
//        return charArray;
//    }
}
