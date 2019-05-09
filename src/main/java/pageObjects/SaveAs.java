package pageObjects;

import org.openqa.selenium.By;

public class SaveAs {
	public static final By DotMenu = By.xpath("//*[@ControlType = 'ControlType.MenuItem' and @Name = 'Chrome']");
	public static final By MoreTools = By.xpath("//*[@ControlType = 'ControlType.MenuItem' and @Name = 'More tools']");
	public static final By SavePageAs = By.xpath("//*[@ControlType = 'ControlType.MenuItem' and contains(@Name,'Save page as')]");
	public static final By Window = By.name("Save As");
	public static final By Address = By.xpath("//*[contains(@Name,'Address')]");
	public static final By FileName = By.xpath("//*[contains(@Name,'File name:')]");
	public static final By Save = By.name("Save");

}
