package runners;

import org.testng.annotations.Test;

import managers.DataManager;
import managers.Reporter;
import settings.Configurations;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.DataProvider;

import static org.testng.Assert.assertTrue;

import java.awt.BorderLayout;
import java.awt.Container;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JProgressBar;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

public class TestNG {
	private static int numberOfTestcases;
	private static int counter;
	private static JProgressBar progressBar;
	private static JFrame frame;
	private static Reporter report = Reporter.getInstance();
	static {
		frame = new JFrame("Progress");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setUndecorated(true);
		Container content = frame.getContentPane();

		// progress bar
		progressBar = new JProgressBar();
		progressBar.setStringPainted(true);
		content.add(progressBar, BorderLayout.NORTH);
		frame.setSize(300, 45);
		frame.setLocationRelativeTo(null);

		counter = 0;

		progressBar.setBorder(BorderFactory.createTitledBorder("Processed " + counter + "/" + numberOfTestcases));
		frame.setVisible(true);
	}

	@Test(dataProvider = "dp")
	public void f(HashMap<String, String> dictionary) throws InterruptedException {
		try {
			report.reportTest(dictionary);
			Class<?> cls = Class.forName(Configurations.ClassContainingTransactionScripts);
			Object object = cls.newInstance();
			Method method = cls.getMethod(dictionary.get("TestCaseDescription"),
					new HashMap<String, String>().getClass());
			boolean result = (boolean) method.invoke(object, dictionary);
			report.endTest();
			assertTrue(result);
		} catch (NoSuchMethodException e) {
			System.out.println("Transaction not implemented yet");
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			counter++;
			progressBar.setBorder(BorderFactory.createTitledBorder("Processed " + counter + "/" + numberOfTestcases));
			progressBar.setValue(counter);
			Thread.sleep(3000);
		}

	}

	@DataProvider
	public Object[] dp() throws InvalidFormatException, IOException {
		System.out.println(System.getProperty("filter"));
		ArrayList<String> arguments = new ArrayList<>(Arrays.asList(System.getProperty("filter").split(" ")));
		Object[] data = DataManager.read(arguments).toArray();
		numberOfTestcases = data.length;
		progressBar.setMaximum(numberOfTestcases);
		progressBar.setBorder(BorderFactory.createTitledBorder("Processed " + counter + "/" + numberOfTestcases));
		return data;

	}

	@AfterSuite
	public void afterSuit() {
		frame.dispose();
	}
}
