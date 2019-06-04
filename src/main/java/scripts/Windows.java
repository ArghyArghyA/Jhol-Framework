package scripts;

import org.openqa.selenium.By;

import com.aventstack.extentreports.Status;

import managers.DriverUtil;
import managers.Reporter;
import pageObjects.SaveAs;

public class Windows extends DriverUtil {

	public Windows(Reporter reportManager) {
		super(reportManager);
	}

	public boolean notepad() {
		boolean proceed = switchToWindow("Notepad", "ClassName") && wSendkeys(By.name("Text Editor"), "test")
				&& wClick(By.name("Close")) && wClick(By.name("Don't Save")) && stopWiniumServer();
		if (proceed)
			log(Status.PASS, "Successfully processed Notepad");
		else
			log(Status.FAIL, "Failed to process Notepad");
		return proceed;
	}

	public boolean saveSearchResult() {
		boolean proceed = true;
		if (get("Save Search Result") == null || !get("Save Search Result").equalsIgnoreCase("yes"))
			return true;
		proceed = switchToWindow("Google Chrome", "Name", false, true) && wClick(SaveAs.DotMenu)
				&& wClick(SaveAs.MoreTools) && wClick(SaveAs.SavePageAs);
		return proceed && switchToWindow("Save As", "Name", false, true)
				&& wSendkeys(SaveAs.Address, System.getProperty("user.dir") + "/src/test/resources/Results/Dowloads")
				&& wSendkeys(SaveAs.FileName, get("Search Keyword")) && wClick(SaveAs.Save) && stopWiniumServer();
	}

}
