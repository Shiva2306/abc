package testCases;


import org.testng.Assert;
import org.testng.annotations.Test;
import pageObjects.HomePage;
import utilities.ExtentReportManager;

public class HomePageTest extends BaseTest {

    @Test(priority = 1, description = "Verifies home page loading and button visibility")
    public void verifyHomePageLoadAndButton() {
        test = ExtentReportManager.getReport().createTest("Home Page Load & Button Test");
        logger.info("Starting verifyHomePageLoadAndButton test.");

        // The site is already opened and "My Dreams" clicked in @BeforeSuite
        // We just need to switch to the home page tab and re-verify its state
        getDriver().switchTo().window(homePageWindowHandle);
        HomePage home = new HomePage(getDriver());

        Assert.assertTrue(home.isLoaderGone(), "Loader did not disappear on Home Page.");
        logger.info("Loader has disappeared on Home Page.");

        Assert.assertTrue(home.isButtonVisible(), "'My Dreams' button not visible on Home Page.");
        logger.info("'My Dreams' button is visible on Home Page.");

        // No need to click "My Dreams" again as it's handled in BaseTest @BeforeSuite
        logger.info("Finished verifyHomePageLoadAndButton test.");
    }
}