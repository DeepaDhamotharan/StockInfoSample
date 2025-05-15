package com.examples.tests;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentTest;
import com.examples.base.BaseTest;
import com.examples.utils.ExtentReportManager;

public class StockInfoTest extends BaseTest{
	
	private ExtentTest extentTest;
	private String browserName;;
	private String stockSymbol;
	String searchLocator = "//input[@placeholder]";
	String resultLocator = "//p//span[2][text()='TATAMOTORS']";
	String highPriceLocator = "//*[@id = 'week52highVal']";
	String lowPriceLocator = "//*[@id = 'week52lowVal']";
	
	
	@BeforeMethod
	public void beforeMethod(ITestContext context) {
		extentTest = ExtentReportManager.getTest();
		browserName = context.getCurrentXmlTest().getParameter("browser");
		stockSymbol = context.getCurrentXmlTest().getParameter("stockSymbol");
		
		if(extentTest != null)
		{
			extentTest.info("Starting test method: " +  context.getName() + " on  " + browserName + " for stock: " + stockSymbol);
			
		}
		else
		{
			System.err.println("ExtentTest is null in beforemethod for: " + context.getName());
		}
		this.browserName = browserName;
	}
	
	@Test
	public void verifyStockInformationDisplay(ITestContext context)
	{
		extentTest = ExtentReportManager.getTest();
		if (extentTest != null)
		{
			extentTest.info("Executing verify title test steps on " + browserName);
		}
		
		String url = "https://www.nseindia.com";
		logger.info(" Navigating to: {}", url);
		driver.get(url);
		WebDriverWait wait = new WebDriverWait( driver, Duration.ofSeconds(100));
		
		try
		{
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(searchLocator)));
			WebElement searchInput = driver.findElement(By.xpath(searchLocator));
			searchInput.sendKeys(stockSymbol);
			
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(resultLocator)));
			WebElement stockLink = driver.findElement((By.xpath("//p//span[2][text() = '" + stockSymbol +"']")));
			stockLink.click();
			
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(highPriceLocator)));
			wait.until(ExpectedConditions.not(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath(highPriceLocator)), "-" )));
			WebElement highElement = driver.findElement(By.xpath(highPriceLocator));
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(lowPriceLocator)));
			WebElement lowElement = driver.findElement(By.xpath(lowPriceLocator));
			
			String highPrice = highElement.getText();
			String lowPrice = lowElement.getText();
			
			logger.info("52 week High: {}", highPrice);
			logger.info("52 week Low: {}", lowPrice);
			extentTest.info("52 week High: " + highPrice);
			extentTest.info("52 week Low: " + lowPrice);
		
		}catch(Exception e)
		{
			logger.info("Error retrieving stock info: " + e.getMessage());
			extentTest.fail("Error retrieving stock info: " + e.getMessage());
			Assert.fail("Failed to retrieve stock information: " + e.getMessage());
		}
		
	
	}
	
}
	