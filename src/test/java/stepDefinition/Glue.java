package stepDefinition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;

import cucumber.api.java.en.Given;
import io.cucumber.datatable.DataTable;
import managers.Reporter;
import scripts.transaction.Transactions;

public class Glue {
	Reporter reporter = Reporter.getInstance();
	
	@Given("Google")
	public void Google(DataTable dataTable) throws Exception
	{
		
		try {
			List<Map<String, String>> data = dataTable.asMaps(String.class, String.class);
			HashMap<String, String> dictionary = new HashMap<>(data.get(0));
			reporter.reportTest(dictionary);
			Assert.assertTrue(Transactions.Google(dictionary));
		} catch (Exception e) {
			e.printStackTrace();
		} finally
		{
			reporter.endTest();
		}
		
	}

}
