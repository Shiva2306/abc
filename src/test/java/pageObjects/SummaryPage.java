package pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class SummaryPage {
    WebDriver driver;

    public SummaryPage(WebDriver driver) {
        this.driver = driver;
    }

    By goodDreams = By.id("good");
    By badDreams = By.id("bad");
    By totalDreams = By.id("total");
    By recurringDreams = By.id("recurring");

    public int getGoodCount() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(goodDreams));
        return Integer.parseInt(driver.findElement(goodDreams).getText());
    }

    public int getBadCount() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(badDreams));
        return Integer.parseInt(driver.findElement(badDreams).getText());
    }

    public int getTotalCount() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(totalDreams));
        return Integer.parseInt(driver.findElement(totalDreams).getText());
    }

    public int getRecurringCount() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(recurringDreams));
        return Integer.parseInt(driver.findElement(recurringDreams).getText());
    }
}