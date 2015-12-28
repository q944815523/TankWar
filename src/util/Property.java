package util;

import java.io.IOException;
import java.util.Properties;

public class Property {
	private static Properties prop = new Properties();
	
	static {
		try {
			prop.load(Property.class.getClassLoader().getResourceAsStream("config/config.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static int getConfig(String key) {
		return Integer.parseInt(prop.getProperty(key));
	}
}
