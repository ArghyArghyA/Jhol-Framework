Feature: test

Scenario: Google : 1
Given Google
|Execute|browser|TestCaseID|index|sheet|TestCaseDescription|
|No|Chrome|1|2|Google|Google|

Scenario: Google : 2
Given Google
|Execute|browser|TestCaseID|Search Keyword|index|sheet|TestCaseDescription|
|No|Chrome|2|jhol|3|Google|Google|

Scenario: Google : 3
Given Google
|Execute|browser|TestCaseID|Search Keyword|index|Save Search Result|sheet|TestCaseDescription|
|Yes|Chrome|3|Another Jhol|4|Yes|Google|Google|

Scenario: windows : 4
Given windows
|Execute|browser|TestCaseID|index|sheet|TestCaseDescription|
|No|Chrome|4|5|Google|windows|
