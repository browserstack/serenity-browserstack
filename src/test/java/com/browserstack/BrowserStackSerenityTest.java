package com.browserstack;

import java.util.HashMap;
import java.util.Map;
import java.io.FileReader;
import com.browserstack.local.Local;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.*;
import org.openqa.selenium.MutableCapabilities;
import net.thucydides.core.util.EnvironmentVariables;
import net.thucydides.core.util.SystemEnvironmentVariables;

public class BrowserStackSerenityTest {
    private static Local bsLocal;
    private static Object lock = new Object();
    private static Integer parallels = 0;

    @BeforeClass
    public static void setUp()
            throws Exception {
        String test_config = System.getProperty("test_config");
        
        JSONParser parser = new JSONParser();
        JSONObject config;
        try {
            config = (JSONObject) parser.parse(new FileReader("src/test/resources/conf/" + test_config));
        } catch (Exception e) {
            System.out.println("Error: could not open " + "src/test/resources/conf/" + test_config);
            e.printStackTrace();
            return;
        }
        EnvironmentVariables environmentVariables = SystemEnvironmentVariables.createEnvironmentVariables();

        String accessKey = System.getenv("BROWSERSTACK_ACCESS_KEY");
        if (accessKey == null) {
            accessKey = (String) config.get("key");
        }

        String environment = System.getProperty("environment");
        JSONObject capabilities = (JSONObject) parser.parse(config.get("capabilities").toString());
        JSONObject bstack_options = null;
        if (capabilities.get("bstack:options") != null)
            bstack_options = (JSONObject) parser.parse(capabilities.get("bstack:options").toString());

        boolean is_local = bstack_options != null && bstack_options.get("local") != null && bstack_options.get("local").equals(true);

        if (bsLocal != null) {
            return;
        }
        synchronized (lock) {
          parallels++;
          if ((bsLocal == null || !bsLocal.isRunning()) && is_local) {
              bsLocal = new Local();
              Map<String, String> bsLocalArgs = new HashMap<String, String>();
              bsLocalArgs.put("key", accessKey);
              try {
                  bsLocal.start(bsLocalArgs);
              } catch (Exception e){
                  e.printStackTrace();
              }
          }
      }
  }

    @AfterClass
    public static void tearDown() throws Exception {
      synchronized (lock){
        parallels--;
        if (bsLocal != null && parallels == 0) bsLocal.stop();
    }
    }
}
