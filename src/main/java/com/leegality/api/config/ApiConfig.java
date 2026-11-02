package com.leegality.api.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ApiConfig {
    private static final String CONFIG_FILE = "config.properties";
    private static final Properties PROPERTIES = loadProperties();

    private ApiConfig() {
    }

    public static String baseUrl() {
        String configuredUrl = System.getProperty("base.url");
        if (configuredUrl == null || configuredUrl.isBlank()) {
            configuredUrl = System.getenv("API_BASE_URL");
        }
        if (configuredUrl == null || configuredUrl.isBlank()) {
            configuredUrl = PROPERTIES.getProperty("base.url");
        }
        if (configuredUrl == null || configuredUrl.isBlank()) {
            throw new IllegalStateException(
                    "API base URL is missing. Set -Dbase.url or the API_BASE_URL environment variable.");
        }
        return configuredUrl.trim();
    }

    private static Properties loadProperties() {
        Properties properties = new Properties();
        try (InputStream input = ApiConfig.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                throw new IllegalStateException("Required configuration file was not found: " + CONFIG_FILE);
            }
            properties.load(input);
            return properties;
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to read configuration file: " + CONFIG_FILE, exception);
        }
    }
}
