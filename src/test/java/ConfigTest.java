import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.config.ConfigValue;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.inject.Inject;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
public class ConfigTest {

  @Inject
  @ConfigProperty(name = "aaa.bbb-ccc.ddd")
  ConfigValue custom;

  @Inject
  Config config;

  @Test
  void configCustom() {
    assertTrue(custom.getConfigSourceName().contains("/.env"));
    assertEquals("123", custom.getValue());

    Set<String> propertyNames = (Set<String>) config.getPropertyNames();
    // From .env
    assertTrue(propertyNames.contains("AAA_BBB_CCC_DDD"));

    // Added by .env
    assertFalse(propertyNames.contains("aaa.bbb.ccc.ddd")); // fails

    // From application.properties
    assertTrue(propertyNames.contains("aaa.bbb-ccc.ddd")); // fails

    // Exact Match
    assertEquals("123", config.getValue("AAA_BBB_CCC_DDD", String.class));

    // with hyphen
    assertEquals("123", config.getValue("aaa.bbb-ccc.ddd", String.class));

    assertThrows(NoSuchElementException.class, () -> config.getValue("aaa.bbb.ccc.ddd", String.class)); // fails
  }
}