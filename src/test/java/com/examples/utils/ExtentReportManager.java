package com.examples.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentReportManager implements ITestListener {

	private static ExtentReports extent;
	private static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();
	private static final Map<Thread, ExtentTest> testMap = new HashMap<>();

	public static ExtentTest getTest() {
		return extentTest.get();
	}

	@Override
	public synchronized void onStart(ITestContext context) {
		if (extent == null) {
			extent = createInstance();
		}
	}

	@Override
	public synchronized void onFinish(ITestContext context) {
		extent.flush(); // Ensure extent is always flushed at the end
	}

	@Override
	public synchronized void onTestStart(ITestResult result) {
		ExtentTest test = extent.createTest(result.getMethod().getMethodName());
		extentTest.set(test);
		testMap.put(Thread.currentThread(), test);
	}

	@Override
	public synchronized void onTestSuccess(ITestResult result) {
		ExtentTest test = testMap.get(Thread.currentThread());
		if (test != null) {
			test.pass("Test Passed");
		}
	}

	@Override
	public synchronized void onTestFailure(ITestResult result) {
		ExtentTest test = testMap.get(Thread.currentThread());
		if (test != null) {
			test.fail(result.getThrowable());
		}
	}

	@Override
	public synchronized void onTestSkipped(ITestResult result) {
		ExtentTest test = testMap.get(Thread.currentThread());
		if (test != null) {
			test.skip(result.getThrowable());
		}
	}

	private static ExtentReports createInstance() {
		String timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
		String reportName = "Test-Report-" + timestamp + ".html";
		File reportFile = new File("./target/extent-reports/" + reportName);

		ExtentSparkReporter htmlReporter = new ExtentSparkReporter(reportFile);

		htmlReporter.config().setTheme(Theme.STANDARD);
		htmlReporter.config().setDocumentTitle("Stock Information Test Automation Report");
		htmlReporter.config().setReportName("Stock Information Test Results");

		extent = new ExtentReports();
		extent.attachReporter(htmlReporter);
		extent.setSystemInfo("OS", System.getProperty("os.name"));
		extent.setSystemInfo("Java Verison", System.getProperty("java.version"));
		extent.setSystemInfo("Environment", "Test");

		return extent;
	}
}