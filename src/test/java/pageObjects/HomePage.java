package pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class HomePage {
    WebDriver driver;

    public HomePage(WebDriver driver) {
        this.driver = driver;
    }

    By loadingIcon = By.id("loader");
    By myDreamsBtn = By.xpath("//button[contains(text(),'My Dreams')]");

    public void openSite() {
        driver.get("https://arjitnigam.github.io/myDreams/");
    }

    public boolean isLoaderGone() {
        // Wait for the loader to become invisible
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15)); // Increased wait time
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(loadingIcon));
    }

    public boolean isButtonVisible() {
        // Wait for the button to be visible
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(myDreamsBtn)).isDisplayed();
    }

    public void clickMyDreams() {
        // Wait for the button to be clickable before clicking
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(myDreamsBtn)).click();
    }
}