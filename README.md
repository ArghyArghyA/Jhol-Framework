# Jhol Framework

This framework is intended/focused for end to end web automation testing. However this framework can be utilized for testing applications on both Web and Desktop

## Getting Started

Clone the project to your local machine and import the project to your eclipse as a Maven project. You are good to go.

<details>
	<summary><b>managers.APIUtil</b> <br>this class has all the methods to carry out API/HTTP request related work</br></summary>
    <ul><li>under development</li></ul>
    <p></p>
</details>
<details>
	<summary><b>managers.DataManager</b> <br>this class has all the methods to read and write our data-sheet</br></summary>
    <ul>
    	<li><b>createFeatureFile(List&ltHashMap&ltString, String&gt&gt)</b> - creates a feature file in "src/test/resources/FunctionalTests" location from the data provided in the excel</li>
    	<li><b>readArrayList&ltString&gt)</b> - reads entire excel sheet with given filter. Filter is passed as arguments
    		<ul>
    			<li><i>Nothing</i> - no arguments is passed if we want to run all the tests marked as "Yes" in the Execute column in our datasheet</li>
    			<li><i>ALL</i> - "ALL" is passed if we want to run all the tests irrespective of the value in the Execute column
    			<li>space separated <i>TestCaseID</i> - "2 3" is passed if we want to run only 2nd and 3rd test case assuming we have 2 and 3 in our TestCaseID column</li>
    		</ul>
    	</li>
    	<li><b>write(HashMap&ltString, String&gt)</b> -writes all the test result back to data-sheet. If we need to update some values other than Status(Passed, Failed etc) we need to mention the column name in settings.Configurations file in appropriate place as well as during execution in the script we need to capture the value and pass the same as a key, value pair where key name must be same as column name as below: <br>
    	<code>put(OutPutFields.FirstResult.columnHeader, getText(Google.SearchResultText));</code>
    	</li>
    </ul>
    <p></p>
