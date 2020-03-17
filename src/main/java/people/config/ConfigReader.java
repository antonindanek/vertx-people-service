package people.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

	private static final String configurationFilename = "configuration.properties";

	private static Properties configuration;

	static {
		configuration = new Properties();

		try (InputStream inputStream = ConfigReader.class.getClassLoader().getResourceAsStream(configurationFilename)) {
			configuration.load(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static int getPort() {
		return Integer.parseInt((String) configuration.getOrDefault("server.port", 8080));
	}

}
