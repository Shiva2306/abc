package utilities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.MediaEntityBuilder; // Import MediaEntityBuilder

import java.io.IOException;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import testCases.BaseTest; // Import BaseTest

public class TestNGExtentReportListener implements ITestListener {

    private static ExtentReports extent = ExtentReportManager.getReport();
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    @Override
    public synchronized void onStart(ITestContext context) {
        System.out.println("Test Suite started: " + context.getName());
    }

    @Override
    public synchronized void onFinish(ITestContext context) {
        System.out.println("Test Suite finished: " + context.getName());
        ExtentReportManager.flushReport();
    }

    @Override
    public synchronized void onTestStart(ITestResult result) {
        String methodName = result.getMethod().getMethodName();
        String className = result.getTestClass().getName();
        String testName = className + " - " + methodName;
        ExtentTest extentTest = extent.createTest(testName);
        test.set(extentTest);
        System.out.println(testName + " started!");
    }

    @Override
    public synchronized void onTestSuccess(ITestResult result) {
        String testName = result.getTestClass().getName() + " - " + result.getMethod().getMethodName();
        if (test.get() != null) {
            test.get().log(Status.PASS, testName + " passed.");
        }
        System.out.println(testName + " passed!");
    }

    @Override
    public synchronized void onTestFailure(ITestResult result) {
        String testName = result.getTestClass().getName() + " - " + result.getMethod().getMethodName();
        if (test.get() != null) {
            test.get().log(Status.FAIL, testName + " failed.");
            test.get().log(Status.FAIL, result.getThrowable()); // Log the exception

            // *** Screenshot Logic for Failed Tests ***
            BaseTest currentTestInstance = BaseTest.getCurrentTestInstance();
            if (currentTestInstance != null) {
                String base64Screenshot = currentTestInstance.takeScreenshotAsBase64();
                if (base64Screenshot != null) {
                    test.get().fail("Screenshot:", MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot).build());
					System.out.println("Screenshot attached for " + testName + " failure.");
                } else {
                    System.err.println("Could not capture screenshot for " + testName + ".");
                }
            } else {
                System.err.println("Could not retrieve BaseTest instance for screenshot capture.");
            }
            // **********************************
        }
        System.out.println(testName + " failed!");
    }

    @Override
    public synchronized void onTestSkipped(ITestResult result) {
        String testName = result.getTestClass().getName() + " - " + result.getMethod().getMethodName();
        if (test.get() != null) {
            test.get().log(Status.SKIP, testName + " skipped.");
            test.get().log(Status.SKIP, result.getThrowable());
        }
        System.out.println(testName + " skipped!");
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        // Not typically implemented
    }
}