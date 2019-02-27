package pageObjects;

import org.openqa.selenium.By;

/**
 * <p>
 * Create one Class for each page. Create inner classes in cases where the page
 * is very large and can be divided into sections. Make them static.
 * </p>
 * 
 * <p>
 * Create one selenium By object variable for every element which needs to be
 * interacted. Make them Public Static Final If any element needs to have
 * dynamic part(s) in it then create a method with dynamic parts as it's
 * argument and return By object from it. Make them Public Static Final.
 * <p>
 * 
 * @author Alpha Romeo
 *
 */
public class Google {
	public static final By SearchBox = By.cssSelector("input[title = 'Search']");
	public static final By GoogleSearch = By.cssSelector("div.FPdoLc>center>input[value = 'Google Search']");
	public static final By IMFeelingLucky = By.cssSelector("div.FPdoLc>center>input[name = 'btnI']");
	public static final By SearchResultText = By.tagName("h3");
	public static final By searchResultURL = By.cssSelector("div.r>a");

	public static class Apps {
		public static final By Apps = By.cssSelector("a[title = 'Google apps']");

		public static final By app(String appName) {
			return By.xpath("//span[text() = '" + appName + "']/preceding-sibling :: span");
		}

		public static final By GooglePlus = By.xpath("//span[text() = 'Google+']/preceding-sibling :: span");
		public static final By Search = By.xpath("//span[text() = 'Search']/preceding-sibling :: span");
		public static final By YouTube = By.xpath("//span[text() = 'YouTube']/preceding-sibling :: span");
		public static final By Maps = By.xpath("//span[text() = 'Maps']/preceding-sibling :: span");
		public static final By Play = By.xpath("//span[text() = 'Play']/preceding-sibling :: span");
		public static final By News = By.xpath("//span[text() = 'News']/preceding-sibling :: span");
		public static final By Gmail = By.xpath("//span[text() = 'Gmail']/preceding-sibling :: span");
		public static final By Drive = By.xpath("//span[text() = 'Drive']/preceding-sibling :: span");
		public static final By Calendar = By.xpath("//span[text() = 'Calendar']/preceding-sibling :: span");
		public static final By PlayMusic = By.xpath("//span[text() = 'Play Music']/preceding-sibling :: span");

		public static final By More = By.linkText("More");
	}
}
