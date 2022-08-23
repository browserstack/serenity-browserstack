package com.browserstack;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import net.thucydides.core.webdriver.DriverSource;
import org.yaml.snakeyaml.Yaml;

public class BrowserStackSerenityDriver implements DriverSource {
    public static final String USER_DIR = "user.dir";
    private static Map<String, Object> browserstackYamlMap;
    private String userName;
    private String accessKey;
    public static final String SERVER_URL = "hub.browserstack.com";

    @Override
    public WebDriver newDriver() {
        MutableCapabilities capabilities = new MutableCapabilities();
        HashMap<String, Object> bStackOptions = new HashMap<>();
        String platformSystemProperty = System.getProperty("platform") != null ? System.getProperty("platform") : System.getenv("platform");
        String platformCount = platformSystemProperty.replace("platform_", "").trim();
        Integer platform = Integer.valueOf(platformCount) - 1;
        Thread.currentThread().setName("webdriver@" + platform);
        if (browserstackYamlMap == null) {
            File file = new File(getUserDir() + "/browserstack.yml");
            browserstackYamlMap = convertYamlFileToMap(file, new HashMap<>());
        }
        browserstackYamlMap.forEach((key, value) -> {
            if (key.equalsIgnoreCase("userName")) {
                userName = System.getenv("BROWSERSTACK_USERNAME") != null ? System.getenv("BROWSERSTACK_USERNAME") : (String) value;
            } else if (key.equalsIgnoreCase("accessKey")) {
                accessKey = System.getenv("BROWSERSTACK_ACCESS_KEY") != null ? System.getenv("BROWSERSTACK_ACCESS_KEY") : (String) value;
            } else if (key.equalsIgnoreCase("platforms")) {
                ArrayList<LinkedHashMap<String, Object>> platformsArrayList = (ArrayList<LinkedHashMap<String, Object>>) value;
                platformsArrayList.get(platform).forEach((k, v) -> {
                    if( k.equalsIgnoreCase("browserName") || k.equalsIgnoreCase("browserVersion")){
                        capabilities.setCapability(k,v.toString());
                    } else {
                        bStackOptions.put(k,v.toString());
                    }
                });
            } else if (key.equalsIgnoreCase("browserstackLocal") ||
                    key.equalsIgnoreCase("local")) {
                bStackOptions.put("local", value);
            } else if (key.equalsIgnoreCase("browserStackLocalOptions") ||
                    key.equalsIgnoreCase("localOptions")) {
                if (value instanceof LinkedHashMap) {
                    ArrayList<LinkedHashMap<String, Object>> localOptionsArrayList = (ArrayList<LinkedHashMap<String, Object>>) value;
                    localOptionsArrayList.forEach(localOptionsMap -> {
                        if (((Boolean) browserstackYamlMap.get("browserstackLocal") || (Boolean) browserstackYamlMap.get("local"))
                                && localOptionsMap.containsKey("localIdentifier")) {
                            bStackOptions.put("localIdentifier", localOptionsMap.get("localIdentifier").toString());
                        }
                    });
                } else if (value instanceof HashMap) {
                    HashMap<String, ?> localOptionsHashMap = new ObjectMapper().convertValue(value, HashMap.class);
                    if (((Boolean) browserstackYamlMap.get("browserstackLocal") || (Boolean) browserstackYamlMap.get("local"))
                            && localOptionsHashMap.containsKey("localIdentifier")) {
                        bStackOptions.put("localIdentifier", localOptionsHashMap.get("localIdentifier").toString());
                    }
                }
            } else {
                bStackOptions.put(key, value);
            }
        });
        capabilities.setCapability("bstack:options", bStackOptions);
        try {
            return new RemoteWebDriver(new URL("https://" + userName + ":" + accessKey + "@" + SERVER_URL + "/wd/hub"), capabilities);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String getUserDir() {
        return System.getProperty(USER_DIR);
    }

    private static Map<String, Object> convertYamlFileToMap(File yamlFile, Map<String, Object> map) {
        try {
            InputStream inputStream = Files.newInputStream(yamlFile.toPath());
            Yaml yaml = new Yaml();
            Map<String, Object> config = yaml.load(inputStream);
            map.putAll(config);
        } catch (Exception e) {
            throw new RuntimeException(String.format("Malformed browserstack.yml file - %s.", e));
        }
        return map;
    }

    @Override
    public boolean takesScreenshots() {
        return true;
    }
}