</details>
<details>
	<summary><b>managers.DriverUtil</b> <br>this class has all the methods to create the scripts. Our Script class must extend this class</br></summary>
    <ul><li><b>act</b> - object to access all the <code>org.openqa.selenium.interactions.Actions</code> class related methods. However no need to call <code>.build()</code> and <code>.perform()</code>. They are called internally</li>
    <li><b>aDriver</b> - object to access all the AutoItX4Java related methods</li>
    <li><b>longwait</b> - object to access Explicit Wait Methods. TimeOut is declared/can be changed in settings.Configurations file </li>
    <li><b>shortWait</b> - another object to access Explicit Wait Methods. However TimeOut is shorter than longWait. TimeOut is declared/can be changed in settings.Configurations file </li>
    <li><b>get(By)</b> - Returns the WebElement identified by the provided locator. If it fails to identify any element with the given identifier it returns null. Before attempting to find the element, waits for page to load and explicitly waits for element to be click-able.</li>
    <li><b>get(By, boolean, boolean)</b> Returns the WebElement identified by the provided locator. If it fails to identify any element with the given locator it returns null. Before attempting to find the element, waits for page to load if waitForPageLoad is true. Explicitly waits for element to be click-able if explicitWait is true.</li>
    <li><b>log(Status, String)</b> - Wrapper for logging statements. Logs both in console and Reporter. However depends on the Configurations.minimumLogLevel</li>
    <li><b>log(Status, String, Throwable)</b> - Wrapper for logging statements. Logs both in console and Reporter. Stacktrace for Throwable is included in the log. However depends on the Configurations.minimumLogLevel</li>
    <li><b>waitForPageToLoad()</b> - Returns true immediately after the page load completes within the page Load time out specified by Configurations.PageLoadTimeOut} in seconds. If the page load is not complete after the stipulated time returns false.</li>
    <li><b>append(String, String)</b> - Appends to already existing key, value pair. If the pair associated with the given key does not exist, a new key, value pair is generated</li>
    <li><b>assertTitle(String)</b> - Asserts if current title starts with the provided expectedTitle</li>
    <li><b>clear(By)</b> - Clears the input field identified by the provided By object by using Selenium WebElement.clear(). If it succeeds to clear returns true, false otherwise. Before attempting to find the element, waits for page to load and explicitly waits for element to be click-able by default.</li>
    <li><b>clear(By, boolean, boolean)</b> - Clears the input field identified by the provided By object by using Selenium WebElement.clear(). If it succeeds to clear returns true, false otherwise. Before attempting to do that, waits for page to load if waitForPageLoad is true. Explicitly waits for element to be click-able if  explicitWait  is true. </li>
    <li><b>clear(WebElement)</b> - Clears the input WebElement object by using Selenium WebElement.clear(). If it succeeds to clear returns true, false otherwise. Before attempting to do that, waits for page to load if waitForPageLoad is true. Explicitly waits for element to be click-able if  explicitWait  is true.</li>
    <li><b>click(By)</b> - Clicks on an WebElement identified by the provided By object. If it succeeds to click returns true, false otherwise. Before attempting to find the element, waits for page to load and explicitly waits for element to be click-able by default</li>
    <li><b>click(By, boolean, boolean)</b> - Clicks on an WebElement identified by the provided By object. If it succeeds to click returns true, false otherwise. Before attempting to find the element, waits for page to load if waitForPageLoad is true. Explicitly waits for element to be click-able if  explicitWait  is true. </li>
    <li><b>click(WebElement)</b> - Clicks on provided WebElement. If it succeeds to click returns true, false otherwise. </li>
    <li><b>closeAndSwitchToWindow(String, String, boolean)</b> - Switches to a window and try to closes it. Then again switches to anotherwindow.This switches to window with a match in driver.getTitle() or driver.getCurrentUrl(). Partial URL or Title is passed as String to be compared. Returns true or false depending on whether successful to switch to Final window or not. Waits for the driver window to check page loading is completed or not before comparing the Title/URL with the arguments if wait is provided as true </li>
    <li><b>fluentWait(Class<?>, String...)</b> - An sophisticated fluent wait method with provision of customization.</li>
    <li><b>generateRandomNumber(int)</b> - Generates a random number of the specified length</li>
    <li><b>get(String)</b> - Gets the value associated the given key from the data-sheet for the current test case</li>
    <li><b>get(String, String)</b> - Returns an array by splitting the value associated with the given key from the data-sheet by given separator</li>
    <li><b>get(String, String, int)</b> - Returns string at the given index from the array, generated by splitting the value associated with the given key from the data-sheet by given separator</li>
    <li><b>getAll(By)</b> - Returns List of WebElement(s) identified by the provided identifier by. Before attempting to find the elements, waits for page to load and explicitly waits for number of element to be non-zero by default </li>
    <li><b>getAll(By, boolean, boolean)</b> - Returns List of WebElement(s) identified by the provided identifier by. Before attempting to find the elements, waits for page to load if waitForPageLoad is true. Explicitly waits for number of element to be non-zero if  explicitWait  is true. </li>
    <li><b></b> - </li>
    <li><b></b> - </li>
    <li><b></b> - </li>
    <li><b></b> - </li>
    <li><b></b> - </li>
    <li><b></b> - </li>
    <li><b></b> - </li>
    <li><b></b> - </li>
    <li><b></b> - </li>
    <li><b></b> - </li>
    <li><b></b> - </li>
    <li><b></b> - </li>
    <li><b></b> - </li>
    <li><b></b> - </li>
    <li><b></b> - </li>
    <li><b></b> - </li>
    <li><b></b> - </li>
    <li><b></b> - </li>
    <li><b></b> - </li>
    <li><b></b> - </li>
    <li><b></b> - </li>
    <li><b></b> - </li>
    <li><b></b> - </li>
    <li><b></b> - </li>
    <li><b></b> - </li>
    <li><b></b> - </li>
    <li><b></b> - </li>
    </ul>
    <p></p>
</details>

### Prerequisites

You have to have Java and eclipse installed in your machine. 
In order to work with windows based application automation .Net Framework 4.1 or higher must be installed

If you are using AutoIt related methods then register the below mentioned DLL files
* /Framework/src/main/resources/AutoItX3_x64.dll
* /Framework/src/main/resources/AutoItX3.dll
	

## Running the tests

Explain how to run the automated tests for this system

### Break down into end to end tests

Explain what these tests test and why

```
Give an example
```

### And coding style tests

Explain what these tests test and why

```
Give an example
```

## Deployment

Add additional notes about how to deploy this on a live system

## Built With

* [Maven](https://maven.apache.org/) - Dependency Management 

## Authors

* **Arghya Roy**  - [ArghyArghyA](https://github.com/ArghyArghyA)

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

