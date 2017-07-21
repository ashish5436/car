package org.jbehave.webapp.taxi.pages;

import org.jbehave.webapp.frontend.element.WebElementEnhanced;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

public class LoginPage extends AbstractTaxiPage {

    public static final String URL = "login";

    @FindBy(id = "username")
    private WebElementEnhanced usernameInput;

    @FindBy(id = "password")
    private WebElementEnhanced passwordInput;

    @FindBy(xpath = "//button[@type='submit']")
    private WebElementEnhanced submitButton;

    @FindBy(partialLinkText = "Registrieren")
    private WebElementEnhanced registrationLink;

    @FindBy(className = "alert-success")
    private WebElementEnhanced alertSuccessDiv;

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void setUsername(String username) {
        usernameInput.setValue(username);
    }

    public void setPassword(String password) {
        passwordInput.setValue(password);
    }

    public void submitLogin() {
        submitButton.click();
    }

    public void clickRegistration() {
        registrationLink.click();
    }

    public boolean registeredMessageIsShown() {
        if (alertSuccessDiv.exists()) {
            return alertSuccessDiv.getText().contains("registriert");
        }
        return false;
    }

    @Override
    public String getURL() {
        return URL;
    }
}
