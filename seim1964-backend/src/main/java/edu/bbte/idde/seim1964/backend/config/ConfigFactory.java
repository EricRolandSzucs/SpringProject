package edu.bbte.idde.seim1964.backend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

public class ConfigFactory {
    private static Config config;
    private static final Logger LOG = LoggerFactory.getLogger(ConfigFactory.class);

    public static synchronized Config getConfig() {

        if (config == null) {
            try {
                InputStream inputStream = Config.class.getResourceAsStream(getName());
                ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
                config = objectMapper.readValue(inputStream, Config.class);
            } catch (IOException e) {
                config = new Config();
            }
        }
        return config;
    }

    private static String getName() {
        final StringBuilder sb = new StringBuilder("/application");

        // PROFILE = prod (environment variable)
        String profile = System.getenv("PROFILE");
        if (profile != null && !profile.isEmpty()) {
            LOG.info("Detected profile {}", profile);
            sb.append('-').append(profile);
        }
        return sb.append(".yaml").toString();
    }
}
