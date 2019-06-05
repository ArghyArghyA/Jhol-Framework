# Jhol Framework

This framework is intended/focused for end to end web automation testing. However this framework can be utilized for testing applications on both Web and Desktop

## Getting Started
Clone the project to your local machine and import the project to your eclipse as a Maven project. You are good to go.

## Architecture
Expand the project to know more about it
<details style="padding: 15px;">
    <summary>src</summary>
    <details style="padding: 15px;">
        <summary>main</summary>
        <details style="padding: 15px;">
            <summary>java</summary>
            <details style="padding: 15px;">
                <summary>managers</summary>
                <details style="padding: 15px;">
                    <summary><b>APIUtil</b> - this class has all the methods to carry out API/HTTP request related work</summary>
                    <ul>
                        <li>under development</li>
                    </ul>
                </details>
                <details style="padding: 15px;">
                    <summary><b>DataManager</b> - this class has all the methods to read and write our data-sheet</summary>
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
                </details>
                <details style="padding: 15px;">
                    <summary><b>DriverUtil</b> - this class has all the methods to create the scripts. Our Script class must extend this class</summary>
                    <ul>
                        <li><b>act</b> - object to access all the <code>org.openqa.selenium.interactions.Actions</code> class related methods. However no need to call <code>.build()</code> and <code>.perform()</code>. They are called internally</li>
                        <li><b>aDriver</b> - object to access all the AutoItX4Java related methods</li>
                        <li><b>longwait</b> - object to access Explicit Wait Methods. TimeOut is declared/can be changed in settings.Configurations file </li>
                        <li><b>shortWait</b> - another object to access Explicit Wait Methods. However TimeOut is shorter than longWait. TimeOut is declared/can be changed in settings.Configurations file</li>
                        <li><b>get(By)</b> - Returns the WebElement identified by the provided locator. If it fails to identify any element with the given identifier it returns null. Before attempting to find the element, waits for page to load and explicitly waits for element to be click-able.</li>
                        <li><b>get(By, boolean, boolean)</b> Returns the WebElement identified by the provided locator. If it fails to identify any element with the given locator it returns null. Before attempting to find the element, waits for page to load if waitForPageLoad is true. Explicitly waits for element to be click-able if explicitWait is true.</li>
                        <li><b>log(Status, String)</b> - Wrapper for logging statements. Logs both in console and Reporter. However depends on the Configurations.minimumLogLevel</li>
                        <li><b>log(Status, String, Throwable)</b> - Wrapper for logging statements. Logs both in console and Reporter. Stacktrace for Throwable is included in the log. However depends on the Configurations.minimumLogLevel</li>
                        <li><b>waitForPageToLoad()</b> - Returns true immediately after the page load completes within the page Load time out specified by Configurations.PageLoadTimeOut} in seconds. If the page load is not complete after the stipulated time returns false.</li>
                        <li><b>append(String, String)</b> - Appends to already existing key, value pair. If the pair associated with the given key does not exist, a new key, value pair is generated</li>
                        <li><b>assertTitle(String)</b> - Asserts if current title starts with the provided expectedTitle</li>
                        <li><b>clear(By)</b> - Clears the input field identified by the provided By object by using Selenium WebElement.clear(). If it succeeds to clear returns true, false otherwise. Before attempting to find the element, waits for page to load and explicitly waits for element to be click-able by default.</li>
                        <li><b>clear(By, boolean, boolean)</b> - Clears the input field identified by the provided By object by using Selenium WebElement.clear(). If it succeeds to clear returns true, false otherwise. Before attempting to do that, waits for page to load if waitForPageLoad is true. Explicitly waits for element to be click-able if explicitWait is true. </li>
                        <li><b>clear(WebElement)</b> - Clears the input WebElement object by using Selenium WebElement.clear(). If it succeeds to clear returns true, false otherwise. Before attempting to do that, waits for page to load if waitForPageLoad is true. Explicitly waits for element to be click-able if explicitWait is true.</li>
                        <li><b>click(By)</b> - Clicks on an WebElement identified by the provided By object. If it succeeds to click returns true, false otherwise. Before attempting to find the element, waits for page to load and explicitly waits for element to be click-able by default</li>
                        <li><b>click(By, boolean, boolean)</b> - Clicks on an WebElement identified by the provided By object. If it succeeds to click returns true, false otherwise. Before attempting to find the element, waits for page to load if waitForPageLoad is true. Explicitly waits for element to be click-able if explicitWait is true. </li>
                        <li><b>click(WebElement)</b> - Clicks on provided WebElement. If it succeeds to click returns true, false otherwise. </li>
                        <li><b>closeAndSwitchToWindow(String, String, boolean)</b> - Switches to a window and try to closes it. Then again switches to anotherwindow.This switches to window with a match in driver.getTitle() or driver.getCurrentUrl(). Partial URL or Title is passed as String to be compared. Returns true or false depending on whether successful to switch to Final window or not. Waits for the driver window to check page loading is completed or not before comparing the Title/URL with the arguments if wait is provided as true </li>
                        <li><b>fluentWait(Class&lt?&gt, String...)</b> - An sophisticated fluent wait method with provision of customization.</li>
                        <li><b>generateRandomNumber(int)</b> - Generates a random number of the specified length</li>
                        <li><b>get(String)</b> - Gets the value associated the given key from the data-sheet for the current test case</li>
                        <li><b>get(String, String)</b> - Returns an array by splitting the value associated with the given key from the data-sheet by given separator</li>
                        <li><b>get(String, String, int)</b> - Returns string at the given index from the array, generated by splitting the value associated with the given key from the data-sheet by given separator</li>
                        <li><b>getAll(By)</b> - Returns List of WebElement(s) identified by the provided identifier by. Before attempting to find the elements, waits for page to load and explicitly waits for number of element to be non-zero by default </li>
                        <li><b>getAll(By, boolean, boolean)</b> - Returns List of WebElement(s) identified by the provided identifier by. Before attempting to find the elements, waits for page to load if waitForPageLoad is true. Explicitly waits for number of element to be non-zero if explicitWait is true. </li>
                        <li><b>getAttribute(By, String)</b> - Returns the attribute-value for the Element identified by the By object. Before attempting that it waits for the page to load completely</li>
                        <li><b>getAttribute(WebElement, String)</b> - Returns the attribute-value from the Element.</li>
                        <li><b>getText(By)</b> - Returns visible text for the Element identified by the By object. Before attempting that it waits for the page to load completely</li>
                        <li><b>getText(WebElement)</b> - Returns visible text from the Element.</li>
                        <li><b>hover(By)</b> - Mouse hovers over the element identified by the by object. Returns true if succeeds otherwise false. Before attempting to do that, waits for page to load Explicitly waits for element to be click-able by default</li>
                        <li><b>hover(By, boolean, boolean)</b> - Mouse hovers over the element identified by the by object. Returns true if succeeds otherwise false. Before attempting to do that, waits for page to load if waitForPageLoad is true. Explicitly waits for element to be click-able if explicitWait is true. </li>
                        <li><b>hover(WebElement)</b> - Mouse hovers over WebElement. Returns true if succeeds otherwise false</li>
                        <li><b>javascript(String)</b> - Injects java-script into the current browser window. Before attempting to do that, waits for page to load by default. It returns whatever returned by the java-script. </li>
                        <li><b>javascript(String, boolean)</b> - Injects java-script into the current browser window. Before attempting to do that, waits for page to load if waitForPageLoad is true. It returns whatever returned by the java-script. </li>
                        <li><b>launchBrowser()</b> - launches browser specified in the data-sheet and loads the URL specified in Configurations.URL</li>
                        <li><b>navigateTo(String)</b> - Navigates to certain URL</li>
                        <li><b>put(String, String)</b> - Puts the value at target element. If it already has a value the new value replaces the old value</li>
                        <li><b>select(By)</b> - Selects a random option from the drop-down identified by the provided By object. If it succeeds to select returns true, false otherwise. Before attempting to do that, waits for page to load and Explicitly waits for element to be click-able. </li>
                        <li><b>select(By, boolean, boolean)</b> - Selects a random option from the drop-down identified by the provided By object. If it succeeds to select returns true, false otherwise. Before attempting to do that, waits for page to load if waitForPageLoad is true. Explicitly waits for element to be click-able if explicitWait is true. </li>
                        <li><b>select(WebElement)</b> - Selects a random option from the drop-down identified by the provided WebElement object. If it succeeds to select returns true, false otherwise.</li>
                        <li><b>selectByIndex(By, int)</b> - Selects the specified index-th option(zero indexed) from the drop-down identified by the provided By object. If it succeeds to select returns true, false otherwise. Before attempting to find the element, waits for page to load and explicitly waits for element to be click-able by default. </li>
                        <li><b>selectByIndex(By, int, boolean, boolean)</b> - Selects the specified text from the dropdown identified by the provided By object. If it succeeds to select returns true, false otherwise. Before attempting to do that, waits for page to load if waitForPageLoad is true. Explicitly waits for element to be click-able if explicitWait is true. </li>
                        <li><b>selectByIndex(WebElement, int)</b> - Selects the specified index-th option(zero indexed) from the drop-down specified by the provided WebElement object. If it succeeds to select returns true, false otherwise.</li>
                        <li><b>selectByValue(By, String)</b> - Selects the specified text from the dropdown identified by the provided By object by using Selenium Select.selectByValue(String text). If it succeeds to select returns true, false otherwise. Before attempting to find the element, waits for page to load and explicitly waits for element to be click-able by default</li>
                        <li><b>selectByValue(By, String, boolean, boolean)</b> - Selects the specified text from the drop-down identified by the provided By object by using Selenium Select.selectByValue(String text). If it succeeds to select returns true, false otherwise. Before attempting to do that, waits for page to load if waitForPageLoad is true. Explicitly waits for element to be click-able if explicitWait is true.</li>
                        <li><b>selectByValue(WebElement, String)</b> - Selects the specified text from the drop-down WebElement object by using Selenium Select.selectByValue(String text). If it succeeds to select returns true, false otherwise.</li>
                        <li><b>selectByVisibleText(By, String)</b> - Selects the specified text from the drop-down identified by the provided By object by using Selenium Select.selectByVisibleText(String text). If it succeeds to select returns true, false otherwise. Before attempting to find the element, waits for page to load and explicitly waits for element to be click-able by default.</li>
                        <li><b>selectByVisibleText(By, String, boolean, boolean)</b> - Selects the specified text from the dropdown identified by the provided By object by using Selenium Select.selectByVisibleText(String text). If it succeeds to select returns true, false otherwise. Before attempting to do that, waits for page to load if waitForPageLoad is true. Explicitly waits for element to be click-able if explicitWait is true.</li>
                        <li><b>selectByVisibleText(WebElement, String)</b> - Selects the specified text from the drop-down WebElement object by using Selenium Select.selectByVisibleText(String text). If it succeeds to select returns true, false otherwise.</li>
                        <li><b>selectComboByIndex(By, int)</b> - Application specific drop-down selection methods. Can be used for combo box or any other cases where standard methods does not work. May need to be revised/Overridden depending on the application</li>
                        <li><b>selectComboByIndex(By, int, boolean, boolean)</b> - Application specific dropdown selection methods. Can be used for combo box or any other cases where standard methods does not work. May need to be revised/Overridden depending on the application</li>
                        <li><b>selectComboByIndex(WebElement, int)</b> - Application specific drop-down selection methods. Can be used for combo box or any other cases where standard methods does not work. May need to be revised/Overridden depending on the application</li>
                        <li><b>selectComboByVisibleText(By, String)</b> - Application specific dropdown selection methods. Can be used for combo box or any other cases where standard methods does not work. May need to be revised/Overridden depending on the application</li>
                        <li><b>selectComboByVisibleText(By, String, boolean, boolean, boolean)</b> - Application specific dropdown selection methods. Can be used for combo box or any other cases where standard methods does not work. May need to be revised/Overridden depending on the application</li>
                        <li><b>selectComboByVisibleText(WebElement, String)</b> - Application specific dropdown selection methods. Can be used for combo box or any other cases where standard methods does not work. May need to be revised/Overridden depending on the application</li>
                        <li><b>selectComboByVisibleText(WebElement, String, boolean)</b> - Application specific dropdown selection methods. Can be used for combo box or any other cases where standard methods does not work. May need to be revised/Overridden depending on the application</li>
                        <li><b>sendkeys(By)</b> - Types random text (minimum length 3, maximum length 10) into the input field identified by the provided By object by using Selenium WebElement.sendKeys(String text). If it succeeds to type returns true, false otherwise. Before attempting to find the element, waits for page to load and explicitly waits for element to be click-able by default.</li>
                        <li><b>sendkeys(By, boolean, boolean)</b> - Types random text (minimum length 3, maximum length 10) into the input field identified by the provided By object by using Selenium WebElement.sendKeys(String text). If it succeeds to type returns true, false otherwise. Before attempting to do that, waits for page to load if waitForPageLoad is true. Explicitly waits for element to be click-able if explicitWait is true. </li>
                        <li><b>sendkeys(By, String)</b> - Types the specified text into the input field identified by the provided By object by using Selenium WebElement.sendKeys(String text). If it succeeds to type returns true, false otherwise. Before attempting to find the element, waits for page to load and explicitly waits for element to be click-able by default. </li>
                        <li><b>sendkeys(By, String, boolean, boolean)</b> - Types the specified text into the input field identified by the provided By object by using Selenium WebElement.sendKeys(String text). If it succeeds to type returns true, false otherwise. Before attempting to do that, waits for page to load if waitForPageLoad is true. Explicitly waits for element to be click-able if explicitWait is true. </li>
                        <li><b>sendkeys(WebElement)</b> - Types random text (minimum length 3, maximum length 10) into the input field identified by the provided WebElement object by using Selenium WebElement.sendKeys(String text). If it succeeds to type returns true, false otherwise</li>
                        <li><b>sendkeys(WebElement, String)</b> - Types the specified text into the input field WebElement object by using Selenium WebElement.sendKeys(String text). If it succeeds to type returns true, false otherwise.</li>
                        <li><b>stopWiniumServer()</b> - stops winnium server</li>
                        <li><b>switchToDefaultContent()</b> - Switches to DOM. Returns true or false depending on whether successful to switch to root DOM or not.</li>
                        <li><b>switchToFrame()</b> - Switches to first iFrame available in the DOM. Returns true or false depending on whether successful to switch to Frame or not. Waits for the driver window to check page loading is completed as well waits for Frame element to appear on DOM if not found immediately for a maximum PageLoadTimeOut </li>
                        <li><b>switchToFrame(By)</b> - Switches to Frame identified by the provided By object. Returns true or false depending on whether successful to switch to Frame or not. Waits for the driver window to check page loading is completed as well as waits for Frame element to appear on DOM if not found immediately for a maximum PageLoadTimeOut</li>
                        <li><b>switchToWindow(String)</b> - Switches to window with a match in driver.getTitle() or driver.getCurrentUrl(). Partial URL or Title is passed as String to be compared. Returns true or false depending on whether successful to switch to window or not. Waits for the driver window to check page loading is completed or not before comparing the Title/URL with the arguments by default.</li>
                        <li><b>switchToWindow(String, boolean)</b> - Switches to window with a match in driver.getTitle() or driver.getCurrentUrl(). Partial URL or Title is passed as String to be compared. Returns true or false depending on whether successful to switch to window or not. Waits for the driver window to check page loading is completed or not before comparing the Title/URL with the arguments if wait is provided as true </li>
                        <li><b>switchToWindow(String, String)</b> - Switches the control of windows execution to a desktop windows matched by the String attributeValue and String attributeName. A complete match with attributeValue is required and only immediate windows available on desktop are compared. Must be used before winiumDriver methods(wClick and wSendkeys)</li>
                        <li><b>switchToWindow(String, String, boolean, boolean)</b> - Switches the control of windows execution to a desktop windows matched by the String attributeValue and String attributeName. If partialMatch is true values are compared by using contains() method, otherwise complete match is required.If searchEntireTree is true entire tree starting from desktop is checked; otherwise only immediate windows available on desktop are compared Must be used before winiumDriver methods(wClick and wSendkeys)</li>
                        <li><b>waitFor(By)</b> - Waits for the element identified by the given By object to be available in DOM. Returns true if the element is found, false otherwise. Should be used where Explicit Wait is not working. Maximum timeout can be configured by changing Configurations.PageLoadTimeOut in seconds. </li>
                        <li><b>wClick(By)</b> - Clicks on an Windows Element identified by the provided By object. If it succeeds to click returns true, false otherwise. Before calling this method one must attach to a window by calling switchToWindow(String attributeValue, String attributeName) or switchToWindow(String attributeValue, String attributeName, boolean partialMatch,boolean searchEntireTree) </li>
                        <li><b>wSendkeys(By, String)</b> - Types into an Windows Element identified by the provided By object. If it succeeds to click returns true, false otherwise. Before calling this method one must attach to a window by calling switchToWindow(String attributeValue, String attributeName) or switchToWindow(String attributeValue, String attributeName, boolean partialMatch,boolean searchEntireTree) </li>
                    </ul>
                </details>
                <details style="padding: 15px;">
                    <summary><b>Reporter</b> - this class has all the methods report to reporter. This is by implementation a 'Singleton' class and therefore must be accessed by using <code>Reporter report = Reporter.getInstance();</code>. Throughout the execution only one instance is allowed</summary>
                    <ul>
                        <li><b>getInstance()</b> - returns the instance for the Reporter class. If there is no instance, it generates one</li>
                        <li><b>endTest()</b> - ends current test and save the report at local directory</li>
                        <li><b>reportEvent(Status, String)</b> - reports the specified event along with the status to the report</li>
                        <li><b>reportEvent(Status, String, String)</b> - reports the specified event along with the status to the report</li>
                        <li><b>reportTest(HashMap &lt;String, String&gt;)</b> - starts a new test</li>
                    </ul>
                </details>
            </details>
        </details>
    </details>
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

## Built With

* [Maven](https://maven.apache.org/) - Dependency Management 

## Authors

* **Arghya Roy**  - [ArghyArghyA](https://github.com/ArghyArghyA)

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

