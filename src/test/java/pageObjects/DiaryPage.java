package pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class DiaryPage {
    WebDriver driver;

    public DiaryPage(WebDriver driver) {
        this.driver = driver;
    }

    By rows = By.xpath("//table//tbody//tr");
    By columns = By.xpath("//table//thead//tr/th"); // Not directly used in methods but good to have

    public int getRowCount() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(rows));
        return driver.findElements(rows).size();
    }

    public boolean allRowsHaveData() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        List<WebElement> allRows = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(rows));
        for (int i = 1; i <= allRows.size(); i++) {
            for (int j = 1; j <= 3; j++) { // Assuming 3 columns as per assignment
                String xpath = "//table//tbody/tr[" + i + "]/td[" + j + "]";
                WebElement cell = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
                if (cell.getText().trim().isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean onlyGoodOrBadDreams() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        List<WebElement> allRows = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(rows));
        for (int i = 1; i <= allRows.size(); i++) {
            String xpath = "//table//tbody/tr[" + i + "]/td[3]"; // Assuming Dream Type is the 3rd column
            WebElement typeElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
            String type = typeElement.getText().trim();
            if (!(type.equalsIgnoreCase("Good") || type.equalsIgnoreCase("Bad"))) {
                return false;
            }
        }
        return true;
    }

    public String getDreamName(int row) {
        // Returns the dream name from a specific row
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        String xpath = "//table//tbody/tr[" + row + "]/td[1]"; // Assuming Dream Name is the 1st column
        WebElement dreamNameElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
        return dreamNameElement.getText().trim();
    }
}