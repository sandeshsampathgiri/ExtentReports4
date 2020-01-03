package com.hubdpot.tests;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.util.Collections;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.WebDriver;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import io.github.bonigarcia.wdm.WebDriverManager;

public class NOPCommerceDemo2 {

	
	//This is the newly added file
	public WebDriver driver;
	public ExtentHtmlReporter htmlReporter;
	public ExtentReports extent;
	public ExtentTest test;

	@BeforeTest
	public void setExtent() {
		htmlReporter = new ExtentHtmlReporter(System.getProperty("user.dir") + "/test-output/myExtentReport.html");
		htmlReporter.config().setDocumentTitle("Automation Test Report");
		htmlReporter.config().setReportName("Functional Test Report");
		htmlReporter.config().setTheme(Theme.DARK);

		extent = new ExtentReports();
		extent.attachReporter(htmlReporter);
		extent.setSystemInfo("Hostname", "My local machine");
		extent.setSystemInfo("Operating System", "Windows 10");
		extent.setSystemInfo("QAName", "Sandesh");
		extent.setSystemInfo("Browser", "Chrome");

	}

	@AfterTest
	public void endReport() {
		extent.flush();

	}

	@BeforeMethod
	public void setUp() {
		WebDriverManager.chromedriver().setup();

		ChromeOptions options = new ChromeOptions();

		options.addArguments("--incognito");

		options.addArguments("--disable-notifications");

		options.setExperimentalOption("useAutomationExtension", false);

		options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));

		options.addArguments("start-maximized");

		options.setAcceptInsecureCerts(true);

		driver = new ChromeDriver(options);

		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		driver.manage().deleteAllCookies();

		driver.get("http://demo.nopcommerce.com/");

	}

	@Test
	public void pageTitleTest() {
		test = extent.createTest("page Title test");

		String pageTitle = driver.getTitle();
		Assert.assertEquals(pageTitle, "nopCommerce demo123 store");

	}

	@Test
	public void pageURLTest() {
		test = extent.createTest("page URL test");

		String pageURL = driver.getCurrentUrl();
		Assert.assertEquals(pageURL, pageURL);

	}

	@AfterMethod
	public void tearDown(ITestResult result) throws IOException {
		if (result.getStatus() == ITestResult.FAILURE) {
			test.log(Status.FAIL, "Test case failed is: " + result.getName());
			test.log(Status.FAIL, "Test case failed is: " + result.getThrowable());

			String screenshotPath = NOPCommerceDemo2.getScreenshot(driver, result.getName());
			test.addScreenCaptureFromPath(screenshotPath);

		}

		else if (result.getStatus() == ITestResult.SKIP) {
			test.log(Status.SKIP, "Test case skipped is: " + result.getName());

		}

		else if (result.getStatus() == ITestResult.SUCCESS) {
			test.log(Status.PASS, "Test case passed is: " + result.getName());

		}
		
		driver.quit();

	}

	public static String getScreenshot(WebDriver driver, String screenshotName) throws IOException {

		String dateName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
		TakesScreenshot ts = (TakesScreenshot) driver;
		File source = ts.getScreenshotAs(OutputType.FILE);

		String destination = System.getProperty("user.dir") + "/Screenshots/" + screenshotName + dateName + ".png";
		File finalDestination = new File(destination);
		FileUtils.copyFile(source, finalDestination);
		return destination;
	}

}
