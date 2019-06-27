package managers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.testng.IReporter;
import org.testng.ISuite;
import org.testng.xml.XmlSuite;

public class TestNGReporter implements IReporter {

	@Override
	public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
		try {
			File file = new File(
					"./src/test/resources/Results/TestNg Report/" + Reporter.getInstance().timeStamp + ".html");
			FileWriter fileWriter = new FileWriter(file);
			fileWriter.write(2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
