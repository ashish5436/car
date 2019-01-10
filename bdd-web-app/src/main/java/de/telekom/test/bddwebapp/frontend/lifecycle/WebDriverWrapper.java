package de.telekom.test.bddwebapp.frontend.lifecycle;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.opera.OperaOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Manage the current WebDriver instance. Uses proven standard configurations for specific browsers. The lifecycle is determined by SeleniumTestRule.
 *
 * @author Daniel Keiss {@literal <daniel.keiss@telekom.de>}
 * @author Igor Cernopolc - Initially added support for RemoteWebDriver
 * <p>
 * Copyright (c) 2018 Daniel Keiss, Deutsche Telekom AG
 * This file is distributed under the conditions of the Apache License, Version 2.0.
 * For details see the file license on the toplevel.
 */
@Component
public class WebDriverWrapper {

    private final Logger log = LoggerFactory.getLogger(WebDriverWrapper.class);

    @Value("${default.browser:chrome}")
    private String defaultBrowser;

    @Value("${webdriver.proxy.host:#{null}}")
    private String proxyHost;

    @Value("${webdriver.proxy.port:#{null}}")
    private String proxyPort;

    @Value("${webdriver.maximize:false}")
    private boolean maximizeBrowser;

    @Getter
    @Setter
    private WebDriver driver;

    public void loadWebdriver() {
        String browser = getBrowser();

        DesiredCapabilities capabilities = capabilities(browser);
        String gridURL = getGridURL();
        if (isBlank(gridURL)) {
            loadLocalWebdriver(browser, capabilities);
        } else {
            loadRemoteWebdriver(gridURL, capabilities);
        }

        afterLoad();
    }

    protected DesiredCapabilities capabilities(String browser) {
        switch (browser) {
            case "firefox": {
                return firefoxCapabilities();
            }
            case "chrome": {
                return chromeCapabilities();
            }
            case "edge": {
                return edgeCapabilities();
            }
            case "ie": {
                return ieCapabilities();
            }
            case "opera": {
                return operaCapabilities();
            }
            case "safari": {
                return safariCapabilities();
            }
            default: {
                throw new IllegalArgumentException("No browser defined! Given browser is: " + browser);
            }
        }
    }

    protected DesiredCapabilities firefoxCapabilities() {
        DesiredCapabilities caps = DesiredCapabilities.firefox();
        caps.setCapability("overlappingCheckDisabled", true);
        return caps;
    }

    protected DesiredCapabilities chromeCapabilities() {
        DesiredCapabilities caps = DesiredCapabilities.chrome();
        caps.setCapability("disable-restore-session-state", true);
        caps.setCapability("disable-application-cache", true);
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-extensions");
        caps.setCapability(ChromeOptions.CAPABILITY, options);
        return caps;
    }

    protected DesiredCapabilities edgeCapabilities() {
        return DesiredCapabilities.edge();
    }

    protected DesiredCapabilities ieCapabilities() {
        DesiredCapabilities caps = DesiredCapabilities.internetExplorer();
        caps.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
        caps.setCapability(CapabilityType.HAS_NATIVE_EVENTS, true);
        caps.setCapability(CapabilityType.SUPPORTS_APPLICATION_CACHE, true);
        caps.setCapability(CapabilityType.SUPPORTS_FINDING_BY_CSS, true);
        caps.setCapability(CapabilityType.SUPPORTS_JAVASCRIPT, true);
        caps.setCapability(CapabilityType.SUPPORTS_LOCATION_CONTEXT, true);
        caps.setCapability(CapabilityType.SUPPORTS_SQL_DATABASE, true);
        caps.setCapability(CapabilityType.SUPPORTS_WEB_STORAGE, true);
        caps.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
        return caps;
    }

    protected DesiredCapabilities operaCapabilities() {
        return DesiredCapabilities.operaBlink();
    }

    private DesiredCapabilities safariCapabilities() {
        return DesiredCapabilities.safari();
    }

    protected void loadLocalWebdriver(String browser, DesiredCapabilities capabilities) {
        log.info("Browser is set to: " + browser);
        switch (browser) {
            case "firefox": {
                loadFirefox(capabilities);
                return;
            }
            case "chrome": {
                loadChrome(capabilities);
                return;
            }
            case "edge": {
                loadEdge(capabilities);
                return;
            }
            case "ie": {
                loadInternetExplorer(capabilities);
                return;
            }
            case "opera": {
                loadOpera(capabilities);
                break;
            }
            case "safari": {
                loadSafari(capabilities);
                return;
            }
            default:
                throw new IllegalArgumentException("No browser defined! Given browser is: " + browser);
        }
    }

    protected void loadFirefox(DesiredCapabilities capabilities) {
        FirefoxOptions firefoxOptions = new FirefoxOptions(capabilities);
        driver = new FirefoxDriver(firefoxOptions);
    }

    protected void loadChrome(DesiredCapabilities capabilities) {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.merge(capabilities);
        driver = new ChromeDriver(chromeOptions);
    }

    protected void loadEdge(DesiredCapabilities capabilities) {
        EdgeOptions edgeOptions = new EdgeOptions();
        edgeOptions.merge(capabilities);
        driver = new EdgeDriver(edgeOptions);
    }

    protected void loadInternetExplorer(DesiredCapabilities capabilities) {
        InternetExplorerOptions internetExplorerOptions = new InternetExplorerOptions(capabilities);
        driver = new InternetExplorerDriver(internetExplorerOptions);
    }

    private void loadOpera(DesiredCapabilities capabilities) {
        OperaOptions operaOptions = new OperaOptions();
        operaOptions.merge(capabilities);
        driver = new OperaDriver(operaOptions);
    }

    protected void loadSafari(DesiredCapabilities capabilities) {
        SafariOptions safariOptions = new SafariOptions();
        safariOptions.merge(capabilities);
        driver = new SafariDriver();
    }

    protected void loadRemoteWebdriver(String gridURL, DesiredCapabilities capabilities) {
        log.info("Running on: " + gridURL);
        try {
            driver = new RemoteWebDriver(new URL(gridURL), capabilities);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("URL for remote webdriver is malformed!", e);
        }
    }

    protected String getBrowser() {
        String browser = System.getProperty("browser");
        if (isBlank(browser)) {
            if (isNotBlank(defaultBrowser)) {
                browser = defaultBrowser;
            } else {
                browser = "chrome";
            }
        }
        browser = browser.toLowerCase();
        return browser;
    }

    protected String getGridURL() {
        String urlStr = System.getProperty("gridURL");
        if (isNotBlank(urlStr)) {
            return urlStr;
        }
        return null;
    }

    protected void afterLoad() {
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
        if (maximizeBrowser) {
            driver.manage().window().maximize();
        }
    }

    public void quit() {
        if (driver != null) {
            try {
                driver.quit();
            } catch (UnreachableBrowserException unreachableBrowserException) {
                log.error(unreachableBrowserException.getMessage());
            }
        }
        driver = null;
    }

    public boolean isClosed() {
        return driver == null;
    }

    public String createScreenshot(String path) {
        try {
            log.info("Create screenshot to '{}'", path);
            if (driver == null) {
                log.error("Can not create screenshot because webdriver is null!");
                return null;
            }
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File destFile = new File(path);
            FileUtils.copyFile(screenshot, destFile);
            return destFile.getAbsolutePath();
        } catch (Exception e) {
            log.error("Exception at capture screenshot", e);
            return null;
        }
    }

}
