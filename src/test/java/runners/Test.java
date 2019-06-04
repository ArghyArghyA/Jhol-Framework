package runners;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import com.google.common.io.Files;
import com.jacob.com.LibraryLoader;

import io.github.bonigarcia.wdm.WebDriverManager;
import managers.APIUtil;
import managers.APIUtil.Method;
import managers.APIUtil.Response;
import managers.utilities.AutoItX;
import managers.utilities.Basic;

public class Test {

	public static WebElement expandRootElement(WebDriver driver, WebElement ele) {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return arguments[0].shadowRoot", ele);
	}

	public void A() {
		StackTraceElement[] traces = Thread.currentThread().getStackTrace();
		for (StackTraceElement trace : traces) {
			System.out.println(trace.getClassName());
			System.out.println(trace.toString().split("\\(")[1].replace(")", ""));
			System.out.println(trace.toString() + "random text");
		}
	}

	public void B() {
		System.out.println(this.getClass().getName());
//		A();
	}

	public static void main(String[] args) throws Exception {
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

//		List<HashMap<String, String>> Data =  DataManager.read();
//		DataManager.createFeatureFile(Data);
//		System.out.println(Features);

		/*
		 * String text = "[[" +
		 * "        {\"firstName\": \"John\", \"lastName\": \"Doe\"}, " +
		 * "        {\"firstName\": \"Anna\", \"lastName\": \"Smith\"}, " +
		 * "        {\"firstName\": \"Peter\", \"lastName\": \"Jones\"}" + "    ], [" +
		 * "        {\"firstName\": \"John\", \"lastName\": \"Doe\"}, " +
		 * "        {\"firstName\": \"Anna\", \"lastName\": \"Smith\"}, " +
		 * "        {\"firstName\": \"Peter\", \"lastName\": \"Jones\"}" + "    ]" +
		 * "]";
		 * 
		 * System.out.println(text); managers.utilities.Response r = new
		 * managers.utilities.Response(); r.Result = text;
		 * System.out.println(r.toJSON().getJSONObject(0));
		 */
		/*
		 * Runtime.getRuntime().exec("TASKKILL /F /IM CHROME.EXE /IM CHROMEDRIVER.EXE");
		 * WebDriverManager.chromedriver().setup(); WebDriver driver = new
		 * ChromeDriver(); driver.manage().window().maximize();
		 * driver.get("chrome://settings/content/pdfDocuments"); WebElement ele =
		 * Test.expandRootElement(driver,
		 * driver.findElement(By.cssSelector("settings-ui"))); ele =
		 * Test.expandRootElement(driver,ele.findElement(By.cssSelector("settings-main")
		 * )); ele = Test.expandRootElement(driver,ele.findElement(By.cssSelector(
		 * "settings-basic-page"))); ele =
		 * ele.findElement(By.cssSelector("settings-section")); ele =
		 * Test.expandRootElement(driver,ele.findElement(By.cssSelector(
		 * "settings-privacy-page"))); ele =
		 * ele.findElement(By.cssSelector("settings-animated-pages")); ele =
		 * ele.findElement(By.cssSelector("settings-subpage")); ele =
		 * Test.expandRootElement(driver,ele.findElement(By.cssSelector(
		 * "settings-pdf-documents"))); ele =
		 * Test.expandRootElement(driver,ele.findElement(By.cssSelector(
		 * "settings-toggle-button"))); ele =
		 * ele.findElement(By.cssSelector("cr-toggle"));
		 * ((JavascriptExecutor)driver).executeScript("arguments[0].click();", ele);
		 * driver.get("chrome://settings"); Thread.sleep(100); // ele =
		 * driver.findElement(By.cssSelector("settings-ui")); ele =
		 * Test.expandRootElement(driver,
		 * driver.findElement(By.cssSelector("settings-ui"))); ele =
		 * ele.findElement(By.cssSelector("div#container")); ele =
		 * Test.expandRootElement(driver,
		 * ele.findElement(By.cssSelector("settings-main"))); ele =
		 * Test.expandRootElement(driver,
		 * ele.findElement(By.cssSelector("settings-basic-page")));
		 * 
		 * ((JavascriptExecutor)driver).executeScript("arguments[0].click();",
		 * ele.findElement(By.cssSelector("h2")).findElement(By.cssSelector(
		 * "paper-button")));
		 * 
		 * ele = ele.findElement(By.cssSelector("div#advancedPage")); ele =
		 * ele.findElement(By.cssSelector("settings-section[section = 'downloads']"));
		 * ele = Test.expandRootElement(driver,
		 * ele.findElement(By.cssSelector("settings-downloads-page"))); ele =
		 * ele.findElement(By.cssSelector("settings-animated-pages")); ele =
		 * ele.findElement(By.cssSelector("div#defaultDownloadPath"));
		 * ((JavascriptExecutor)driver).executeScript("arguments[0].innerHTML = 'C:\\';"
		 * , ele); System.out.println(ele.getAttribute("innerHTML"));
		 * 
		 * // driver.quit();
		 */

		/*
		 * File jacobDLL = new File("src/main/resources/jacob-1.19/jacob-1.19-x64.dll");
		 * System.setProperty(LibraryLoader.JACOB_DLL_PATH, jacobDLL.getAbsolutePath());
		 * AutoItX autoIt = new AutoItX(); Runtime.getRuntime().exec("notepad");
		 * autoIt.winActivate("Untitled - Notepad");
		 * autoIt.winWaitActive("Untitled - Notepad"); autoIt.send("test");
		 * autoIt.winClose("Untitled - Notepad"); autoIt.winWaitActive("Notepad");
		 * autoIt.send("!n");
		 */

		/*
		 * Test t = new Test(); t.B();
		 */
		APIUtil ap = new APIUtil();
		ap.setURI("https://www.google.com/?q='test'");
		ap.setParameter(new HashMap<String, String>() {{put("q", "Test");}});
//		ap.setAuthentication(new Basic("ArghyArghya", "f8cda53181fd92a7e12548fd4a6541c11b47f00b"));
		ap.GET();

	}
}
