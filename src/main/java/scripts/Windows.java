package scripts;

import org.openqa.selenium.By;

import managers.DriverUtil;
import managers.Reporter;

public class Windows extends DriverUtil
{

	public Windows(Reporter reportManager)
	{
		super(reportManager);
		// TODO Auto-generated constructor stub
	}
	
	public boolean notepad()
	{
		startDesktopAutomation();
		click(By.name("Text Edit"));
		stopDesktopAutomation();
		return true;
		
	}

}
