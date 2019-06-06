package managers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.winium.DesktopOptions;
import org.openqa.selenium.winium.WiniumDriver;

import com.aventstack.extentreports.Status;
import com.paulhammant.ngwebdriver.NgWebDriver;

import io.github.bonigarcia.wdm.WebDriverManager;
import managers.utilities.AutoItX;
import settings.Configurations;

/**
 * @author Alpha Romeo
 *
 */
public class DriverUtil {
	protected static Action act;
	static HashMap<String, String> dictionary = new HashMap<String, String>();
	static Reporter reporter;
	private static String webElementStyle = "";
	protected static WebDriverWait shortWait;
	protected static WebDriverWait longWait;
	static WebDriver driver;
	private static WebElement webElement;
	private static WebElement wElement;
	static WiniumDriver wDriver;
	protected static AutoItX aDriver;

	public DriverUtil(Reporter reportManager) {
		super();
		DriverUtil.reporter = reportManager;
	}

	/**
	 * Appends to already existing key, value pair. If the pair associated with the
	 * given key does not exist, a new key, value pair is generated
	 * 
	 * @param key   key of the key, value pair
	 * @param value Value to append to existing one
	 */
	protected void append(String key, String value) {
		if (dictionary.get(key) != null)
			dictionary.put(key, dictionary.get(key) + value);
		else
			dictionary.put(key, value);
	}

	/**
	 * Asserts if current title starts with the provided <code>String</code>
	 * expectedTitle
	 * 
	 * @param expectedTitle <code>String</code> to be matched with current Title
	 * 
	 * @return <code>true</code> or <code>false</code> depending on whether title
	 *         starts with expectedTitle or not.
	 */
	protected boolean assertTitle(String expectedTitle) {
		try {
			assert driver.getTitle().toUpperCase().startsWith(expectedTitle.toUpperCase());
			log(Status.INFO, "Successfully Navigated to " + expectedTitle);
		} catch (AssertionError e) {
			log(Status.INFO, "Unable to navigate to " + expectedTitle, e);
			return false;
		}
		return true;
	}

