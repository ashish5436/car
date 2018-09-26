package de.telekom.test.bddwebapp.frontend.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

/**
 * Abstract base class for page objects. Checks the current url when creating.
 *
 * @author Daniel Keiss {@literal <daniel.keiss@telekom.de>}
 * <p>
 * Copyright (c) 2018 Daniel Keiss, Deutsche Telekom AG
 * This file is distributed under the conditions of the Apache License, Version 2.0.
 * For details see the file license on the toplevel.
 */
public abstract class Page {

    protected final WebDriver driver;

    public Page(WebDriver driver) {
        this.driver = driver;
        checkUrl();
    }

    public void checkUrl() {
        Wait<WebDriver> wait = new WebDriverWait(driver, 30);
        wait.until(new UrlMatchesExpectation(getURL(), this.getClass().getName()));
    }

    public void reload() {
        driver.navigate().refresh();
    }

    public void waitFor(long milliseconds) {
        try {
            TimeUnit.MILLISECONDS.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public abstract String getURL();

}
