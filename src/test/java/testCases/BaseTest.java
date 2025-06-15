package testCases;


import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType; // Import OutputType
import org.openqa.selenium.TakesScreenshot; // Import TakesScreenshot
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import pageObjects.HomePage;
import utilities.LoggerClass;
import com.aventstack.extentreports.ExtentTest;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap; // For thread safety with map

public class BaseTest {
    private static final ThreadLocal<WebDriver> drivers = new ThreadLocal<>();
    protected Logger logger;
    protected ExtentTest test; // Will be set by listener or passed from listener

    protected static String homePageWindowHandle;
    protected static String diaryPageWindowHandle;
    protected static String summaryPageWindowHandle;

    // Use a ConcurrentHashMap to store test instances for the listener to access
    // Key: Thread ID, Value: Current BaseTest instance for that thread
    private static final ConcurrentHashMap<Long, BaseTest> testInstances = new ConcurrentHashMap<>();

    // Make this method public so the listener can access the WebDriver
    public WebDriver getDriver() {
        return drivers.get();
    }

    protected void setDriver(WebDriver driver) {
        drivers.set(driver);
    }

    @BeforeSuite
    public void setupSuite() {
        logger = LoggerClass.getLogger("DreamPortal");
        logger.info("Setting up test suite...");

        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        // options.addArguments("--headless");
        // options.addArguments("--disable-gpu");
        // options.addArguments("--window-size=1920,1080");

        WebDriver driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0)); // Set implicit wait to 0 when using explicit waits frequently
        // If you rely on implicit waits for some elements, keep it as Duration.ofSeconds(5) or similar.
        // For current structure, where explicit waits are used, 0 is safer.

        setDriver(driver);
        logger.info("Browser launched for the suite. Session ID: " + ((RemoteWebDriver) getDriver()).getSessionId());

        // Initial navigation and tab opening, done once for the suite
        navigateToDreamsPages();
    }

    @AfterSuite
    public void tearDownSuite() {
        logger.info("Tearing down test suite...");
        if (getDriver() != null) {
            getDriver().quit();
            drivers.remove(); // Clean up ThreadLocal
            logger.info("Browser closed for the suite.");
        }
    }

    private void navigateToDreamsPages() {
        WebDriver currentDriver = getDriver();
        HomePage home = new HomePage(currentDriver);
        home.openSite();
        logger.info("Site opened: " + currentDriver.getCurrentUrl());

        Assert.assertTrue(home.isLoaderGone(), "Loader did not disappear on initial load.");
        logger.info("Loader has disappeared.");

        Assert.assertTrue(home.isButtonVisible(), "'My Dreams' button not visible on initial load.");
        logger.info("'My Dreams' button is visible.");

        homePageWindowHandle = currentDriver.getWindowHandle();
        logger.info("Home Page Window Handle: " + homePageWindowHandle);

        home.clickMyDreams();
        logger.info("Clicked on 'My Dreams' button");

        WebDriverWait wait = new WebDriverWait(currentDriver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.numberOfWindowsToBe(3));
        logger.info("Successfully waited for 3 windows to be present.");

        Set<String> allWindows = currentDriver.getWindowHandles();
        List<String> windowList = new ArrayList<>(allWindows);

        for (String handle : windowList) {
            currentDriver.switchTo().window(handle);
            String url = currentDriver.getCurrentUrl();
            if (url.contains("index.html")) {
                homePageWindowHandle = handle;
            } else if (url.contains("dreams-diary.html")) {
                diaryPageWindowHandle = handle;
            } else if (url.contains("dreams-total.html")) {
                summaryPageWindowHandle = handle;
            }
        }
        logger.info("Window Handles Mapped - Home: " + homePageWindowHandle +
                    ", Diary: " + diaryPageWindowHandle +
                    ", Summary: " + summaryPageWindowHandle);

        currentDriver.switchTo().window(homePageWindowHandle);
        logger.info("Switched back to Home Page for subsequent test execution.");
    }

    @BeforeMethod
    public void setupMethod() {
        logger = LoggerClass.getLogger("DreamPortal");
        // Store the current test instance in the map for the listener to retrieve
        testInstances.put(Thread.currentThread().getId(), this);
    }

    @AfterMethod
    public void tearDownMethod() {
        // Remove the current test instance from the map
        testInstances.remove(Thread.currentThread().getId());
    }

    // Helper method for listener to get the current BaseTest instance
    public static BaseTest getCurrentTestInstance() {
        return testInstances.get(Thread.currentThread().getId());
    }

    /**
     * Takes a screenshot of the current browser window and returns it as a Base64 encoded string.
     * This format is suitable for embedding directly into HTML reports.
     *
     * @return Base64 encoded string of the screenshot, or null if an error occurs.
     */
    public String takeScreenshotAsBase64() {
        WebDriver driver = getDriver();
        if (driver instanceof TakesScreenshot) {
            logger.info("Attempting to take screenshot.");
            try {
                return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
            } catch (Exception e) {
                logger.error("Failed to take screenshot: " + e.getMessage());
                return null;
            }
        }
        logger.warn("Driver does not support taking screenshots.");
        return null;
    }
}