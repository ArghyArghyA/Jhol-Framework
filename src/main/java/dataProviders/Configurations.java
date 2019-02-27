package dataProviders;

import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.safari.SafariOptions;

public class Configurations {

	public static final String URL = "https://www.google.com/";

	// timeouts
	public static final int PageLoadTimeOut = 120; // in seconds
	public static final int ShortTimeOut = 5;// in seconds
	public static final int LongTimeOut = 60;// in seconds
	
	/**set all the required options in the static block at the bottom of the <code>Configurations</code> class*/
	public static final InternetExplorerOptions ieOptions = new InternetExplorerOptions();
	/**set all the required options in the static block at the bottom of the <code>Configurations</code> class*/
	public static final ChromeOptions chromeOptions = new ChromeOptions();
	/**set all the required options in the static block at the bottom of the <code>Configurations</code> class*/
	public static final FirefoxOptions firefoxOptions = new FirefoxOptions();
	/**set all the required options in the static block at the bottom of the <code>Configurations</code> class*/
	public static final SafariOptions safariOptions = new SafariOptions();

	/**
	 * Title for the Report
	 */
	public static final String ReportName = "Demo framework";
	/**
	 * Path for the Data-sheet. can be relative or full path
	 */
	public static final String DataSheetPath = "./src/test/resources/Data/DataSheet.xlsx";
	
	/**
	 * String class name including package name as <code>package.class</code> contaning all the End-to-End scripts
	 */
	public static final String ClassContainingTransactionScripts = "runners.Jhol";

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
	
	
	static
	{
		ieOptions.setCapability(InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION, true);
//		ieOptions.setCapability(InternetExplorerDriver.INITIAL_BROWSER_URL, Configurations.URL);
		ieOptions.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
		ieOptions.setCapability(InternetExplorerDriver.UNEXPECTED_ALERT_BEHAVIOR, "accept");
		chromeOptions.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.ACCEPT);
//		chromeOptions.setCapability(, value);
		firefoxOptions.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.ACCEPT);
	}

}