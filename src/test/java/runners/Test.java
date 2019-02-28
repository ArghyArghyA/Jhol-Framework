package runners;

import java.net.URL;

import org.openqa.selenium.By;
import org.openqa.selenium.winium.DesktopOptions;
import org.openqa.selenium.winium.WiniumDriver;

public class Test
{


	public static void main(String[] args) throws Exception
	{
		System.setProperty("webdriver.winium.driver.desktop", "./src/main/resources/Winium.Desktop.Driver.exe");
//		Runtime.getRuntime().exec("C:\\Windows\\System32\\notepad.exe");
		DesktopOptions options = new DesktopOptions();
		options.setDebugConnectToRunningApp(true);;
		WiniumDriver driver = new WiniumDriver(new URL("http://localhost:9999"), options);
//		driver.findElement(By.name("Start")).click();
		driver.findElement(By.name("Text Editor")).sendKeys("sab golmal hai bhai sab golmal hai");
		
		System.out.println("complete");
	}
}
