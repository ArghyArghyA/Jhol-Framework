package scripts;

import com.aventstack.extentreports.Status;

import dataProviders.Configurations.OutPutFields;
import managers.DriverUtil;
import managers.Reporter;
import pageObjects.Google;

public class Search extends DriverUtil {

	public Search(Reporter reportManager) {
		super(reportManager);
		// TODO Auto-generated constructor stub
	}

	public boolean search() {
		boolean proceed = true;
		try {
			proceed = ((dictionary.get("Search Keyword") != null)
					? sendkeys(Google.SearchBox, dictionary.get("Search Keyword"))
					: sendkeys(Google.SearchBox));
			proceed = proceed && click(Google.GoogleSearch);
			waitForPageToLoad();
			if (proceed) {
				dictionary.put(OutPutFields.FirstResult.columnHeader, getText(Google.SearchResultText));
				dictionary.put(OutPutFields.URL.columnHeader, getAttribute(Google.searchResultURL, "href"));

				log(Status.PASS, "Successfully Searched");
			} else {
				log(Status.FAIL, "Unable to Search");
			}
		} catch (Exception e) {
			
			log(Status.FAIL, "Unable to Search", e);
			return false;
		}
		return proceed;
	}
	
	public boolean clickFirstResult()
	{
		boolean proceed = true;
		try {
			proceed = click(Google.searchResultURL);
			waitForPageToLoad();
			proceed = proceed && assertTitle(dictionary.get("Search Keyword"));
			if (proceed) {
				log(Status.PASS, "Successfully opened first search result");
			} else {
				log(Status.FAIL, "Unable to open first search result");
			}
		} catch (Exception e) {
			
			log(Status.FAIL, "Unable to open first search result", e);
			return false;
		}
		return proceed;
	}

}
