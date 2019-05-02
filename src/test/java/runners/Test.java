package runners;

import java.util.HashMap;
import java.util.List;

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
		System.out.println("test");
	}
}
