package runners;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;

@RunWith(cucumber.api.junit.Cucumber.class)
@CucumberOptions(features = "src/test/resources/FunctionalTests", glue = { "stepDefinition" }, plugin = { "pretty",
		"html:src/test/resources/Results/Cucumber Report" })
public class Cucumber {

}
