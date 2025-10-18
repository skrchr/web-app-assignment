package web.example.com.tmdb.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Configuration to load local.properties file from project root
 * This file should contain sensitive configuration like API keys
 */
@Configuration
@PropertySource(value = {
        "file:../local.properties", // When running from backend/
        "file:local.properties" // When running from project root
}, ignoreResourceNotFound = true)
public class LocalPropertiesConfig {
    // This class loads local.properties from the project root
    // The file is ignored by git and should contain:
    // tmdb.api.key=your_actual_api_key_here
}
