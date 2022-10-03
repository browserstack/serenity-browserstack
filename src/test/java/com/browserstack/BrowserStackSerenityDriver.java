package com.browserstack;

import java.io.FileReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.browserstack.local.Local;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import net.thucydides.core.webdriver.DriverSource;

@SuppressWarnings("unchecked")
public class BrowserStackSerenityDriver implements DriverSource{

    public WebDriver newDriver() {
        String test_config = System.getProperty("test_config");
        int test_config_environment_index = Integer.parseInt(System.getProperty("test_config_environment_index").split("index_")[1]) - 1;

        JSONParser parser = new JSONParser();
        JSONObject config;
        try {
            config = (JSONObject) parser.parse(new FileReader("src/test/resources/conf/" + test_config));
        } catch (Exception e) {
            System.out.println("Error: could not open " + "src/test/resources/conf/" + test_config);
            e.printStackTrace();
            return null;
        }
        JSONObject allEnvironments = (JSONObject) config.get("environments");
        String currentEnvironmentName = (String) allEnvironments.keySet().toArray()[test_config_environment_index];

        DesiredCapabilities capabilities = new DesiredCapabilities();
        Map<String, Object> envCapabilities = (Map<String, Object>) allEnvironments.get(currentEnvironmentName);
        Iterator<Map.Entry<String, Object>> it = envCapabilities.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Object> pair = (Map.Entry<String, Object>) it.next();
            capabilities.setCapability(pair.getKey().toString(), pair.getValue());
        }

        Map<String, Object> commonCapabilities = (Map<String, Object>) config.get("capabilities");
        it = commonCapabilities.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Object> pair = (Map.Entry<String, Object>) it.next();
            Object envData = capabilities.getCapability(pair.getKey().toString());
            Object resultData = pair.getValue();
            if (envData != null && envData.getClass() == JSONObject.class) {
                resultData = ((JSONObject) resultData).clone(); // do not modify actual common caps
                ((JSONObject) resultData).putAll((JSONObject) envData);
            }
            capabilities.setCapability(pair.getKey().toString(), resultData);
        }

        String username = System.getenv("BROWSERSTACK_USERNAME");
        if (username == null) {
            username = (String) config.get("user");
        }

        String accessKey = System.getenv("BROWSERSTACK_ACCESS_KEY");
        if (accessKey == null) {
            accessKey = (String) config.get("key");
        }

        try {
            BrowserStackSerenityTest.checkAndStartBrowserStackLocal(capabilities, accessKey);
        } catch(Exception e) {
            System.err.println("Error: could not start browserstack local");
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        String urlString = "https://" + username + ":" + accessKey + "@" + config.get("server") + "/wd/hub";
        HashMap<String, Object> bStackOptionsMap = (HashMap<String, Object>) capabilities.getCapability("bstack:options");
        bStackOptionsMap.put("source", "serenity:sample-selenium-4:v1.0");
        try {
            return new RemoteWebDriver(
                    new URL(urlString),
                    capabilities);
        } catch (MalformedURLException e) {
            System.err.println(
                    "Malformed url " + urlString);
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public boolean takesScreenshots() {
        return true;
    }
}
