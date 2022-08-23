package com.browserstack;
import java.net.URL;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import net.thucydides.core.webdriver.DriverSource;

public class BrowserStackSerenityDriver implements DriverSource {

    @Override
    public WebDriver newDriver() {
        MutableCapabilities capabilities = new MutableCapabilities();
        String platformSystemProperty = System.getProperty("platform") != null ? System.getProperty("platform") : System.getenv("platform");
        String platformCount = platformSystemProperty.replace("platform_", "").trim();
        Integer platform = Integer.valueOf(platformCount) - 1;
        Thread.currentThread().setName("webdriver@" + platform);
        try {
            return new RemoteWebDriver(new URL("https://hub.browserstack.com/wd/hub"), capabilities);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean takesScreenshots() {
        return true;
    }
}
