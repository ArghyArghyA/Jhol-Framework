package runners;

import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.winium.DesktopOptions;
import org.openqa.selenium.winium.WiniumDriver;

import com.aventstack.extentreports.Status;

import dataProviders.Configurations;
import dataProviders.Configurations.OutPutFields;
import managers.DataManager;


public class Test
{
	public static void main(String[] args) throws Exception
	{
//		System.setProperty("webdriver.winium.driver.desktop", "./src/main/resources/Winium.Desktop.Driver.exe");
////		Runtime.getRuntime().exec("C:\\Windows\\System32\\notepad.exe");
//		DesktopOptions options = new DesktopOptions();
//		options.setDebugConnectToRunningApp(true);;
//		WiniumDriver driver = new WiniumDriver(new URL("http://localhost:9999"), options);
////		driver.findElement(By.name("Start")).click();
//		driver.findElement(By.name("Text Editor")).sendKeys("sab golmal hai bhai sab golmal hai");
//		
//		System.out.println("complete");
		
//		System.setProperty("webdriver.chrome.driver", "./src/main/resources/chromedriver.exe");
//		WebDriver driver = new ChromeDriver();
//		driver.get("https://www.google.com/");
//		driver.findElement(By.cssSelector("input.gsfi")).sendKeys("jhol");
//		driver.findElement(By.cssSelector("input.gsfi")).submit();
//		driver.findElement(By.xpath("//h3[text()='Jhol (film) - Wikipedia']")).click();
//		driver.quit();
		
		List<HashMap<String, String>> Data =  DataManager.read();
		DataManager.createFeatureFile(Data);
//		System.out.println(Features);
	}
}
