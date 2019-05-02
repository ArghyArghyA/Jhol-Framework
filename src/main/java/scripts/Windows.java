package scripts;

import org.openqa.selenium.By;

import managers.DriverUtil;
import managers.Reporter;

public class Windows extends DriverUtil
{

	public Windows(Reporter reportManager)
	{
		super(reportManager);
	}

	public boolean notepad()
	{
		return startDesktopAutomation() && switchToWindow("Notepad", "ClassName")
				&& wSendkeys(By.name("Text Editor"), "test") && wClick(By.name("Close"))
				&& wClick(By.name("Don't Save")) && stopDesktopAutomation();
	}


}
