package settings;

import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.safari.SafariOptions;

import com.aventstack.extentreports.Status;

public class Configurations {

	public static final String URL = "https://www.google.com/";

	// timeouts
	public static final int PageLoadTimeOut = 120; // in seconds
	public static final int ShortTimeOut = 5;// in seconds
	public static final int LongTimeOut = 60;// in seconds
	
	/**
	 * Applicable for TestNG only. Will retry the failed test cases if greater than 0
	 */
	public static final int maximumRetryCount = 2;

	/**
	 * set it true to enable highlight method before trying to interact with the
	 * elements. useful for visual representation of the execution
	 * 
	 * Change the hexadecimal color code of highlightColor to suit your choice.
	 */
	public static final boolean enableHighlight = true;
	public static final String highlightColor = "#00CC00"; // hexadecimal color for the highlight of elements
	/**
	 * set it true to enable scrollToView method before trying to interact with the
	 * elements. Specificly useful for websites with sticky/fixed position
	 * header/footer/left pane/right pane.
	 * 
	 */
	public static final boolean enableScrollToView = true;

	/**
	 * Mention minimum logLevel threshold for the report and console. all the log
	 * statements with status below the threshold will be ignored. Log hierarchy is
	 * given as:
	 * <li>debug: 0,</li>
	 * <li>info: 1,</li>
	 * <li>pass: 2,</li>
	 * <li>skip: 3,</li>
	 * <li>warning: 4,</li>
	 * <li>error: 5,</li>
	 * <li>fail: 6,</li>
	 * <li>fatal: 7.</li>
	 */
	public static final Status minimumLogLevel = Status.DEBUG;

	/** set all the required capabilities in the static block */
	public static final InternetExplorerOptions ieOptions = new InternetExplorerOptions();
	static {
		ieOptions.setCapability(InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION, true);
//		ieOptions.setCapability(InternetExplorerDriver.INITIAL_BROWSER_URL, Configurations.URL);
		ieOptions.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
		ieOptions.setCapability(InternetExplorerDriver.UNEXPECTED_ALERT_BEHAVIOR, "accept");
	}
	/** set all the required options in the static block */
	public static final ChromeOptions chromeOptions = new ChromeOptions();
	static {
		chromeOptions.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.ACCEPT);
//		chromeOptions.setCapability(, value);
	}
	/** set all the required options in the static block */
	public static final FirefoxOptions firefoxOptions = new FirefoxOptions();
	static {
		firefoxOptions.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.ACCEPT);
	}
	/** set all the required options in the static block */
	public static final SafariOptions safariOptions = new SafariOptions();
	static {
//		safariOptions.setAutomaticInspection(true);
	}

	/**
	 * Title for the Report
	 */
	public static final String ReportName = "Demo framework";
	/**
	 * Path for the Data-sheet. can be relative or full path
	 */
	public static final String DataSheetPath = "./src/test/resources/Data/DataSheet.xlsx";
	
	/**
	 * Path for the Feature File. can be relative or full path
	 */
	public static final String FeatureFilePath = "./src/test/resources/FunctionalTests/test.feature";

	/**
	 * String class name including package name as <code>package.class</code>
	 * containing all the End-to-End scripts script methods must match to that of
	 * TestCaseDescription of the test case
	 */
	public static final String ClassContainingTransactionScripts = "scripts.transaction.Transactions";

	/**
	 * Contains all the output field details and their column names/indexes.
	 * preferably store all the details in the dictionary with the same key as the
	 * column name eg. if u need to output the search result in the demo example and
	 * the column name is "SearchResult" store the result in dictionary like this
	 * 
	 * <pre>
	 * <code>dictionary.put("SearchResult", "whatever u want to put");</code>
	 * </pre>
	 * 
	 * make sure to organize the output fields in increasing column index manner.
	 * These columns must be present in the DataSheet. Otherwise the test data
	 * columns will be overwritten
	 * 
	 * @author Alpha Romeo
	 *
	 */
	public static enum OutPutFields {

		testStatus("Status", 3), FirstResult("First Result", 4), URL("URL", 5);

		public int columnIndex;// zero indexed
		public String columnHeader;

		private OutPutFields(String columnHeader, int columnIndex) {
			this.columnIndex = columnIndex;
			this.columnHeader = columnHeader;
		}

	}

}