	/**
	 * Brings the window to front for given Element. Strictly applicable for Windows
	 * Automation. Executes little command-line applet BringToFront placed at
	 * "./src/main/resources/" for the same which accepts processID for the
	 * application as argument
	 * 
	 * @param element The WebELement for which the associated window needs to be
	 *                brought to front
	 */
	private void bringToFront(WebElement element) {
		try {
			Runtime.getRuntime().exec("./src/main/resources/BringToFront.exe " + element.getAttribute("ProcessId"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Clears the input field identified by the provided <code>By</code> object by
	 * using Selenium <code>WebElement.clear()</code>. If it succeeds to clear
	 * returns <code>true</code>, <code>false</code> otherwise.
	 * 
	 * <p>
	 * Before attempting to do that, waits for page to load if
	 * <code>waitForPageLoad</code> is <code>true</code>. Explicitly waits for
	 * element to be click-able if <code> explicitWait </code> is <code>true</code>.
	 * </p>
	 * 
	 * @param by              an <code>By</code> object to identify the input field
	 * @param waitForPageLoad a <code>boolean</code> value denoting whether to wait
	 *                        for page to finish loading or not
	 * @param explicitWait    a <code>boolean</code> value denoting whether to wait
	 *                        explicitly for the element to be click-able
	 * @return <code>true</code> if it succeeds to type into the field,
	 *         <code>false</code> otherwise
	 */
	protected boolean clear(By by, boolean waitForPageToLoad, boolean explicitWait) {
		WebElement ele;
		try {
			if (waitForPageToLoad)
				waitForPageToLoad();
			if (explicitWait) {
				ele = shortWait.until(ExpectedConditions.elementToBeClickable(by));
			} else {
				ele = driver.findElement(by);
			}
			scrollToView(ele);
			highlight(ele);
			ele.clear();
			log(Status.INFO, "Successfully cleared " + by.toString());
		} catch (Exception e) {
			log(Status.INFO, "Unable to fill " + by.toString(), e);
			return false;
		}
		return true;
	}

	/**
	 * Clears the input field identified by the provided <code>By</code> object by
	 * using Selenium <code>WebElement.clear()</code>. If it succeeds to clear
	 * returns <code>true</code>, <code>false</code> otherwise.
	 * 
	 * <p>
	 * Before attempting to find the element, waits for page to load and explicitly
	 * waits for element to be click-able by default.
	 * </p>
	 * 
	 * @param by an <code>By</code> object to identify the input field
	 * @return <code>true</code> if it succeeds to type into the field,
	 *         <code>false</code> otherwise
	 */
	protected boolean clear(By by) {
		return clear(by, true, true);
	}

	/**
	 * Clears the input <code>WebElement</code> object by using Selenium
	 * <code>WebElement.clear()</code>. If it succeeds to clear returns
	 * <code>true</code>, <code>false</code> otherwise.
	 * 
	 * <p>
	 * Before attempting to do that, waits for page to load if
	 * <code>waitForPageLoad</code> is <code>true</code>. Explicitly waits for
	 * element to be click-able if <code> explicitWait </code> is <code>true</code>.
	 * </p>
	 * 
	 * @param ele an <code>WebElement</code> object to identify the input field
	 * @return <code>true</code> if it succeeds to type into the field,
	 *         <code>false</code> otherwise
	 */
	protected boolean clear(WebElement ele) {
		try {
			scrollToView(ele);
			highlight(ele);
			ele.clear();
			log(Status.INFO, "Successfully cleared " + ele.toString());
		} catch (Exception e) {
			log(Status.INFO, "Unable to fill " + ele.toString(), e);
			return false;
		}
		return true;
	}

	/**
	 * Clicks on an <code>WebElement</code> identified by the provided
	 * <code>By</code> object. If it succeeds to click returns <code>true</code>,
	 * <code>false</code> otherwise.
	 * 
	 * <p>
	 * Before attempting to find the element, waits for page to load if
	 * <code>waitForPageLoad</code> is <code>true</code>. Explicitly waits for
	 * element to be click-able if <code> explicitWait </code> is <code>true</code>.
	 * </p>
	 * 
	 * @param by              an <code>By</code> object to identify the element
	 * @param waitForPageLoad a <code>boolean</code> value denoting whether to wait
	 *                        for page to finish loading or not
	 * @param explicitWait    a <code>boolean</code> value denoting whether to wait
	 *                        explicitly for the element to be click-able
	 * @return <code>true</code> if it succeeds to click, <code>false</code>
	 *         otherwise
	 */
	protected boolean click(By by, boolean waitForPageLoad, boolean explicitWait) {
		try {
			if (waitForPageLoad)
				waitForPageToLoad();
			WebElement ele = explicitWait ? shortWait.until(ExpectedConditions.elementToBeClickable(by))
					: driver.findElement(by);
			scrollToView(ele);
			highlight(ele);
			ele.click();
			log(Status.INFO, "Successfully clicked on " + by.toString());

		} catch (Exception e) {
			log(Status.INFO, "Unable to click on " + by.toString(), e);
			return false;
		}
		return true;
	}

	/**
	 * Clicks on an <code>WebElement</code> identified by the provided
	 * <code>By</code> object. If it succeeds to click returns <code>true</code>,
	 * <code>false</code> otherwise.
	 * 
	 * <p>
	 * Before attempting to find the element, waits for page to load and explicitly
	 * waits for element to be click-able by default.
	 * </p>
	 * 
	 * @param by an <code>By</code> object to identify the element
	 * @return <code>true</code> if it succeeds to click, <code>false</code>
	 *         otherwise
	 */
	protected boolean click(By by) {
		return click(by, true, true);
	}

	/**
	 * Clicks on provided <code>WebElement</code>.
	 * 
	 * <p>
	 * If it succeeds to click returns <code>true</code>, <code>false</code>
	 * otherwise.
	 * </p>
	 * 
	 * @param webElement an <code>WebElement</code> object
	 * @return <code>true</code> if it succeeds to click, <code>false</code>
	 *         otherwise
	 */
	protected boolean click(WebElement webElement) {
		try {
			scrollToView(webElement);
			highlight(webElement);
			webElement.click();
			log(Status.INFO, "Successfully clicked on " + webElement.toString());

		} catch (Exception e) {
			log(Status.INFO, "Unable to click on " + webElement.toString(), e);
			return false;
		}
		return true;
	}

	/**
	 * Switches to a window and try to closes it. Then again switches to another
	 * window.
	 * 
	 * This switches to window with a match in <code>driver.getTitle()</code> or
	 * <code>driver.getCurrentUrl(). Partial URL or Title is passed as <code>String</code>
	 * to be compared. Returns <code>true</code> or <code>false</code> depending on
	 * whether successful to switch to Final window or not.
	 * 
	 * <p>
	 * Waits for the driver window to check page loading is completed or not before
	 * comparing the Title/URL with the arguments if <code>wait</code> is provided
	 * as <code>true</code>
	 * </P>
	 * 
	 * @param TitleOrURLToBeClosed <code>String</code> part of Title or URL of the
	 *                             Target Window that needs to be closed
	 * @param FinalTitleOrUrl      <code>String</code> part of Title or URL of the
	 *                             Target Window that needs to be switched
	 * @param wait                 <code>Boolean</code> value denoting whether to
	 *                             wait for page to finish loading or not
	 * @return <code>true</code> or <code>false</code> depending on whether
	 *         successful to switch to Final Target window or not.
	 */
	protected boolean closeAndSwitchToWindow(String TitleOrURLToBeClosed, String FinalTitleOrUrl, boolean wait) {
		boolean success = false;
		Set<String> windowHandles = driver.getWindowHandles();
		for (String windowHandle : windowHandles) {
			driver.switchTo().window(windowHandle);
			if (wait)
				waitForPageToLoad();
			if (driver.getTitle().contains(TitleOrURLToBeClosed)
					|| driver.getCurrentUrl().contains(TitleOrURLToBeClosed)) {
				success = true;
				windowHandles.remove(windowHandle);
				break;
			}
		}
		if (success) {
			log(Status.INFO, "Successfully switched to " + TitleOrURLToBeClosed);
			try {
				driver.close();
				log(Status.INFO, "Successfully Closed " + TitleOrURLToBeClosed);
			} catch (Exception e) {
				log(Status.INFO, "Unable to close " + TitleOrURLToBeClosed);
				return false;
			}
		} else {
			log(Status.INFO, "Unable to switch to " + TitleOrURLToBeClosed);
//			return false;
		}

		success = false;
		for (String windowHandle : windowHandles) {
			driver.switchTo().window(windowHandle);
			if (wait)
				waitForPageToLoad();
//			System.out.println(driver.getTitle());
//			System.out.println(driver.getCurrentUrl());
			if (driver.getTitle().contains(FinalTitleOrUrl) || driver.getCurrentUrl().contains(FinalTitleOrUrl)) {
				success = true;
				break;
			}
		}

		if (success) {
			log(Status.INFO, "Successfully switched to " + FinalTitleOrUrl);
		} else {
			log(Status.INFO, "Unable to switch to " + FinalTitleOrUrl);
			return false;
		}
		driver.manage().window().maximize();
		return true;

	}

	/**
	 * An sophisticated fluentwait method with provision of customization.
	 * 
	 * @param classType a Class object for the class where methods are declared
	 *                  which needs to be evaluated to be true
	 * @param methods   the method names all of which must be true in order to
	 *                  complete the contract of FluentWait
	 * @return boolean value depending on complete contract or time-out
	 */
	protected boolean fluentWait(Class<?> classType, String... methods) {
		Wait<WebDriver> fWait = new FluentWait<WebDriver>(driver)
				.withTimeout(Duration.ofSeconds(Configurations.LongTimeOut)).pollingEvery(Duration.ofMillis(100))
				.ignoring(Exception.class);
		return fWait.until(new Function<WebDriver, Boolean>() {
			public Boolean apply(WebDriver driver) {
				boolean proceed = true;
				for (String methodName : methods) {
					try {
						proceed = proceed && (boolean) classType.getMethod(methodName).invoke(classType.newInstance());
					} catch (Exception e) {
						System.out.println(e);
						return false;
					}
				}
				return proceed;
			}
		});
	}

	/**
	 * Generates a random number of the specified length
	 * 
	 * @param length <code>Integer</code> length of the required Number
	 * @return A randomly generated <code>long</code> number of the specified length
	 */
	protected long generateRandomNumber(int length) {
		long result = 0;
		for (int i = 0; i < length; i++) {
			result = result * 10 + (new Random().nextInt(10));
		}
		if (Long.toString(result).length() != length)
			return generateRandomNumber(length);
		else
			return result;

	}

	/**
	 * Generates a random alphabetic string of the specified length
	 * 
	 * @param length <code>Integer</code> length of the required String
	 * @return A randomly generated <code>String</code> of the specified length
	 */
	private String generateRandomString(int length) {
		return RandomStringUtils.randomAlphabetic(length);
	}

	/**
	 * <p>
	 * Returns the <code>WebElement</code> identified by the provided identifier
	 * <code>by</code>. If it fails to identify any element with the given
	 * identifier it returns <code>null</code>.
	 * </p>
	 * <p>
	 * Before attempting to find the element, waits for page to load and explicitly
	 * waits for element to be click-able by default.
	 * </p>
	 * 
	 * @param by a <code>By</code> object to identify the element
	 * @return the <code>WebElement</Code> (if) identified by the <code>By</code>
	 *         object. <code>null</code> otherwise
	 */
	protected static WebElement get(By by) {
		return get(by, true, true);
	}

	/**
	 * <p>
	 * Returns the <code>WebElement</code> identified by the provided identifier
	 * <code>by</code>. If it fails to identify any element with the given
	 * identifier it returns <code>null</code>.
	 * </p>
	 * 
	 * <p>
	 * Before attempting to find the element, waits for page to load if
	 * <code>waitForPageLoad</code> is <code>true</code>. Explicitly waits for
	 * element to be click-able if <code> explicitWait </code> is <code>true</code>.
	 * </p>
	 * 
	 * @param by              an <code>By</code> object to identify the element
	 * @param waitForPageLoad a <code>boolean</code> value denoting whether to wait
	 *                        for page to finish loading or not
	 * @param explicitWait    a <code>boolean</code> value denoting whether to wait
	 *                        explicitly for the element to be click-able
	 * @return the <code>WebElement</Code> (if) identified by the <code>By</code>
	 *         object. <code>null</code> otherwise
	 */
	protected static WebElement get(By by, boolean waitForPageLoad, boolean explicitWait) {
		WebElement ele = null;
		try {
			if (waitForPageLoad)
				waitForPageToLoad();
			if (explicitWait) {
				ele = shortWait.until(ExpectedConditions.elementToBeClickable(by));
			} else {
				ele = driver.findElement(by);
			}
			scrollToView(ele);
			highlight(ele);
			log(Status.INFO, "Successfully identified " + by.toString());

		} catch (Exception e) {
			log(Status.INFO, "Unable to identify " + by.toString(), e);
		}
		return ele;
	}

	/**
	 * Returns <code>List</code> of <code>WebElement</code>(s) identified by the
	 * provided identifier <code>by</code>.
	 * 
	 * <p>
	 * Before attempting to find the elements, waits for page to load and explicitly
	 * waits for number of element to be non-zero by default
	 * </p>
	 * 
	 * @param by an <code>By</code> object to identify the element(s)
	 * @return the <code>List</code> of <code>WebElement</Code>(s) identified by the
	 *         <code>By</code> object.
	 */
	protected List<WebElement> getAll(By by) {
		return getAll(by, true, true);
	}

	/**
	 * Gets the value associated the given key from the datasheet for the current
	 * testcase
	 * 
	 * @param keyWord keyword to get value for
	 * @return string value for the key
	 */
	protected String get(String keyWord) {
		try {
			return dictionary.get(keyWord).trim();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Returns an array by splitting the value associated with the given key from
	 * the data-sheet by given separator
	 * 
	 * @param keyWord keyword to get value for
	 * @param regex   regular expression to split the value
	 * @return array of strings
	 */
	protected String[] get(String keyWord, String regex) {
		try {
			return dictionary.get(keyWord).trim().split(regex);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Returns string at the given index from the array, generated by splitting the
	 * value associated with the given key from the data-sheet by given separator
	 * 
	 * @param keyWord keyword to get value for
	 * @param regex   regular expression to split the value
	 * @return string at the given index after splitting the data, retrieved by the
	 *         keyword, by the given regex.
	 */
	protected String get(String keyWord, String regex, int index) {
		try {
			return dictionary.get(keyWord).trim().split(regex)[index].trim();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Returns <code>List</code> of <code>WebElement</code>(s) identified by the
	 * provided identifier <code>by</code>.
	 * 
	 * <p>
	 * Before attempting to find the elements, waits for page to load if
	 * <code>waitForPageLoad</code> is <code>true</code>. Explicitly waits for
	 * number of element to be non-zero if <code> explicitWait </code> is
	 * <code>true</code>.
	 * </p>
	 * 
	 * @param by              an <code>By</code> object to identify the element(s)
	 * @param waitForPageLoad a <code>boolean</code> value denoting whether to wait
	 *                        for page to finish loading or not
	 * @param explicitWait    a <code>boolean</code> value denoting whether to wait
	 *                        explicitly for number of elements to be non-zero
	 * @return the <code>List</code> of <code>WebElement</Code>(s) identified by the
	 *         <code>By</code> object.
	 */
	protected List<WebElement> getAll(By by, boolean waitForPageLoad, boolean explicitWait) {
		List<WebElement> ele = new ArrayList<WebElement>();
		try {
			if (waitForPageLoad)
				waitForPageToLoad();
			if (explicitWait)
				shortWait.until(ExpectedConditions.numberOfElementsToBeMoreThan(by, 0));
			ele = driver.findElements(by);

			log(Status.INFO, "Successfully identified " + by.toString());

		} catch (Exception e) {
			log(Status.INFO, "Unable to identify " + by.toString(), e);
		}
		return ele;
	}

	/**
	 * Returns some attribute-value for the Element identified by the
	 * <code>By</code> object. Before attempting that it waits for the page to load
	 * completely
	 * 
	 * @param by        <code>By</code> identifier for which some attribute-value
	 *                  needs to be extracted
	 * @param attribute <code>String</code> attribute which needs to be extracted
	 * @return <code>String</code> attribute value extracted for the Element
	 *         identified by the <code>By</code> object
	 */
	protected String getAttribute(By by, String attribute) {
		String result = "";
		try {
			waitForPageToLoad();
			result = driver.findElement(by).getAttribute(attribute);
		} catch (Exception e) {

			log(Status.INFO, "Unable to get " + attribute + " from " + by.toString(), e);
		}
		return result;
	}

	/**
	 * Returns some attribute-value from the Element.
	 * 
	 * @param ele       <code>WebElement</code> for which some attribute-value needs
	 *                  to be extracted
	 * @param attribute <code>String</code> attribute which needs to be extracted
	 * @return <code>String</code> attribute value extracted for the Element
	 *         identified by the <code>By</code> object
	 */
	protected String getAttribute(WebElement ele, String attribute) {
		String result = "";
		try {
			result = ele.getAttribute(attribute);
		} catch (Exception e) {

			log(Status.INFO, "Unable to get " + attribute + " from " + ele.toString(), e);
		}
		return result;
	}

	/**
	 * Returns visible text for the Element identified by the <code>By</code>
	 * object. Before attempting that it waits for the page to load completely
	 * 
	 * @param by <code>By</code> identifier for which visible text needs to be
	 *           extracted
	 * @return <code>String</code> visible text extracted for the Element identified
	 *         by the <code>By</code> object
	 */
	protected String getText(By by) {
		String result = "";
		try {
			waitForPageToLoad();
			result = driver.findElement(by).getText();
		} catch (Exception e) {
			log(Status.INFO, "Unable to get text from " + by.toString(), e);
		}
		return result;
	}

	/**
	 * Returns visible text from the Element.
	 * 
	 * @param ele <code>WebElement</code> for which visible text needs to be
	 *            extracted
	 * @return <code>String</code> visible text extracted for the Element identified
	 *         by the <code>By</code> object
	 */
	protected String getText(WebElement ele) {
		String result = "";
		try {
			result = ele.getText();
		} catch (Exception e) {
			log(Status.INFO, "Unable to get text from " + ele.toString(), e);
		}
		return result;
	}

	/**
	 * highlights a given webelement. set enableHighlight in the Configurations to
	 * true in order to work.
	 * 
	 * @param ele Webelement to be highlighted
	 */
	private static WebElement highlight(WebElement ele) {
		if (!Configurations.enableHighlight)
			return ele;
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			try {
				js.executeScript("arguments[0].setAttribute('style', '" + webElementStyle + "');", webElement);
			} catch (Exception e) {
				// do nothing
			}

			String style = ele.getAttribute("style");
			js.executeScript("arguments[0].setAttribute('style', '" + style + " border: 4px solid "
					+ Configurations.highlightColor + " !important;');", ele);
			webElement = ele;
			webElementStyle = style;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ele;
	}

	/**
	 * Mouse hovers over the element identified by the <code>by</code> object.
	 * Returns <code>true</code> if succeeds otherwise <code>false</code>
	 * 
	 * <p>
	 * Before attempting to do that, waits for page to load Explicitly waits for
	 * element to be click-able by default
	 * </p>
	 * 
	 * @param by a <code>By</code> object to identify the element
	 * @return <code>true</code> if it succeeds to type into the field,
	 *         <code>false</code> otherwise
	 */
	protected boolean hover(By by) {
		return hover(by, true, true);
	}

	/**
	 * Mouse hovers over the element identified by the <code>by</code> object.
	 * Returns <code>true</code> if succeeds otherwise <code>false</code>
	 * 
	 * <p>
	 * Before attempting to do that, waits for page to load if
	 * <code>waitForPageLoad</code> is <code>true</code>. Explicitly waits for
	 * element to be click-able if <code> explicitWait </code> is <code>true</code>.
	 * </p>
	 * 
	 * @param by              a <code>By</code> object to identify the element
	 * @param waitForPageLoad a <code>boolean</code> value denoting whether to wait
	 *                        for page to finish loading or not
	 * @param explicitWait    a <code>boolean</code> value denoting whether to wait
	 *                        explicitly for the element to be click-able
	 * @return <code>true</code> if it succeeds to type into the field,
	 *         <code>false</code> otherwise
	 */
	protected boolean hover(By by, boolean waitForPageLoad, boolean explicitWait) {
		WebElement ele;
		try {
			if (waitForPageLoad)
				waitForPageToLoad();
			if (explicitWait) {
				ele = shortWait.until(ExpectedConditions.visibilityOfElementLocated(by));
			} else {
				ele = driver.findElement(by);
			}
			scrollToView(ele);
			highlight(ele);
			act.moveToElement(ele);
			log(Status.INFO, "Successfully hovered over " + by.toString());
		} catch (Exception e) {
			log(Status.INFO, "Unable to hover over " + by.toString(), e);
			return false;
		}
		return true;
	}

	/**
	 * Mouse hovers over <code>WebElement</code>. Returns <code>true</code> if
	 * succeeds otherwise <code>false</code>
	 * 
	 * 
	 * @param ele a <code>WebElement</code> object to identify the element
	 * @return <code>true</code> if it succeeds to type into the field,
	 *         <code>false</code> otherwise
	 */
	protected boolean hover(WebElement ele) {
		try {
			scrollToView(ele);
			highlight(ele);
			act.moveToElement(ele);
			log(Status.INFO, "Successfully hovered over " + ele.toString());
		} catch (Exception e) {
			log(Status.INFO, "Unable to hover over " + ele.toString(), e);
			return false;
		}
		return true;
	}

	/**
	 * Injects java-script into the current browser window. Before attempting to do
	 * that, waits for page to load by default.
	 * 
	 * <p>
	 * It returns whatever returned by the java-script.
	 * </p>
	 * 
	 * @param javaScript        <code>String</code> java-script to be executed
	 * @param waitForPageToLoad <code>boolean</code> value denoting whether to wait
	 *                          for page to finish loading or not
	 * @return <code>Object</code> if returned by the java-script, otherwise
	 *         <code>null</code>
	 */
	protected Object javascript(String javaScript) {
		return javascript(javaScript, true);
	}

	/**
	 * Injects java-script into the current browser window. Before attempting to do
	 * that, waits for page to load if <code>waitForPageLoad</code> is
	 * <code>true</code>.
	 * 
	 * <p>
	 * It returns whatever returned by the java-script.
	 * </p>
	 * 
	 * @param javaScript        <code>String</code> java-script to be executed
	 * @param waitForPageToLoad <code>boolean</code> value denoting whether to wait
	 *                          for page to finish loading or not
	 * @return <code>Object</code> if returned by the java-script, otherwise
	 *         <code>null</code>
	 */
	protected Object javascript(String javaScript, boolean waitForPageToLoad) {
		Object obj = null;
		String title = null;
		try {
			title = driver.getTitle();
			JavascriptExecutor js = (JavascriptExecutor) driver;
			if (waitForPageToLoad)
				waitForPageToLoad();
			obj = js.executeScript(javaScript);
			waitForPageToLoad();
			log(Status.INFO, "Successfully executed JavaScript \"" + javaScript + "\" on window " + title);
		} catch (Exception e) {
			log(Status.INFO, "Failed to execute JavaScript \"" + javaScript + "\" on window " + title);
			return null;
		}
		return obj;
	}

	/**
	 * launches browser specified in the data-sheet
	 * 
	 * @return true if success, false otherwise
	 */
	protected boolean launchBrowser() {
		String browser = dictionary.get("browser");
		if (browser.equalsIgnoreCase("chrome")) {
			WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver(Configurations.chromeOptions);
		} else if (browser.equalsIgnoreCase("firefox")) {
			WebDriverManager.firefoxdriver().setup();
			driver = new FirefoxDriver(Configurations.firefoxOptions);
		} else if (browser.equalsIgnoreCase("ie")) {
			WebDriverManager.iedriver().setup();
			driver = new InternetExplorerDriver(Configurations.ieOptions);
		} else if (browser.equalsIgnoreCase("safari")) {
			driver = new SafariDriver(Configurations.safariOptions);
		} else {
			log(Status.FATAL, "Unimplemented browser");
			return false;
		}
		driver.manage().deleteAllCookies();
		driver.get(Configurations.URL);
		driver.manage().window().maximize();

		log(Status.INFO, "Successfully opened " + browser);
		DriverUtil.act = new Action(driver);
		DriverUtil.shortWait = new WebDriverWait(driver, Configurations.ShortTimeOut);
		DriverUtil.longWait = new WebDriverWait(driver, Configurations.LongTimeOut);
		return true;
	}

	/**
	 * Wrapper for logging statements. Logs both in console and Reporter. However
	 * depends on the Configurations.minimumLogLevel
	 * 
	 * @param status   <code>Status</code>LogStatus can be PASS, FAIL, WARNING etc
	 * @param stepName <code>String</code>short description of the step
	 */
	protected static void log(Status status, String stepName) {
		log(status, stepName, null);
	}

	/**
	 * Wrapper for logging statements. Logs both in console and Reporter However
	 * depends on the <code>Configurations.minimumLogLevel<code>
	 * 
	 * @param status   <code>Status</code>LogStatus can be PASS, FAIL, WARNING etc
	 * 
	 * @param stepName <code>String</code>short description of the step
	 * @param e        <code>Throwable</code>If any exception occurred during the
	 *                 step
	 */
	protected static void log(Status status, String stepName, Throwable e) {
		switch (Configurations.minimumLogLevel) {
		case ERROR:
			if (!(status == Status.ERROR || status == Status.FAIL || status == Status.FATAL))
				return;
			break;
		case FAIL:
			if (!(status == Status.FAIL || status == Status.FATAL))
				return;
			break;
		case FATAL:
			if (!(status == Status.FATAL))
				return;
			break;
		case INFO:
			if (status == Status.DEBUG)
				return;
			break;
		case PASS:
			if (status == Status.DEBUG || status == Status.INFO)
				return;
			break;
		case SKIP:
			if (status == Status.DEBUG || status == Status.INFO || status == Status.PASS)
				return;
			break;
		case WARNING:
			if (status == Status.DEBUG || status == Status.INFO || status == Status.PASS || status == Status.SKIP)
				return;
			break;
		default:
			break;
		}
		String reportTrace = "", consoleTrace = "";
		StackTraceElement[] traces = Thread.currentThread().getStackTrace();
		for (StackTraceElement trace : traces) {
			if (!(trace.getClassName().equalsIgnoreCase(DriverUtil.class.getName())
					|| trace.getClassName().equalsIgnoreCase(Thread.class.getName()))) {
				consoleTrace = trace.toString() + ": " + stepName;
				reportTrace = trace.getClassName() + ":" + trace.getLineNumber() + ": " + stepName;
				break;
			}
		}
		if (e != null) {
			e.printStackTrace();
			reporter.reportEvent(status, reportTrace, ExceptionUtils.getFullStackTrace(e));
			org.testng.Reporter.log(status.toString().toUpperCase() + ": " + reportTrace + "<br><pre>"
					+ ExceptionUtils.getFullStackTrace(e) + "</pre></br>");
		} else {
			reporter.reportEvent(status, reportTrace);
			org.testng.Reporter.log(status.toString().toUpperCase() + ": " + reportTrace);
		}
		System.out.println(status.toString().toUpperCase() + ": " + consoleTrace);
	}

	/**
	 * Navigates to certain URL
	 * 
	 * @param string <code>String</code> target URL
	 * @return <code>true</code> or <code>false</code> depending on whether
	 *         navigation is successful or not
	 */
	protected boolean navigateTo(String string) {
		try {
			driver.navigate().to(string);
			waitForPageToLoad();
			log(Status.INFO, "Successfully Navigated to " + string);
		} catch (Exception e) {
			log(Status.INFO, "Unable to navigate to " + string, e);
			return false;
		}
		return true;
	}

	/**
	 * Puts the value at target element. If it already has a value the new value
	 * replaces the old value
	 * 
	 * @param key   keyword for the value
	 * @param value value that needs to be put
	 */
	protected void put(String key, String value) {
		dictionary.put(key, value);
	}

	/**
	 * brings the webelement to view. more precisely to the middle of the webPage.
	 * Useful for pages with sticky header, footer, left or right pane etc.
	 * 
	 * set Configurations.enableScrollToView to true
	 * 
	 * @param webElement
	 */
	private static void scrollToView(WebElement webElement) {
		if (!Configurations.enableScrollToView)
			return;
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("var zx = arguments[0].getBoundingClientRect();"
					+ "scrollBy((zx.left+zx.right)/2-window.innerWidth/2, (zx.top+zx.bottom)/2-window.innerHeight/2);",
					webElement);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Selects a random option from the drop-down identified by the provided
	 * <code>By</code> object. If it succeeds to select returns <code>true</code>,
	 * <code>false</code> otherwise.
	 * 
	 * <p>
	 * Before attempting to do that, waits for page to load and Explicitly waits for
	 * element to be click-able.
	 * </p>
	 * 
	 * @param ele a <code>By</code> object to identify the drop-down
	 * @return <code>true</code> if it succeeds to click, <code>false</code>
	 *         otherwise
	 */
	protected boolean select(By by) {
		return select(by, true, true);
	}

	/**
	 * Selects a random option from the drop-down identified by the provided
	 * <code>By</code> object. If it succeeds to select returns <code>true</code>,
	 * <code>false</code> otherwise.
	 * 
	 * <p>
	 * Before attempting to do that, waits for page to load if
	 * <code>waitForPageLoad</code> is <code>true</code>. Explicitly waits for
	 * element to be click-able if <code> explicitWait </code> is <code>true</code>.
	 * </p>
	 * 
	 * @param by              an <code>By</code> object to identify the drop-down
	 * @param waitForPageLoad a <code>boolean</code> value denoting whether to wait
	 *                        for page to finish loading or not
	 * @param explicitWait    a <code>boolean</code> value denoting whether to wait
	 *                        explicitly for the element to be click-able
	 * @return <code>true</code> if it succeeds to click, <code>false</code>
	 *         otherwise
	 */
	protected boolean select(By by, boolean waitForPageLoad, boolean explicitWait) {
		try {
			if (waitForPageLoad)
				waitForPageToLoad();
			WebElement ele;
			if (explicitWait) {
				ele = shortWait.until(ExpectedConditions.elementToBeClickable(by));
			} else {
				ele = driver.findElement(by);
			}
			scrollToView(ele);
			highlight(ele);
			Select select = new Select(ele);
			int maxSize = ele.findElements(By.tagName("option")).size();
			String text = ele.findElements(By.tagName("option")).get(new Random().nextInt(maxSize - 1) + 1).getText();
			select.selectByVisibleText(text);
			log(Status.INFO, "Successfully selected " + text + " from " + by.toString());
		} catch (Exception e) {
			log(Status.INFO, "Unable to select from " + by.toString(), e);
			return false;
		}
		return true;
	}

	/**
	 * Selects a random option from the drop-down identified by the provided
	 * <code>WebElement</code> object. If it succeeds to select returns
	 * <code>true</code>, <code>false</code> otherwise.
	 * 
	 * @param ele a <code>WebElement</code> object to identify the drop-down
	 * @return <code>true</code> if it succeeds to click, <code>false</code>
	 *         otherwise
	 */
	protected boolean select(WebElement ele) {
		try {
			scrollToView(ele);
			highlight(ele);
			Select select = new Select(ele);
			int maxSize = ele.findElements(By.tagName("option")).size();
			String text = ele.findElements(By.tagName("option")).get(new Random().nextInt(maxSize - 1) + 1).getText();
			select.selectByVisibleText(text);
			log(Status.INFO, "Successfully selected " + text + " from " + ele.toString());
		} catch (Exception e) {
			log(Status.INFO, "Unable to select from " + ele.toString(), e);
			return false;
		}
		return true;
	}

	/**
	 * Selects the specified <code>index</code>th option(zero indexed) from the
	 * drop-down identified by the provided <code>By</code> object. If it succeeds
	 * to select returns <code>true</code>, <code>false</code> otherwise.
	 * 
	 * <p>
	 * Before attempting to find the element, waits for page to load and explicitly
	 * waits for element to be click-able by default.
	 * </p>
	 * 
	 * @param by    an <code>By</code> object to identify the drop-down
	 * @param index <code>Integer</code> index of the option to be selected
	 * @return <code>true</code> if it succeeds to click, <code>false</code>
	 *         otherwise
	 */
	protected boolean selectByIndex(By by, int index) {
		return selectByIndex(by, index, true, true);
	}

	/**
	 * Selects the specified <code>text</code> from the dropdown identified by the
	 * provided <code>By</code> object. If it succeeds to select returns
	 * <code>true</code>, <code>false</code> otherwise.
	 * 
	 * <p>
	 * Before attempting to do that, waits for page to load if
	 * <code>waitForPageLoad</code> is <code>true</code>. Explicitly waits for
	 * element to be click-able if <code> explicitWait </code> is <code>true</code>.
	 * </p>
	 * 
	 * @param by              an <code>By</code> object to identify the dropdown
	 * @param index           <code>Integer</code> index of the option to be
	 *                        selected
	 * @param waitForPageLoad a <code>boolean</code> value denoting whether to wait
	 *                        for page to finish loading or not
	 * @param explicitWait    a <code>boolean</code> value denoting whether to wait
	 *                        explicitly for the element to be click-able
	 * @return <code>true</code> if it succeeds to click, <code>false</code>
	 *         otherwise
	 */
	protected boolean selectByIndex(By by, int index, boolean waitForPageLoad, boolean explicitWait) {
		try {
			if (waitForPageLoad)
				waitForPageToLoad();
			WebElement ele;
			if (explicitWait) {
				ele = shortWait.until(ExpectedConditions.elementToBeClickable(by));
			} else {
				ele = driver.findElement(by);
			}
			scrollToView(ele);
			highlight(ele);
			Select select = new Select(ele);
			String text = ele.findElements(By.tagName("option")).get(index).getText();
//			select.selectByIndex(index);
			select.selectByVisibleText(text);
			log(Status.INFO, "Successfully selected " + text + " from " + by.toString());
		} catch (Exception e) {
			log(Status.INFO, "Unable to select " + Integer.toString(index) + " entry from " + by.toString(), e);
			return false;
		}
		return true;
	}

	/**
	 * Selects the specified <code>index</code>th option(zero indexed) from the
	 * drop-down specified by the provided <code>WebElement</code> object. If it
	 * succeeds to select returns <code>true</code>, <code>false</code> otherwise.
	 * 
	 * @param ele   an <code>WebElement</code> object for the dropdown
	 * @param index <code>Integer</code> index of the option to be selected
	 * @return <code>true</code> if it succeeds to click, <code>false</code>
	 *         otherwise
	 */
	protected boolean selectByIndex(WebElement ele, int index) {
		try {
			scrollToView(ele);
			highlight(ele);
			Select select = new Select(ele);
			String text = ele.findElements(By.tagName("option")).get(index).getText();
			select.selectByVisibleText(text);
			log(Status.INFO, "Successfully selected " + text + " from " + ele.toString());
		} catch (Exception e) {
			log(Status.INFO, "Unable to select " + Integer.toString(index) + " entry from " + ele.toString(), e);
			return false;
		}
		return true;
	}

	/**
	 * Selects the specified <code>text</code> from the dropdown identified by the
	 * provided <code>By</code> object by using Selenium
	 * <code>Select.selectByValue(String text)</code>. If it succeeds to select
	 * returns <code>true</code>, <code>false</code> otherwise.
	 * 
	 * <p>
	 * Before attempting to find the element, waits for page to load and explicitly
	 * waits for element to be click-able by default.
	 * </p>
	 * 
	 * @param by   an <code>By</code> object to identify the dropdown
	 * @param text <code>String</code> to be selected from the dropdown (must match
	 *             to the value of the option)
	 * @return <code>true</code> if it succeeds to click, <code>false</code>
	 *         otherwise
	 */
	protected boolean selectByValue(By by, String text) {
		return selectByValue(by, text, true, true);
	}

	/**
	 * Selects the specified <code>text</code> from the drop-down identified by the
	 * provided <code>By</code> object by using Selenium
	 * <code>Select.selectByValue(String text)</code>. If it succeeds to select
	 * returns <code>true</code>, <code>false</code> otherwise.
	 * 
	 * <p>
	 * Before attempting to do that, waits for page to load if
	 * <code>waitForPageLoad</code> is <code>true</code>. Explicitly waits for
	 * element to be click-able if <code> explicitWait </code> is <code>true</code>.
	 * </p>
	 * 
	 * @param by              an <code>By</code> object to identify the dropdown
	 * @param text            <code>String</code> to be selected from the dropdown
	 *                        (must match to the value of the option)
	 * @param waitForPageLoad a <code>boolean</code> value denoting whether to wait
	 *                        for page to finish loading or not
	 * @param explicitWait    a <code>boolean</code> value denoting whether to wait
	 *                        explicitly for the element to be click-able
	 * @return <code>true</code> if it succeeds to click, <code>false</code>
	 *         otherwise
	 */
	protected boolean selectByValue(By by, String text, boolean waitForPageLoad, boolean explicitWait) {
		try {
			WebElement ele;
			if (waitForPageLoad)
				waitForPageToLoad();
			if (explicitWait) {
				ele = shortWait.until(ExpectedConditions.elementToBeClickable(by));
			} else {
				ele = driver.findElement(by);
			}
			scrollToView(ele);
			highlight(ele);
			Select select = new Select(ele);
			select.selectByValue(text);
			log(Status.INFO, "Successfully selected " + text + " from " + by.toString());
		} catch (Exception e) {
			log(Status.INFO, "Unable to select " + text + " from " + by.toString(), e);
			return false;
		}
		return true;
	}

	/**
	 * Selects the specified <code>text</code> from the drop-down
	 * <code>WebElement</code> object by using Selenium
	 * <code>Select.selectByValue(String text)</code>. If it succeeds to select
	 * returns <code>true</code>, <code>false</code> otherwise.
	 * 
	 * @param ele  a <code>WebElement</code> object for the drop-down
	 * @param text <code>String</code> to be selected from the drop-down (must match
	 *             to the value of the option)
	 * @return <code>true</code> if it succeeds to click, <code>false</code>
	 *         otherwise
	 */
	protected boolean selectByValue(WebElement ele, String text) {
		try {
			scrollToView(ele);
			highlight(ele);
			Select select = new Select(ele);
			select.selectByValue(text);
			log(Status.INFO, "Successfully selected " + text + " from " + ele.toString());
		} catch (Exception e) {
			log(Status.INFO, "Unable to select " + text + " from " + ele.toString(), e);
			return false;
		}
		return true;
	}

	/**
	 * Selects the specified <code>text</code> from the drop-down identified by the
	 * provided <code>By</code> object by using Selenium
	 * <code>Select.selectByVisibleText(String text)</code>. If it succeeds to
	 * select returns <code>true</code>, <code>false</code> otherwise.
	 * 
	 * <p>
	 * Before attempting to find the element, waits for page to load and explicitly
	 * waits for element to be click-able by default.
	 * </p>
	 * 
	 * @param by   an <code>By</code> object to identify the drop-down
	 * @param text <code>String</code> to be selected from the drop-down (must match
	 *             to the visible text of the option)
	 * @return <code>true</code> if it succeeds to click, <code>false</code>
	 *         otherwise
	 */
	protected boolean selectByVisibleText(By by, String text) {
		return selectByVisibleText(by, text, true, true);
	}

	/**
	 * Selects the specified <code>text</code> from the dropdown identified by the
	 * provided <code>By</code> object by using Selenium
	 * <code>Select.selectByVisibleText(String text)</code>. If it succeeds to
	 * select returns <code>true</code>, <code>false</code> otherwise.
	 * 
	 * <p>
	 * Before attempting to do that, waits for page to load if
	 * <code>waitForPageLoad</code> is <code>true</code>. Explicitly waits for
	 * element to be click-able if <code> explicitWait </code> is <code>true</code>.
	 * </p>
	 * 
	 * @param by              an <code>By</code> object to identify the dropdown
	 * @param text            <code>String</code> to be selected from the dropdown
	 *                        (must match to the visible text of the option)
	 * @param waitForPageLoad a <code>boolean</code> value denoting whether to wait
	 *                        for page to finish loading or not
	 * @param explicitWait    a <code>boolean</code> value denoting whether to wait
	 *                        explicitly for the element to be click-able
	 * @return <code>true</code> if it succeeds to click, <code>false</code>
	 *         otherwise
	 */
	protected boolean selectByVisibleText(By by, String text, boolean waitForPageLoad, boolean explicitWait) {
		try {
			WebElement ele;
			if (waitForPageLoad)
				waitForPageToLoad();
			if (explicitWait) {
				ele = shortWait.until(ExpectedConditions.elementToBeClickable(by));
			} else {
				ele = driver.findElement(by);
			}
			scrollToView(ele);
			highlight(ele);
			Select select = new Select(ele);
			select.selectByVisibleText(text);
			log(Status.INFO, "Successfully selected " + text + " from " + by.toString());
		} catch (Exception e) {
			log(Status.INFO, "Unable to select " + text + " from " + by.toString(), e);
			return false;
		}
		return true;
	}

	/**
	 * Selects the specified <code>text</code> from the drop-down
	 * <code>WebElement</code> object by using Selenium
	 * <code>Select.selectByVisibleText(String text)</code>. If it succeeds to
	 * select returns <code>true</code>, <code>false</code> otherwise.
	 * 
	 * 
	 * @param ele  an <code>WebElement</code> object for the drop-down
	 * @param text <code>String</code> to be selected from the drop-down (must match
	 *             to the visible text of the option)
	 * @return <code>true</code> if it succeeds to click, <code>false</code>
	 *         otherwise
	 */
	protected boolean selectByVisibleText(WebElement ele, String text) {
		try {
			scrollToView(ele);
			highlight(ele);
			Select select = new Select(ele);
			select.selectByVisibleText(text);
			log(Status.INFO, "Successfully selected " + text + " from " + ele.toString());
		} catch (Exception e) {
			log(Status.INFO, "Unable to select " + text + " from " + ele.toString(), e);
			return false;
		}
		return true;
	}

	/**
	 * Application specific drop-down selection methods. Can be used for combo box
	 * or any other cases where standard methods does not work. May need to be
	 * revised/Overridden depending on the application
	 * 
	 * @param by
	 * @param index
	 * @return
	 */
	protected boolean selectComboByIndex(By by, int index) {
		return selectComboByIndex(by, index, true, true);
	}

	/**
	 * Application specific dropdown selection methods. Can be used for combo box or
	 * any other cases where standard methods does not work. May need to be
	 * revised/Overridden depending on the application
	 * 
	 * @param by
	 * @param index
	 * @param waitForPageLoad
	 * @param explicitWait
	 * @return
	 */
	protected boolean selectComboByIndex(By by, int index, boolean waitForPageLoad, boolean explicitWait) {
		try {
			By Options = By.xpath(null);// Identifier to capture all the option elements;
			return click(by, waitForPageLoad, explicitWait) && waitForPageLoad && click(getAll(Options).get(index));
		} catch (Exception e) {
			log(Status.INFO, "Unable to select " + Integer.toString(index) + " entry from " + by.toString(), e);
			return false;
		}
	}

	/**
	 * Application specific drop-down selection methods. Can be used for combo box
	 * or any other cases where standard methods does not work. May need to be
	 * revised/Overridden depending on the application
	 * 
	 * @param by
	 * @param index
	 * @param waitForPageToLoad
	 * @param explicitWait
	 * @return
	 */
	protected boolean selectComboByIndex(WebElement ele, int index) {
		try {
			By Options = By.xpath(null);// Identifier to capture all the option elements;
			return click(ele) && waitForPageToLoad() && click(getAll(Options).get(index));
		} catch (Exception e) {
			log(Status.INFO, "Unable to select " + Integer.toString(index) + " entry from " + ele.toString(), e);
			return false;
		}

	}

	/**
	 * Application specific dropdown selection methods. Can be used for combo box or
	 * any other cases where standard methods does not work. May need to be
	 * revised/Overridden depending on the application
	 * 
	 * @param by   identifier for target dropdown
	 * @param text String that should be part of text of the target option
	 * @return boolean value depending on successful or not
	 */
	protected boolean selectComboByVisibleText(By by, String text) {
		return selectComboByVisibleText(by, text, true, true, false);
	}

	/**
	 * Application specific dropdown selection methods. Can be used for combo box or
	 * any other cases where standard methods does not work. May need to be
	 * revised/Overridden depending on the application
	 * 
	 * @param by              identifier for target dropdown
	 * @param text            String that should be part/entire text of the target
	 *                        option
	 * @param explicitWait    flag to determine explicitly wait or not
	 * @param waitForPageLoad flag to determine wait for page to load or not
	 * @param exactMatch      flag to determine whether to match entire or partial
	 *                        text
	 * @return boolean value depending on successful or not
	 */
	protected boolean selectComboByVisibleText(By by, String text, boolean waitForPageLoad, boolean explicitWait,
			boolean exactMatch) {
		try {
			By xPathOfTargetOption;
			if (exactMatch)
				xPathOfTargetOption = By.xpath("//div[text()='" + text + "']");
			else
				xPathOfTargetOption = By.xpath("//div[contains(text(), '" + text + "')]");

			return click(by, waitForPageLoad, explicitWait) && waitForPageLoad
					&& click(xPathOfTargetOption, waitForPageLoad, explicitWait);
		} catch (Exception e) {
			log(Status.INFO, "Unable to select " + text + " from " + by.toString(), e);
			return false;
		}
	}

	/**
	 * Application specific dropdown selection methods. Can be used for combo box or
	 * any other cases where standard methods does not work. May need to be
	 * revised/Overridden depending on the application
	 * 
	 * @param element target element
	 * @param text    String that should be partial match of the target option
	 * @return boolean value depending on successful or not
	 */
	protected boolean selectComboByVisibleText(WebElement element, String text) {
		return selectComboByVisibleText(element, text, false);
	}

	/**
	 * Application specific dropdown selection methods. Can be used for combo box or
	 * any other cases where standard methods does not work. May need to be
	 * revised/Overridden depending on the application
	 * 
	 * @param element    target element
	 * @param text       String that should be part/entire text of the target option
	 * @param exactMatch flag to determine whether to match entire or partial text
	 * @return boolean value depending on successful or not
	 */
	protected boolean selectComboByVisibleText(WebElement element, String text, boolean exactMatch) {
		try {
			By xPathOfTargetOption;
			if (exactMatch)
				xPathOfTargetOption = By.xpath("//div[text()='" + text + "']");
			else
				xPathOfTargetOption = By.xpath("//div[contains(text(), '" + text + "')]");

			return click(element) && waitForPageToLoad() && click(xPathOfTargetOption);
		} catch (Exception e) {
			log(Status.INFO, "Unable to select " + text + " from " + element.toString(), e);
			return false;
		}
	}

	/**
	 * Types random text (minimum length 3, maximum length 10) into the input field
	 * identified by the provided <code>By</code> object by using Selenium
	 * <code>WebElement.sendKeys(String text)</code>. If it succeeds to type returns
	 * <code>true</code>, <code>false</code> otherwise.
	 * 
	 * <p>
	 * Before attempting to find the element, waits for page to load and explicitly
	 * waits for element to be click-able by default.
	 * </p>
	 * 
	 * @param by an <code>By</code> object to identify the input field
	 * @return <code>true</code> if it succeeds to type into the field,
	 *         <code>false</code> otherwise
	 */
	protected boolean sendkeys(By by) {
		return sendkeys(by, true, true);
	}

	/**
	 * Types random text (minimum length 3, maximum length 10) into the input field
	 * identified by the provided <code>By</code> object by using Selenium
	 * <code>WebElement.sendKeys(String text)</code>. If it succeeds to type returns
	 * <code>true</code>, <code>false</code> otherwise.
	 * 
	 * <p>
	 * Before attempting to do that, waits for page to load if
	 * <code>waitForPageLoad</code> is <code>true</code>. Explicitly waits for
	 * element to be click-able if <code> explicitWait </code> is <code>true</code>.
	 * </p>
	 * 
	 * @param by              an <code>By</code> object to identify the input field
	 * @param waitForPageLoad a <code>boolean</code> value denoting whether to wait
	 *                        for page to finish loading or not
	 * @param explicitWait    a <code>boolean</code> value denoting whether to wait
	 *                        explicitly for the element to be click-able
	 * @return <code>true</code> if it succeeds to type into the field,
	 *         <code>false</code> otherwise
	 */
	protected boolean sendkeys(By by, boolean waitForPageLoad, boolean explicitWait) {
		int length = 3 + new Random().nextInt(8);
		return sendkeys(by, generateRandomString(length), waitForPageLoad, explicitWait);
	}

	/**
	 * Types the specified <code>text</code> into the input field identified by the
	 * provided <code>By</code> object by using Selenium
	 * <code>WebElement.sendKeys(String text)</code>. If it succeeds to type returns
	 * <code>true</code>, <code>false</code> otherwise.
	 * 
	 * <p>
	 * Before attempting to find the element, waits for page to load and explicitly
	 * waits for element to be click-able by default.
	 * </p>
	 * 
	 * @param by   an <code>By</code> object to identify the input field
	 * @param text <code>String</code> to be entered into the field
	 * @return <code>true</code> if it succeeds to type into the field,
	 *         <code>false</code> otherwise
	 */
	protected boolean sendkeys(By by, String text) {
		return sendkeys(by, text, true, true);
	}

	/**
	 * Types the specified <code>text</code> into the input field identified by the
	 * provided <code>By</code> object by using Selenium
	 * <code>WebElement.sendKeys(String text)</code>. If it succeeds to type returns
	 * <code>true</code>, <code>false</code> otherwise.
	 * 
	 * <p>
	 * Before attempting to do that, waits for page to load if
	 * <code>waitForPageLoad</code> is <code>true</code>. Explicitly waits for
	 * element to be click-able if <code> explicitWait </code> is <code>true</code>.
	 * </p>
	 * 
	 * @param by              an <code>By</code> object to identify the input field
	 * @param text            <code>String</code> to be entered into the field
	 * @param waitForPageLoad a <code>boolean</code> value denoting whether to wait
	 *                        for page to finish loading or not
	 * @param explicitWait    a <code>boolean</code> value denoting whether to wait
	 *                        explicitly for the element to be click-able
	 * @return <code>true</code> if it succeeds to type into the field,
	 *         <code>false</code> otherwise
	 */
	protected boolean sendkeys(By by, String text, boolean waitForPageLoad, boolean explicitWait) {

		try {
			if (waitForPageLoad)
				waitForPageToLoad();
			WebElement ele;
			if (explicitWait) {
				ele = shortWait.until(ExpectedConditions.elementToBeClickable(by));
			} else {
				ele = driver.findElement(by);
			}
			scrollToView(ele);
			highlight(ele);
			ele.clear();
			ele.sendKeys(text + Keys.TAB);
			log(Status.INFO, "Successfully filled " + by.toString() + " by " + text);
		} catch (Exception e) {
			log(Status.INFO, "Unable to fill " + by.toString() + " by " + text, e);
			return false;
		}
		return true;
	}

	/**
	 * Types random text (minimum length 3, maximum length 10) into the input field
	 * identified by the provided <code>WebElement</code> object by using Selenium
	 * <code>WebElement.sendKeys(String text)</code>. If it succeeds to type returns
	 * <code>true</code>, <code>false</code> otherwise.
	 * 
	 * 
	 * @param ele an <code>WebElement</code> object to identify the input field
	 * @return <code>true</code> if it succeeds to type into the field,
	 *         <code>false</code> otherwise
	 */
	protected boolean sendkeys(WebElement ele) {
		int length = 3 + new Random().nextInt(8);
		return sendkeys(ele, generateRandomString(length));
	}

	/**
	 * Types the specified <code>text</code> into the input field <code>WebElement
	 * </code> object by using Selenium
	 * <code>WebElement.sendKeys(String text)</code>. If it succeeds to type returns
	 * <code>true</code>, <code>false</code> otherwise.
	 * 
	 * @param ele  an <code>WebElement</code> object to identify the input field
	 * @param text <code>String</code> to be entered into the field
	 * @return <code>true</code> if it succeeds to type into the field,
	 *         <code>false</code> otherwise
	 */
	protected boolean sendkeys(WebElement ele, String text) {

		try {
			scrollToView(ele);
			highlight(ele);
			ele.clear();
			ele.sendKeys(text + Keys.TAB);
			log(Status.INFO, "Successfully filled " + ele.toString() + " by " + text);
		} catch (Exception e) {
			log(Status.INFO, "Unable to fill " + ele.toString() + " by " + text, e);
			return false;
		}
		return true;
	}

	/**
	 * starts Desktop Automation Server and creates driver to control/communicate
	 * with desktop application
	 * 
	 * @return <code>true</code> if successful, <code>false</code> otherwise
	 */
	static boolean startWiniumServer() {
		try {
			DesktopOptions options = new DesktopOptions();
			options.setDebugConnectToRunningApp(true);
			wDriver = new WiniumDriver(new URL("http://localhost:9999"), options);
			log(Status.INFO, "Successfully started desktop automation server");
		} catch (WebDriverException e) {
			try {
				Runtime.getRuntime().exec("./src/main/resources/Winium.Desktop.Driver.exe");
				Thread.sleep(2000);
				DesktopOptions options = new DesktopOptions();
				options.setDebugConnectToRunningApp(true);
				wDriver = new WiniumDriver(new URL("http://localhost:9999"), options);
				log(Status.INFO, "Successfully started desktop automation server");
			} catch (MalformedURLException e1) {
				e.printStackTrace();
				log(Status.INFO, "Failed to start desktop automation server", e1);
				return false;
			} catch (IOException e1) {
				e1.printStackTrace();
				log(Status.INFO, "Failed to start desktop automation server", e1);
				return false;
			} catch (InterruptedException e1) {
				e1.printStackTrace();
				log(Status.INFO, "Failed to start desktop automation server", e1);
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			log(Status.INFO, "Failed to start desktop automation server", e);
			return false;
		}
		return true;
	}

	protected boolean stopWiniumServer() {
		try {
			wDriver.quit();
			wDriver = null;
			wElement = null;
			Runtime.getRuntime().exec("taskkill /F /IM Winium.Desktop.Driver.exe");
			log(Status.INFO, "Successfully stopped desktop Automation Server");
		} catch (Exception e) {
			e.printStackTrace();
			log(Status.INFO, "Unable to stop desktop Automation Server", e);
			return false;
		}
		return true;
	}

	/**
	 * Switches to DOM. Returns <code>true</code> or <code>false</code> depending on
	 * whether successful to switch to root DOM or not.
	 * 
	 * @return <code>true</code> or <code>false</code> depending on whether
	 *         successful to switch to Frame or not.
	 */
	protected boolean switchToDefaultContent() {
		try {
			driver.switchTo().defaultContent();
			driver.manage().window().maximize();
		} catch (Exception e) {
			log(Status.INFO, "Unable to switch to default content", e);
			return false;
		}

		log(Status.INFO, "Successfully switched to default content");
		return true;
	}

	/**
	 * Switches to first iFrame available in the DOM. Returns <code>true</code> or
	 * <code>false</code> depending on whether successful to switch to Frame or not.
	 * 
	 * <p>
	 * Waits for the driver window to check page loading is completed as well as
	 * waits for Frame element to appear on DOM if not found immediately for a
	 * maximum PageLoadTimeOut
	 * </P>
	 * 
	 * @return <code>true</code> or <code>false</code> depending on whether
	 *         successful to switch to Frame or not.
	 */
	protected boolean switchToFrame() {
		return switchToFrame(By.tagName("iframe"));
	}

	/**
	 * Switches to Frame identified by the provided
	 * <code>By<code> object. Returns <code>true</code> or <code>false</code>
	 * depending on whether successful to switch to Frame or not.
	 * 
	 * <p>
	 * Waits for the driver window to check page loading is completed as well as
	 * waits for Frame element to appear on DOM if not found immediately for a
	 * maximum PageLoadTimeOut
	 * </P>
	 * 
	 * @param by <code>By</code> object identifying the Frame
	 * @return <code>true</code> or <code>false</code> depending on whether
	 *         successful to switch to Frame or not.
	 */

	protected boolean switchToFrame(By by) {
		try {
			waitForPageToLoad();
			waitFor(by);
			driver.switchTo().frame(get(by, true, false));
			driver.manage().window().maximize();
		} catch (Exception e) {
			log(Status.INFO, "Unable to switch to Frame" + by.toString(), e);
			return false;
		}

		log(Status.INFO, "Successfully switched to Frame" + by.toString());

		return true;
	}

	/**
	 * Switches to window with a match in <code>driver.getTitle()</code> or
	 * <code>driver.getCurrentUrl(). Partial URL or Title is passed as <code>String</code>
	 * to be compared. Returns <code>true</code> or <code>false</code> depending on
	 * whether successful to switch to window or not.
	 * 
	 * <p>
	 * Waits for the driver window to check page loading is completed or not before
	 * comparing the Title/URL with the arguments by default.
	 * </P>
	 * 
	 * @param TitleOrURL
	 * @return <code>true</code> or <code>false</code> depending on whether
	 *         successful to switch to window or not.
	 */
	protected boolean switchToWindow(String TitleOrURL) {
		return switchToWindow(TitleOrURL, true);
	}

	/**
	 * Switches to window with a match in <code>driver.getTitle()</code> or
	 * <code>driver.getCurrentUrl(). Partial URL or Title is passed as <code>String</code>
	 * to be compared. Returns <code>true</code> or <code>false</code> depending on
	 * whether successful to switch to window or not.
	 * 
	 * <p>
	 * Waits for the driver window to check page loading is completed or not before
	 * comparing the Title/URL with the arguments if <code>wait</code> is provided
	 * as <code>true</code>
	 * </P>
	 * 
	 * @param TitleOrURL <code>String</code> part of Title or URL of the Target
	 *                   Window
	 * @param wait       <code>Boolean</code> value denoting whether to wait for
	 *                   page to finish loading or not
	 * @return <code>true</code> or <code>false</code> depending on whether
	 *         successful to switch to window or not.
	 */
	protected boolean switchToWindow(String TitleOrURL, boolean wait) {
		boolean success = false;
		Set<String> windows = driver.getWindowHandles();
		for (String string : windows) {
			driver.switchTo().window(string);
			if (wait)
				waitForPageToLoad();
			if (driver.getTitle().contains(TitleOrURL) || driver.getCurrentUrl().contains(TitleOrURL)) {
				success = true;
				break;
			}
		}

		if (success) {
			log(Status.INFO, "Successfully switched to " + TitleOrURL);
			driver.manage().window().maximize();
			return true;
		} else {
			log(Status.INFO, "Unable to switch to " + TitleOrURL);
			return false;
		}
	}

	/**
	 * Switches the control of windows execution to a desktop windows matched by the
	 * <code>String</code> attributeValue and <code>String</code> attributeName.
	 * <p>
	 * A complete match with attributeValue is required and only immediate windows
	 * available on desktop are compared
	 * </p>
	 * 
	 * 
	 * 
	 * @param attributeValue <code>String</code> Value of the uniquely identifying
	 *                       attribute that identifies the window
	 * @param attributeName  <code>String</code> Name of the unique identifier
	 * @return <code>true<code> if a match is found and is able to switch to the window, <code>false</code>
	 *         otherwise.
	 */
	protected boolean switchToWindow(String attributeValue, String attributeName) {
		return switchToWindow(attributeValue, attributeName, false, false);
	}

	/**
	 * Switches the control of windows execution to a desktop windows matched by the
	 * <code>String</code> attributeValue and <code>String</code> attributeName.
	 * <p>
	 * If <code>partialMatch<code> is <code>true</code> values are compared by using
	 * <code>contains()</code> method, otherwise complete match is required. If
	 * <code>searchEntireTree</code> is <code>true</code> entire tree starting from
	 * desktop is checked; otherwise only immediate windows available on desktop are
	 * compared
	 * </p>
	 * 
	 * 
	 * 
	 * @param attributeValue   <code>String</code> Value of the uniquely identifying
	 *                         attribute that identifies the window
	 * @param attributeName    <code>String</code> Name of the unique identifier
	 * @param partialMatch     <code>boolean</code> if <code>true</code> compares
	 *                         the value of the unique identifier using
	 *                         <code>contains()</code> method
	 * @param searchEntireTree <code>boolean<code> if <code>false<code> searches
	 *                         only in the immediate windows available on desktop.
	 *                         Otherwise looks for all possible nodes until match
	 *                         found
	 * @return <code>true<code> if a match is found and is able to switch to the window, <code>false</code>
	 *         otherwise.
	 */
	protected boolean switchToWindow(String attributeValue, String attributeName, boolean partialMatch,
			boolean searchEntireTree) {
		try {
			String xPath = "";
			if (partialMatch) {
				if (searchEntireTree)
					xPath = "//*[contains(@" + attributeName + ", '" + attributeValue + "')]";
				else
					xPath = "/*[contains(@" + attributeName + ", '" + attributeValue + "')]";
			} else {
				if (searchEntireTree)
					xPath = "//*[@" + attributeName + " = '" + attributeValue + "']";
				else
					xPath = "/*[@" + attributeName + " = '" + attributeValue + "']";
			}
			if (wDriver == null)
				startWiniumServer();
			wElement = wDriver.findElement(By.xpath(xPath));
			bringToFront(wElement);
			log(Status.INFO, "Succesfully switched to window with " + attributeName + " = " + attributeValue);
		} catch (Exception e) {
			e.printStackTrace();
			log(Status.INFO, "Unable to switch to window with " + attributeName + " = " + attributeValue, e);
			return false;
		}
		return true;
	}

	/**
	 * Waits for the element identified by the given <code>By</code> object to be
	 * available in DOM. Returns <code>true</code> if the element is found,
	 * <code>false</code> otherwise.
	 * 
	 * <p>
	 * Should be used where Explicit Wait is not working. Maximum timeout can be
	 * configured by changing <code>Configurations.PageLoadTimeOut</code> in
	 * seconds.
	 * </p>
	 * 
	 * @param by
	 * @return
	 */
	protected boolean waitFor(By by) {
		int counter = 0;
		int TargetCounter = Configurations.PageLoadTimeOut * 10;
		while (true) {
			try {
				driver.findElement(by);
				log(Status.INFO, "Successfully identified " + by.toString());
				return true;
			} catch (Exception e) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e1) {
				}
				counter++;
				if (counter == TargetCounter) {
					log(Status.INFO, "Waited for " + Configurations.PageLoadTimeOut + " Seconds but " + by.toString()
							+ " is not located yet", e);
					return false;
				}
			}
		}
	}

	/**
	 * Returns <code>true</code> immediately after the page load completes within
	 * the page Load time out specified by
	 * {@link settings.Configurations#PageLoadTimeOut PageLoadTimeOut} in seconds.
	 * If the page load is not complete after the stipulated time returns false.
	 * 
	 * @return <code>true</code> or <code>false</code> depending on whether the page
	 *         load is complete or not after the Page Load Time Out
	 */
	protected static boolean waitForPageToLoad() {
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			NgWebDriver ajsDriver = new NgWebDriver(js);
			ajsDriver.waitForAngularRequestsToFinish();
//			String script = "if (document.readyState == 'complete'){if (window.jQuery){if (jQuery.active == 0){return true;} else {return false;}} else {return true;}} else {return false}";
			String script = "if (document.readyState == 'complete'){if (window.jQuery){if (jQuery.active == 0){return true;}} else {return true;}}";
			int counter = 0;
			int TargetCounter = Configurations.PageLoadTimeOut * 10;
			while (true) {
//				if (js.executeScript("return document.readyState").equals("complete")) {
				if ((Boolean) js.executeScript(script)) {
					return true;
				} else {
					Thread.sleep(100);
					counter++;
					if (counter == TargetCounter) {
						System.out.println("Waited for " + Configurations.PageLoadTimeOut
								+ " Seconds but page is not fully loaded yet");
						return false;
					}
				}

			}
		} catch (NullPointerException e) {
			// do nothing
		} catch (JavascriptException e) {
			// do nothing
		} catch (InterruptedException e) {
//			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Clicks on an Windows Element identified by the provided <code>By</code>
	 * object. If it succeeds to click returns <code>true</code>, <code>false</code>
	 * otherwise.
	 * 
	 * <p>
	 * Before calling this method one must attach to a window by calling
	 * <code>switchToWindow(String attributeValue, String attributeName)</code> or
	 * <code>switchToWindow(String attributeValue, String attributeName, boolean partialMatch,
			boolean searchEntireTree)</code>
	 * </p>
	 * 
	 * @param by an <code>By</code> object to identify the element
	 * @return <code>true</code> if it succeeds to click, <code>false</code>
	 *         otherwise
	 */
	protected boolean wClick(By by) {
		try {
			WebElement ele = wElement.findElement(by);
			bringToFront(ele);
			ele.click();
			log(Status.INFO, "Successfully clicked on " + by.toString());
		} catch (Exception e) {
			e.printStackTrace();
			log(Status.INFO, "Unable to click on " + by.toString(), e);
			return false;
		}
		return true;
	}

	/**
	 * Types into an Windows Element identified by the provided <code>By</code>
	 * object. If it succeeds to click returns <code>true</code>, <code>false</code>
	 * otherwise.
	 * 
	 * <p>
	 * Before calling this method one must attach to a window by calling
	 * <code>switchToWindow(String attributeValue, String attributeName)</code> or
	 * <code>switchToWindow(String attributeValue, String attributeName, boolean partialMatch,
			boolean searchEntireTree)</code>
	 * </p>
	 * 
	 * @param by an <code>By</code> object to identify the element
	 * @return <code>true</code> if it succeeds to click, <code>false</code>
	 *         otherwise
	 */
	protected boolean wSendkeys(By by, String string) {
		try {
			WebElement ele = wElement.findElement(by);
			bringToFront(ele);
			ele.sendKeys(string);
			log(Status.INFO, "Successfully typed " + string + " into " + by.toString());
		} catch (Exception e) {
			e.printStackTrace();
			log(Status.INFO, "Unable to type" + string + " into " + by.toString(), e);
			return false;
		}
		return true;
	}

	static class Action {
		private static Actions action;

		public Action(WebDriver driver) {
			action = new Actions(driver);
		}

		/**
		 * Clicks at the current mouse location. Useful when combined with
		 * moveToElement(org.openqa.selenium.WebElement, int, int) or moveByOffset(int,
		 * int).
		 * 
		 * @return A self reference
		 */
		public Action click() {
			try {
				action.click().build().perform();
				log(Status.INFO, "Successfully clicked");
			} catch (Exception e) {
				log(Status.INFO, "Failed to click", e);
				return null;
			}
			return this;
		}

		/**
		 * Clicks in the middle of the given element. Equivalent to:
		 * <Code>Actions.moveToElement(onElement).click()</code>
		 * 
		 * @param target Element to click
		 * @return A self reference
		 */
		public Action click(By target) {
			try {
				action.click(get(target)).build().perform();
				log(Status.INFO, "Successfully clicked on " + target.toString());
			} catch (Exception e) {
				log(Status.INFO, "Failed to clicked on " + target.toString(), e);
				return null;
			}
			return this;
		}

		/**
		 * Clicks in the middle of the given element. Equivalent to:
		 * <Code>Actions.moveToElement(onElement).click()</code>
		 * 
		 * @param target Element to click
		 * @return A self reference
		 */
		public Action click(WebElement target) {
			try {
				action.click(highlight(target)).build().perform();
				log(Status.INFO, "Successfully clicked on " + target.toString());
			} catch (Exception e) {
				log(Status.INFO, "Failed to clicked on " + target.toString(), e);
				return null;
			}
			return this;
		}

		/**
		 * Clicks (without releasing) at the current mouse location.
		 * 
		 * @return A self reference
		 */
		public Action clickAndHold() {
			try {
				action.clickAndHold().build().perform();
				log(Status.INFO, "Successfully clicked and held");
			} catch (Exception e) {
				log(Status.INFO, "Failed to click and held", e);
				return null;
			}
			return this;
		}

		/**
		 * Performs a context-click at the current mouse location.
		 * 
		 * @return A self reference
		 */
		public Action contextClick() {
			try {
				action.contextClick().build().perform();
				log(Status.INFO, "Successfully context-clicked");
			} catch (Exception e) {
				log(Status.INFO, "Failed to context-click", e);
				return null;
			}
			return this;
		}

		/**
		 * Performs a context-click at middle of the given element. First performs a
		 * mouseMove to the location of the element.
		 * 
		 * @param target Element to move to
		 * @return A self reference
		 */
		public Action contextClick(By target) {
			try {
				action.contextClick(get(target)).build().perform();
				log(Status.INFO, "Successfully context-clicked at " + target.toString());
			} catch (Exception e) {
				log(Status.INFO, "Failed to context-click at " + target.toString(), e);
				return null;
			}
			return this;
		}

		/**
		 * Performs a context-click at middle of the given element. First performs a
		 * mouseMove to the location of the element.
		 * 
		 * @param target Element to move to
		 * @return A self reference
		 */
		public Action contextClick(WebElement target) {
			try {
				action.contextClick(highlight(target)).build().perform();
				log(Status.INFO, "Successfully context-clicked at " + target.toString());
			} catch (Exception e) {
				log(Status.INFO, "Failed to context-click at " + target.toString(), e);
				return null;
			}
			return this;
		}

		/**
		 * Performs a double-click at the current mouse location.
		 * 
		 * @return A self reference
		 */
		public Action doubleClick() {
			try {
				action.doubleClick().build().perform();
				log(Status.INFO, "Successfully double clicked");
			} catch (Exception e) {
				log(Status.INFO, "Failed to double click", e);
				return null;
			}
			return this;
		}

		/**
		 * Performs a double-click at middle of the given element. Equivalent to:
		 * <code>Action.moveToElement(element).doubleClick()</code>
		 * 
		 * @param target Element to move to
		 * @return A self reference
		 */
		public Action doubleClick(By target) {
			try {
				action.doubleClick(get(target)).build().perform();
				log(Status.INFO, "Successfully double clicked on " + target.toString());
			} catch (Exception e) {
				log(Status.INFO, "Failed to double click on " + target.toString(), e);
				return null;
			}
			return this;
		}

		/**
		 * Performs a double-click at middle of the given element. Equivalent to:
		 * <code>Action.moveToElement(element).doubleClick()</code>
		 * 
		 * @param target Element to move to
		 * @return A self reference
		 */
		public Action doubleClick(WebElement target) {
			try {
				action.doubleClick(highlight(target)).build().perform();
				log(Status.INFO, "Successfully double clicked on " + target.toString());
			} catch (Exception e) {
				log(Status.INFO, "Failed to double click on " + target.toString(), e);
				return null;
			}
			return this;
		}

		/**
		 * A convenience method that performs click-and-hold at the location of the
		 * source element, moves to the location of the target element, then releases
		 * the mouse.
		 * 
		 * @param source element to emulate mouse-button down at
		 * @param target element to move to and release the mouse at
		 * @return A self reference
		 */
		public Action dragAndDrop(By source, By target) {
			try {
				action.dragAndDrop(get(source), get(target)).build().perform();
				log(Status.INFO, "Successfully dragged " + source.toString() + " and dropped at " + target.toString());
			} catch (Exception e) {
				log(Status.INFO, "Failed to drag " + source.toString() + " and drop at " + target.toString(), e);
				return null;
			}
			return this;
		}

		/**
		 * A convenience method that performs click-and-hold at the location of the
		 * source element, moves to the location of the target element, then releases
		 * the mouse.
		 * 
		 * @param source element to emulate mouse-button down at
		 * @param target element to move to and release the mouse at
		 * @return A self reference
		 */
		public Action dragAndDrop(WebElement source, WebElement target) {
			try {
				action.dragAndDrop(highlight(source), highlight(target)).build().perform();
				log(Status.INFO, "Successfully dragged " + source.toString() + " and dropped at " + target.toString());
			} catch (Exception e) {
				log(Status.INFO, "Failed to drag " + source.toString() + " and drop at " + target.toString(), e);
				return null;
			}
			return this;
		}

		/**
		 * A convenience method that performs click-and-hold at the location of the
		 * source element, moves by a given offset, then releases the mouse
		 * 
		 * @param source  element to emulate button down at
		 * @param xOffset horizontal move offset
		 * @param yOffset vertical move offset
		 * @return A self reference
		 */
		public Action dragAndDropBy(By source, int xOffset, int yOffset) {
			try {
				action.dragAndDropBy(get(source), xOffset, yOffset).build().perform();
				log(Status.INFO, "Successfully dragged " + source.toString() + " and dropped by offset (" + xOffset
						+ "," + yOffset + ")");
			} catch (Exception e) {
				log(Status.INFO,
						"Failed to drag " + source.toString() + " and drop by offset (" + xOffset + "," + yOffset + ")",
						e);
				return null;
			}
			return this;
		}

		/**
		 * A convenience method that performs click-and-hold at the location of the
		 * source element, moves by a given offset, then releases the mouse
		 * 
		 * @param source  element to emulate button down at
		 * @param xOffset horizontal move offset
		 * @param yOffset vertical move offset
		 * @return A self reference
		 */
		public Action dragAndDropBy(WebElement source, int xOffset, int yOffset) {
			try {
				action.dragAndDropBy(highlight(source), xOffset, yOffset).build().perform();
				log(Status.INFO, "Successfully dragged " + source.toString() + " and dropped by offset (" + xOffset
						+ "," + yOffset + ")");
			} catch (Exception e) {
				log(Status.INFO,
						"Failed to drag " + source.toString() + " and drop by offset (" + xOffset + "," + yOffset + ")",
						e);
				return null;
			}
			return this;
		}

		/**
		 * Performs a modifier key press after focusing on an element. Equivalent to:
		 * Actions.click(element).sendKeys(theKey);
		 * 
		 * @param target WebElement to perform the action
		 * @param key    Either Keys.SHIFT, Keys.ALT or Keys.CONTROL. If the provided
		 *               key is none of those, IllegalArgumentException is thrown.
		 * @return A self reference
		 */
		public Action keyDown(By target, CharSequence key) {
			try {
				action.keyDown(get(target), key).build().perform();
				log(Status.INFO, "Successfully pressed " + key.toString() + " on " + target.toString());
			} catch (Exception e) {
				log(Status.INFO, "Failed to press " + key.toString() + " on " + target.toString(), e);
				return null;
			}
			return this;
		}

		/**
		 * Performs a modifier key press. Does not release the modifier key - subsequent
		 * interactions may assume it's kept pressed. Note that the modifier key is
		 * never released implicitly - either keyUp(theKey) or sendKeys(Keys.NULL) must
		 * be called to release the modifier.
		 * 
		 * @param key Either Keys.SHIFT, Keys.ALT or Keys.CONTROL. If the provided key
		 *            is none of those, IllegalArgumentException is thrown.
		 * @return A self reference
		 */
		public Action keyDown(CharSequence key) {
			try {
				action.keyDown(key).build().perform();
				log(Status.INFO, "Successfully pressed " + key.toString());
			} catch (Exception e) {
				log(Status.INFO, "Successfully pressed " + key.toString(), e);
				return null;
			}
			return this;
		}

		/**
		 * Performs a modifier key press after focusing on an element. Equivalent to:
		 * Actions.click(element).sendKeys(theKey);
		 * 
		 * @param target WebElement to perform the action
		 * @param key    Either Keys.SHIFT, Keys.ALT or Keys.CONTROL. If the provided
		 *               key is none of those, IllegalArgumentException is thrown.
		 * @return A self reference
		 */
		public Action keyDown(WebElement target, CharSequence key) {
			try {
				action.keyDown(target, key).build().perform();
				log(Status.INFO, "Successfully pressed " + key.toString() + " on " + target.toString());
			} catch (Exception e) {
				log(Status.INFO, "Failed to press " + key.toString() + " on " + target.toString(), e);
				return null;
			}
			return this;
		}

		/**
		 * Performs a modifier key release after focusing on an element. Equivalent to:
		 * Actions.click(element).sendKeys(theKey);
		 * 
		 * @param target WebElement to perform the action on
		 * @param key    Either Keys.SHIFT, Keys.ALT or Keys.CONTROL.
		 * @return A self reference
		 */
		public Action keyUp(By target, CharSequence key) {
			try {
				action.keyUp(get(target), key).build().perform();
				log(Status.INFO, "Successfully released " + key.toString() + " from " + target.toString());
			} catch (Exception e) {
				log(Status.INFO, "Failed to release " + key.toString() + " from " + target.toString(), e);
				return null;
			}
			return this;
		}

		/**
		 * Performs a modifier key release. Releasing a non-depressed modifier key will
		 * yield undefined behavior.
		 * 
		 * @param key Either Keys.SHIFT, Keys.ALT or Keys.CONTROL.
		 * @return A self reference
		 */
		public Action keyUp(CharSequence key) {
			try {
				action.keyUp(key).build().perform();
				log(Status.INFO, "Successfully released " + key.toString());
			} catch (Exception e) {
				log(Status.INFO, "Failed to release " + key.toString(), e);
				return null;
			}
			return this;
		}

		/**
		 * Performs a modifier key release after focusing on an element. Equivalent to:
		 * Actions.click(element).sendKeys(theKey);
		 * 
		 * @param target WebElement to perform the action on
		 * @param key    Either Keys.SHIFT, Keys.ALT or Keys.CONTROL.
		 * @return A self reference
		 */
		public Action keyUp(WebElement target, CharSequence key) {
			try {
				action.keyUp(highlight(target), key).build().perform();
				log(Status.INFO, "Successfully released " + key.toString() + " from " + target.toString());
			} catch (Exception e) {
				log(Status.INFO, "Failed to release " + key.toString() + " from " + target.toString(), e);
				return null;
			}
			return this;
		}

		/**
		 * Moves the mouse from its current position (or 0,0) by the given offset. If
		 * the coordinates provided are outside the viewport (the mouse will end up
		 * outside the browser window) then the viewport is scrolled to match.
		 * 
		 * @param xOffset horizontal move offset. A negative value means moving the
		 *                mouse left.
		 * @param yOffset vertical move offset. A negative value means moving the mouse
		 *                up.
		 * @return A self reference
		 */
		public Action moveByOffset(int xOffset, int yOffset) {
			try {
				action.moveByOffset(xOffset, yOffset).build().perform();
				log(Status.INFO, "Successfully moved by (" + xOffset + "," + yOffset + ")");
			} catch (Exception e) {
				log(Status.INFO, "Failed to move by (" + xOffset + "," + yOffset + ")", e);
				return null;
			}
			return this;
		}

		/**
		 * Moves the mouse to the middle of the element. The element is scrolled into
		 * view and its location is calculated using getBoundingClientRect.
		 * 
		 * @param target element to move to.
		 * @return A self reference
		 */
		public Action moveToElement(By target) {
			try {
				action.moveToElement(get(target)).build().perform();
				log(Status.INFO, "Successfully moved to " + target.toString());
			} catch (Exception e) {
				log(Status.INFO, "Failed to move to " + target.toString(), e);
				return null;
			}
			return this;
		}

		/**
		 * Moves the mouse to an offset from the top-left corner of the element. The
		 * element is scrolled into view and its location is calculated using
		 * getBoundingClientRect.
		 * 
		 * @param target  element to move to.
		 * @param xOffset horizontal move offset from the top-left corner. A negative
		 *                value means moving the mouse left.
		 * @param yOffset vertical move offset from the top-left corner. A negative
		 *                value means moving the mouse up.
		 * @return A self reference
		 */
		public Action moveToElement(By target, int xOffset, int yOffset) {
			try {
				action.moveToElement(get(target), xOffset, yOffset).build().perform();
				log(Status.INFO, "Successfully moved to " + target.toString() + " with offset (" + xOffset + ","
						+ yOffset + ")");
			} catch (Exception e) {
				log(Status.INFO,
						"Failed to move to " + target.toString() + " with offset (" + xOffset + "," + yOffset + ")", e);
				return null;
			}
			return this;
		}

		/**
		 * Moves the mouse to the middle of the element. The element is scrolled into
		 * view and its location is calculated using getBoundingClientRect.
		 * 
		 * @param target element to move to.
		 * @return A self reference
		 */
		public Action moveToElement(WebElement target) {
			try {
				action.moveToElement(highlight(target)).build().perform();
				log(Status.INFO, "Successfully moved to " + target.toString());
			} catch (Exception e) {
				log(Status.INFO, "Failed to move to " + target.toString(), e);
				return null;
			}
			return this;
		}

		/**
		 * Moves the mouse to an offset from the top-left corner of the element. The
		 * element is scrolled into view and its location is calculated using
		 * getBoundingClientRect.
		 * 
		 * @param target  element to move to.
		 * @param xOffset horizontal move offset from the top-left corner. A negative
		 *                value means moving the mouse left.
		 * @param yOffset vertical move offset from the top-left corner. A negative
		 *                value means moving the mouse up.
		 * @return A self reference
		 */
		public Action moveToElement(WebElement target, int xOffset, int yOffset) {
			try {
				action.moveToElement(highlight(target), xOffset, yOffset).build().perform();
				log(Status.INFO, "Successfully moved to " + target.toString() + " with offset (" + xOffset + ","
						+ yOffset + ")");
			} catch (Exception e) {
				log(Status.INFO,
						"Failed to move to " + target.toString() + " with offset (" + xOffset + "," + yOffset + ")", e);
				return null;
			}
			return this;
		}

		/**
		 * @param duration
		 * @return A self reference
		 */
		public Action pause(Duration duration) {
			try {
				action.pause(duration).build().perform();
				log(Status.INFO, "Successfully paused for " + duration.toString());
			} catch (Exception e) {
				log(Status.INFO, "Failed to pause for " + duration.toString(), e);
				return null;
			}
			return this;
		}

		/**
		 * @param pause
		 * @return A self reference
		 */
		public Action pause(long pause) {
			try {
				action.pause(pause).build().perform();
				log(Status.INFO, "Successfully paused for " + Long.toString(pause));
			} catch (Exception e) {
				log(Status.INFO, "Failed to pause for " + Long.toString(pause), e);
				return null;
			}
			return this;
		}

		/**
		 * Releases the depressed left mouse button at the current mouse location.
		 * 
		 * @return A self reference
		 */
		public Action release() {
			try {
				action.release().build().perform();
				log(Status.INFO, "Successfully released mouse click");
			} catch (Exception e) {
				log(Status.INFO, "Failed to release mouse click", e);
				return null;
			}
			return this;
		}

		/**
		 * Releases the depressed left mouse button, in the middle of the given element.
		 * This is equivalent to:
		 * <code>Actions.moveToElement(onElement).release()</code>. Invoking this
		 * action, without invoking <code>clickAndHold()</code> first will result in
		 * undefined behavior.
		 * 
		 * @param target Element to release the mouse button above.
		 * @return A self reference
		 */
		public Action release(By target) {
			try {
				action.release(get(target)).build().perform();
				log(Status.INFO, "Successfully released mouse click from " + target.toString());
			} catch (Exception e) {
				log(Status.INFO, "Failed to release mouse click from " + target.toString(), e);
				return null;
			}
			return this;
		}

		/**
		 * Releases the depressed left mouse button, in the middle of the given element.
		 * This is equivalent to:
		 * <code>Actions.moveToElement(onElement).release()</code>. Invoking this
		 * action, without invoking <code>clickAndHold()</code> first will result in
		 * undefined behavior.
		 * 
		 * @param target Element to release the mouse button above.
		 * @return A self reference
		 */
		public Action release(WebElement target) {
			try {
				action.release(highlight(target)).build().perform();
				log(Status.INFO, "Successfully released mouse click from " + target.toString());
			} catch (Exception e) {
				log(Status.INFO, "Failed to release mouse click from " + target.toString(), e);
				return null;
			}
			return this;
		}

		/**
		 * Equivalent to calling: Actions.click(element).sendKeys(keysToSend). This
		 * method is different from {@link #sendKeys(WebElement, CharSequence...)} - see
		 * {@link #sendKeys(CharSequence...)} for details how.
		 * 
		 * @param target element to focus on.
		 * @param keys   The keys
		 * @return A self reference
		 */
		public Action sendKeys(By target, CharSequence... keys) {
			try {
				action.sendKeys(get(target), keys).build().perform();
				log(Status.INFO, "Successfully typed " + keys.toString() + " into " + target.toString());
			} catch (Exception e) {
				log(Status.INFO, "Failed to typed " + keys.toString() + " into " + target.toString(), e);
				return null;
			}
			return this;
		}

		/**
		 * Sends keys to the active element. This differs from calling
		 * WebElement.sendKeys(CharSequence...) on the active element in two ways:
		 * <li>The modifier keys included in this call are not released.
		 * <li>There is no attempt to re-focus the element - so sendKeys(Keys.TAB) for
		 * switching elements should work.
		 * 
		 * @param keys The keys
		 * @return A self reference
		 */
		public Action sendKeys(CharSequence... keys) {
			try {
				action.sendKeys(keys).build().perform();
				log(Status.INFO, "Successfully typed " + keys.toString());
			} catch (Exception e) {
				log(Status.INFO, "Failed to type " + keys.toString(), e);
				return null;
			}
			return this;
		}

		/**
		 * Equivalent to calling: Actions.click(element).sendKeys(keysToSend). This
		 * method is different from {@link #sendKeys(WebElement, CharSequence...)} - see
		 * {@link #sendKeys(CharSequence...)} for details how.
		 * 
		 * @param target element to focus on.
		 * @param keys   The keys
		 * @return A self reference
		 */
		public Action sendKeys(WebElement target, CharSequence... keys) {
			try {
				action.sendKeys(highlight(target), keys).build().perform();
				log(Status.INFO, "Successfully typed " + keys.toString() + " into " + target.toString());
			} catch (Exception e) {
				log(Status.INFO, "Failed to typed " + keys.toString() + " into " + target.toString(), e);
				return null;
			}
			return this;
		}
	}
}
