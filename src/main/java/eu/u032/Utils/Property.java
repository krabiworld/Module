package eu.u032.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Property {

    private static final Properties properties = new Properties();

    private static String get(String key) {
        InputStream inputStream = Property.class.getClassLoader().getResourceAsStream("words.properties");
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties.getProperty(key);
    }

    public static String getError(String key) {
        return get("error." + key);
    }

}
