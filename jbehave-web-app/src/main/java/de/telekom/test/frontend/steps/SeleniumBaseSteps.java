package de.telekom.test.frontend.steps;

import de.telekom.test.frontend.page.UrlMatchesExpectation;
import de.telekom.test.steps.Steps;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Created by d.keiss on 08.11.2016.
 */
@Steps
public final class SeleniumBaseSteps extends SeleniumSteps {

    @When("run browserback")
    public void runBrowserback() {
        webDriverWrapper.getDriver().navigate().back();
    }

    @When("run browserforward")
    public void runBrowserforward() {
        webDriverWrapper.getDriver().navigate().forward();
    }

    @Then("the page with url $url is shown")
    public void thePageWithUrlIsShown(String url) {
        WebDriver driver = webDriverWrapper.getDriver();
        Wait<WebDriver> wait = new WebDriverWait(driver, 30);
        wait.until(new UrlMatchesExpectation(driver, url, String.format("The url %s is not shown! (was: %s)", url, driver.getCurrentUrl())));
    }

}