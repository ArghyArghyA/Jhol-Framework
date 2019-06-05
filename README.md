# Jhol Framework

This framework is intended/focused for end to end web automation testing. However this framework can be utilized for testing applications on both Web and Desktop

## Getting Started

Clone the project to your local machine and import the project to your eclipse as a Maven project. You are good to go.

<details>
	<summary><b>managers.APIUtil</b> <br>this class has all the methods to carry out API/HTTP request related work</br></summary>
    <li>under development</li>
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

