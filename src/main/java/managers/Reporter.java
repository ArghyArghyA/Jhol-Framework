package managers;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriverException;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import settings.Configurations;
import settings.Configurations.OutPutFields;

import org.apache.commons.io.FileUtils;

public final class Reporter {

	private static ExtentReports report;
	private static ExtentTest test;
	private static String testName;
	final String timeStamp;
	private static int counter;
	private static Reporter reporter = new Reporter();

	private Reporter() {
		super();
		this.timeStamp = new Timestamp(new Date().getTime()).toString().replaceAll(":", "-");
		Reporter.report = new ExtentReports();
		ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(
				"./src/test/resources/Results/Report/" + timeStamp + ".html");
		htmlReporter.config().setTheme(Theme.DARK);
		htmlReporter.config().setDocumentTitle("Status Report");
		htmlReporter.config().setReportName(Configurations.ReportName);
		String css = "li.waves-effect:nth-child(2) { display: none; }"
//				+ "div.chart-box { display: none; }"
				+ "div.left.panel-name { display: none; }";
		htmlReporter.config().setCSS(css);

		report.attachReporter(htmlReporter);
		new File("./src/test/resources/Results/Screenshot/" + timeStamp).mkdir();
		/*
		 * uncomment the below lines, in case having any driver spawn related issue.
		 * Also download latest version of corresponding driver.exe in
		 * "./src/main/resources/" folder
		 */
//		System.setProperty("webdriver.ie.driver", "./src/main/resources/IEDriverServer.exe");
//		System.setProperty("webdriver.chrome.driver", "./src/main/resources/chromedriver.exe");
//		System.setProperty("webdriver.gecko.driver", "./src/main/resources/geckodriver.exe");
//		System.setProperty("webdriver.winium.driver.desktop", "./src/main/resources/Winium.Desktop.Driver.exe");

	}

	public static Reporter getInstance() {
		return reporter;
	}

	public void reportTest(HashMap<String, String> dictionary) throws Exception {
		testName = dictionary.get("TestCaseID");
		String testDescription = "<h5>" + dictionary.get("TestCaseDescription") + "</h5>" + "<p>"
				+ dictionary.toString().replace("{", "").replace("}", "") + "</p>";
		test = report.createTest(testName, testDescription);

		DriverUtil.dictionary.clear();
		DriverUtil.dictionary.putAll(dictionary);
	}

	public void endTest() {
		DriverUtil.dictionary.put(OutPutFields.testStatus.columnHeader, test.getStatus().toString());
		DataManager.write(DriverUtil.dictionary);

		HashMap<String, String> temp = new HashMap<String, String>();

		for (OutPutFields o : OutPutFields.values()) {
			if (DriverUtil.dictionary.containsKey(o.columnHeader)) {
				temp.put(o.columnHeader, DriverUtil.dictionary.get(o.columnHeader));
				DriverUtil.dictionary.remove(o.columnHeader);
			}
		}
		String testCaseDesription = DriverUtil.dictionary.remove("TestCaseDescription");
		DriverUtil.dictionary.remove("Execute");
		DriverUtil.dictionary.remove("TestCaseID");
		DriverUtil.dictionary.remove("index");
		DriverUtil.dictionary.remove("sheet");
		// Remove any other columns or temporary details that is not needed on the final
		// report here.

		String description = "<h5>" + testCaseDesription + "</h5>" + "<p>"
				+ DriverUtil.dictionary.toString().replace("{", "").replace("}", "") + "</p>";
		for (OutPutFields o : OutPutFields.values()) {
			if (o.columnHeader != "Status" && temp.containsKey(o.columnHeader)) {
				description = description + "<tr>\r\n" + " <td>" + o.columnHeader + " :</td>\r\n"
						+ "<td style=\"text-align: right\">" + temp.get(o.columnHeader) + "</td>\r\n" + "</tr>";
			}
		}

		if (!description.isEmpty()) {
			description = "<table>\r\n" + description + "    </table>";
			test.getModel().setDescription(description);
		}

		report.flush();

		try {
			if (DriverUtil.wDriver!=null)
			{
				DriverUtil.wDriver.quit();
				DriverUtil.wDriver = null;
			}
			String command = "taskkill /F /IM Winium.Desktop.Driver.exe";
			Runtime.getRuntime().exec(command);
			DriverUtil.driver.quit();
			DriverUtil.driver = null;
			if (DriverUtil.dictionary.get("browser").equalsIgnoreCase("Chrome"))
				command = "taskkill /F /IM chrome.exe /IM chromedriver.exe";
			else if (DriverUtil.dictionary.get("browser").equalsIgnoreCase("Firefox"))
				command = "taskkill /F /IM firefox.exe /IM geckodriver.exe";
			else if (DriverUtil.dictionary.get("browser").equalsIgnoreCase("IE"))
				command = "taskkill /F /IM iexplore.exe /IM IEDriverServer.exe";
			Runtime.getRuntime().exec(command);
		} catch (Exception e) {
		}
	}

