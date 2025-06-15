
package utilities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentReportManager {

    private static ExtentReports extent;

    // Private constructor to prevent instantiation
    private ExtentReportManager() {
    }

    public static ExtentReports getReport() {
        if (extent == null) {
            synchronized (ExtentReportManager.class) { // Synchronize for thread-safety
                if (extent == null) { // Double-check locking
                    String path = System.getProperty("user.dir") + "/reports/DreamPortalReport.html";
                    ExtentSparkReporter reporter = new ExtentSparkReporter(path);
                    reporter.config().setReportName("Dream Portal Automation Report");
                    reporter.config().setDocumentTitle("Dream Portal Test Results");
                    reporter.config().setTheme(com.aventstack.extentreports.reporter.configuration.Theme.STANDARD); // Optional: Dark theme

                    extent = new ExtentReports();
                    extent.attachReporter(reporter);
                    extent.setSystemInfo("Tester", "QA Intern");
                    extent.setSystemInfo("OS", System.getProperty("os.name"));
                    extent.setSystemInfo("Java Version", System.getProperty("java.version"));
                    extent.setSystemInfo("Browser", "Chrome");
                }
            }
        }
        return extent;
    }

    // This method is called by the listener to ensure the report is flushed
    public static void flushReport() {
        if (extent != null) {
            extent.flush();
        }
    }
}