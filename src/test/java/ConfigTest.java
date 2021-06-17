import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.config.ConfigValue;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
public class ConfigTest {
    @Inject
    @ConfigProperty(name = "mp.messaging.incoming.keycloak-events.connector")
    ConfigValue value;
    @Inject
    Config config;

    @Test
    void config() {
        assertTrue(value.getConfigSourceName().contains("/.env"));
        assertEquals("smallrye-in-memory", value.getValue());

        Set<String> propertyNames = (Set<String>) config.getPropertyNames();
        // From .env
        assertTrue(propertyNames.contains("MP_MESSAGING_INCOMING_KEYCLOAK_EVENTS_CONNECTOR"));
        // Added by .env
        assertTrue(propertyNames.contains("mp.messaging.incoming.keycloak.events.connector"));
        // From application.properties
        assertTrue(propertyNames.contains("mp.messaging.incoming.keycloak-events.connector"));

        // Exact Match
        assertEquals("smallrye-in-memory", config.getValue("MP_MESSAGING_INCOMING_KEYCLOAK_EVENTS_CONNECTOR", String.class));
        // Both these have the same value based on the Env conversion rules
        assertEquals("smallrye-in-memory", config.getValue("mp.messaging.incoming.keycloak-events.connector", String.class));
        assertEquals("smallrye-in-memory", config.getValue("mp.messaging.incoming.keycloak.events.connector", String.class));
    }
}