	/**
	 * @deprecated
	 */
	public void complete() {
		report.flush();

	}

	public void reportEvent(Status logStatus, String stepName, String details) {
		String imagePath;
		try {
			switch (logStatus) {
			case INFO:
				test.info(stepName + "<br/>" + details.replace("\n", "<br/>"));
				break;
			case PASS:
				test.pass(stepName + "<br/>" + details.replace("\n", "<br/>"));
				break;
			case SKIP:
				test.skip(stepName + "<br/>" + details.replace("\n", "<br/>"));
				break;
			case WARNING:
				takeScreenshot();
				imagePath = "../Screenshot/" + timeStamp + "/" + Reporter.testName + "/" + counter + ".png";
				test.warning(stepName + "<br/>" + details.replace("\n", "<br/>")).addScreenCaptureFromPath(imagePath);
				break;
			case ERROR:
				takeScreenshot();
				imagePath = "../Screenshot/" + timeStamp + "/" + Reporter.testName + "/" + counter + ".png";
				test.error(stepName + "<br/>" + details.replace("\n", "<br/>")).addScreenCaptureFromPath(imagePath);
				break;
			case FAIL:
				takeScreenshot();
				imagePath = "../Screenshot/" + timeStamp + "/" + Reporter.testName + "/" + counter + ".png";
				test.fail(stepName + "<br/>" + details.replace("\n", "<br/>")).addScreenCaptureFromPath(imagePath);
				break;
			case FATAL:
				takeScreenshot();
				imagePath = "../Screenshot/" + timeStamp + "/" + Reporter.testName + "/" + counter + ".png";
				test.fatal(stepName + "<br/>" + details.replace("\n", "<br/>")).addScreenCaptureFromPath(imagePath);
				break;
			default:
				// do nothing
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void reportEvent(Status logStatus, String stepName) {
		reportEvent(logStatus, stepName, "");
	}

	private void takeScreenshot() {
		counter++;
		try {
			File file;
			try {
				TakesScreenshot screenshot = (TakesScreenshot) DriverUtil.driver;
				file = screenshot.getScreenshotAs(OutputType.FILE);
			} catch (NullPointerException e1) {
				if (DriverUtil.wDriver == null)
					DriverUtil.startWiniumServer();
				TakesScreenshot screenshot = (TakesScreenshot) DriverUtil.wDriver;
				file = screenshot.getScreenshotAs(OutputType.FILE);
			}
			String imagePath = "./src/test/resources/Results/Screenshot/" + timeStamp + "/" + Reporter.testName + "/"
					+ counter + ".png";
			File localFile = new File(imagePath);
//			imagePath = localFile.getAbsolutePath();
			try {
				FileUtils.copyFile(file, localFile);
				org.testng.Reporter.log("<a href='."+ imagePath + "'> <img src='."+ imagePath + "' height='100' width='100'/> </a>");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (WebDriverException e) {
			e.printStackTrace();
		} 
	}

}
