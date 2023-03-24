package com.browserstack;

import com.browserstack.local.Local;

import java.util.Map;
import java.util.HashMap;

import org.junit.BeforeClass;
import org.junit.AfterClass;

import net.thucydides.core.util.EnvironmentVariables;
import net.thucydides.core.util.SystemEnvironmentVariables;

public class BrowserStackSerenityTest {
    static Local bsLocal;
    private static Object lock = new Object();
    private static Integer parallels = 0;

    @BeforeClass
    public static void setUp() throws Exception {
        EnvironmentVariables environmentVariables = SystemEnvironmentVariables.createEnvironmentVariables();

        String accessKey = System.getenv("BROWSERSTACK_ACCESS_KEY");
        if (accessKey == null) {
            accessKey = (String) environmentVariables.getProperty("browserstack.key");
        }

        String environment = System.getProperty("environment");
        String key = "bstack_browserstack.local";
        boolean is_local = environmentVariables.getProperty(key) != null
                && environmentVariables.getProperty(key).equals("true");

        if (environment != null && !is_local) {
            key = "environment." + environment + ".browserstack.local";
            is_local = environmentVariables.getProperty(key) != null
                    && environmentVariables.getProperty(key).equals("true");
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
