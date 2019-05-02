package managers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.InvalidArgumentException;
import org.openqa.selenium.JavascriptException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.winium.DesktopOptions;
import org.openqa.selenium.winium.WiniumDriver;

import com.aventstack.extentreports.Status;
import com.paulhammant.ngwebdriver.NgWebDriver;

import dataProviders.Configurations;

/**
 * @author argroy
 *
 */
public class DriverUtil {
	protected static Actions act;
	protected static HashMap<String, String> dictionary = new HashMap<String, String>();
	protected static Reporter reporter;
	private static String webElementStyle = "";
	protected static WebDriverWait shortWait;
	protected static WebDriverWait longWait;
	protected static WebDriver driver;
	private static WebElement webElement;
	private static WebElement windowElement;
	protected static WiniumDriver windowsDriver;

	public DriverUtil(Reporter reportManager) {
		super();
		DriverUtil.reporter = reportManager;
		DriverUtil.act = new Actions(driver);
		DriverUtil.shortWait = new WebDriverWait(driver, Configurations.ShortTimeOut);
		DriverUtil.longWait = new WebDriverWait(driver, Configurations.LongTimeOut);
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
	 * Clears the input field identified by the provided <code>By</code> object by
	 * using Selenium <code>WebElement.clear()</code>. If it succeeds to select
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
	 * using Selenium <code>WebElement.clear()</code>. If it succeeds to select
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
	 * <code>WebElement.clear()</code>. If it succeeds to select returns
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
			result = generateRandomNumber(length);
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
	protected WebElement get(By by) {
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
	protected WebElement get(By by, boolean waitForPageLoad, boolean explicitWait) {
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
	 * Returns some attribute-value for for the Element identified by the
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
	 * Returns some attribute-value for for the Element identified by the
	 * <code>By</code> object. Before attempting that it waits for the page to load
	 * completely
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
	 * Returns visible text for for the Element identified by the <code>By</code>
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
	 * Returns visible text for for the Element identified by the <code>By</code>
	 * object. Before attempting that it waits for the page to load completely
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
	private void highlight(WebElement ele) {
		if (!Configurations.enableHighlight)
			return;
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
			act.moveToElement(ele).build().perform();
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
			act.moveToElement(ele).build().perform();
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
	 * Wrapper for logging statements. Logs both in console and Reporter. However
	 * depends on the Configurations.minimumLogLevel
	 * 
	 * @param status   <code>Status</code>LogStatus can be PASS, FAIL, WARNING etc
	 * @param stepName <code>String</code>short description of the step
	 */
	protected void log(Status status, String stepName) {
		log(status, stepName, null);
	}

	/**
	 * Wrapper for logging statements. Logs both in console and Reporter However
	 * depends on the <code>Configurations.minimumLogLevel<code>
	 * 
	 * @param status   <code>Status</code>LogStatus can be PASS, FAIL, WARNING etc
	 * @param stepName <code>String</code>short description of the step
	 * @param e        <code>Throwable</code>If any exception occurred during the
	 *                 step
	 */
	protected void log(Status status, String stepName, Throwable e) {
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
		if (e != null) {
			e.printStackTrace();
			reporter.reportEvent(status, stepName, ExceptionUtils.getFullStackTrace(e));
		} else
			reporter.reportEvent(status, stepName);
		System.out.println(stepName);
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
	 * brings the webelement to view. more precisely to the middle of the webPage.
	 * Useful for pages with sticky header, footer, left or right pane etc.
	 * 
	 * set Configurations.enableScrollToView to true
	 * 
	 * @param webElement
	 */
	private void scrollToView(WebElement webElement) {
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
			ele.click();
			for (int i = 0; i < index; i++) {
				ele.sendKeys(Keys.ARROW_DOWN);
			}
			String text = ele.getAttribute("value");
			ele.sendKeys(Keys.TAB);
			log(Status.INFO, "Successfully selected " + text + " from " + by.toString());
		} catch (Exception e) {
			log(Status.INFO, "Unable to select " + Integer.toString(index) + " entry from " + by.toString(), e);
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
	 * @param waitForPageToLoad
	 * @param explicitWait
	 * @return
	 */
	protected boolean selectComboByIndex(WebElement ele, int index) {
		try {
			scrollToView(ele);
			highlight(ele);
			ele.click();
			for (int i = 0; i < index; i++) {
				ele.sendKeys(Keys.ARROW_DOWN);
			}
			String text = ele.getAttribute("value");
			ele.sendKeys(Keys.TAB);
			log(Status.INFO, "Successfully selected " + text + " from " + ele.toString());
		} catch (Exception e) {
			log(Status.INFO, "Unable to select " + Integer.toString(index) + " entry from " + ele.toString(), e);
			return false;
		}
		return true;

	}

	/**
	 * Application specific dropdown selection methods. Can be used for combo box or
	 * any other cases where standard methods does not work. May need to be
	 * revised/Overridden depending on the application
	 * 
	 * @param by
	 * @param text
	 * @return
	 * @deprecated
	 */
	protected boolean selectComboByVisibleText(By by, String text) {
		return selectComboByVisibleText(by, text, true, true);
	}

	/**
	 * @deprecated
	 * @param by
	 * @param text
	 * @param explicitWait
	 * @return
	 * @throws Exception
	 */
	protected boolean selectComboByVisibleText(By by, String text, boolean waitForPageLoad, boolean explicitWait) {
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
			ele.click();
			String initialText = ele.getText();
			while (!initialText.startsWith(text)) {
				ele.sendKeys(Keys.ARROW_DOWN);
				String finalText = ele.getText();
				if (initialText.equalsIgnoreCase(finalText)) {
					throw new InvalidArgumentException(text + " could not be found in the " + by.toString());
				} else {
					initialText = finalText;
				}
			}
			ele.sendKeys(Keys.TAB);
			log(Status.INFO, "Successfully selected " + text + " from " + by.toString());
		} catch (Exception e) {
			log(Status.INFO, "Unable to select " + text + " from " + by.toString(), e);
			return false;
		}
		return true;

	}

	/**
	 * Types random text (minimum length 3, maximum length 10) into the input field
	 * identified by the provided <code>By</code> object by using Selenium
	 * <code>WebElement.sendKeys(String text)</code>. If it succeeds to select
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
	protected boolean sendkeys(By by) {
		return sendkeys(by, true, true);
	}

	/**
	 * Types random text (minimum length 3, maximum length 10) into the input field
	 * identified by the provided <code>By</code> object by using Selenium
	 * <code>WebElement.sendKeys(String text)</code>. If it succeeds to select
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
	protected boolean sendkeys(By by, boolean waitForPageLoad, boolean explicitWait) {
		int length = 3 + new Random().nextInt(8);
		return sendkeys(by, generateRandomString(length), waitForPageLoad, explicitWait);
	}

	/**
	 * Types the specified <code>text</code> into the input field identified by the
	 * provided <code>By</code> object by using Selenium
	 * <code>WebElement.sendKeys(String text)</code>. If it succeeds to select
	 * returns <code>true</code>, <code>false</code> otherwise.
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
	 * <code>WebElement.sendKeys(String text)</code>. If it succeeds to select
	 * returns <code>true</code>, <code>false</code> otherwise.
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
	 * <code>WebElement.sendKeys(String text)</code>. If it succeeds to select
	 * returns <code>true</code>, <code>false</code> otherwise.
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
	 * <code>WebElement.sendKeys(String text)</code>. If it succeeds to select
	 * returns <code>true</code>, <code>false</code> otherwise.
	 * 
	 * <p>
	 * Before attempting to do that, waits for page to load if
	 * <code>waitForPageLoad</code> is <code>true</code>. Explicitly waits for
	 * element to be click-able if <code> explicitWait </code> is <code>true</code>.
	 * </p>
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
	protected boolean startDesktopAutomation() {
		try {
			DesktopOptions options = new DesktopOptions();
			options.setDebugConnectToRunningApp(true);
			windowsDriver = new WiniumDriver(new URL("http://localhost:9999"), options);
			log(Status.INFO, "Successfully started desktop automation server");
		} catch (WebDriverException e) {
			try {
				Runtime.getRuntime().exec("./src/main/resources/Winium.Desktop.Driver.exe");
				Thread.sleep(2000);
				DesktopOptions options = new DesktopOptions();
				options.setDebugConnectToRunningApp(true);
				windowsDriver = new WiniumDriver(new URL("http://localhost:9999"), options);
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

	protected boolean stopDesktopAutomation() {
		try {
			windowsDriver.quit();
			windowsDriver = null;
			windowElement = null;
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
	 * well waits for Frame element to appear on DOM if not found immediately for a
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
	 * well waits for Frame element to appear on DOM if not found immediately for a
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
	 * using <code>contains()</code> method, otherwise complete match is required.
	 * If <code>searchEntireTree</code> is <code>true</code> entire tree starting
	 * from desktop is checked; otherwise only immediate windows available on
	 * desktop are compared
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
			windowElement = windowsDriver.findElement(By.xpath(xPath));
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
	 * {@link dataProviders.Configurations#PageLoadTimeOut PageLoadTimeOut} in
	 * seconds. If the page load is not complete after the stipulated time returns
	 * false.
	 * 
	 * @return <code>true</code> or <code>false</code> depending on whether the page
	 *         load is complete or not after the Page Load Time Out
	 */
	protected boolean waitForPageToLoad() {
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
			windowElement.findElement(by).click();
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
			windowElement.findElement(by).sendKeys(string);
			log(Status.INFO, "Successfully clicked on " + by.toString());
		} catch (Exception e) {
			e.printStackTrace();
			log(Status.INFO, "Unable to click on " + by.toString(), e);
			return false;
		}
		return true;
	}
}
