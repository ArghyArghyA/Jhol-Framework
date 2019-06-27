$(document).ready(function() {var formatter = new CucumberHTML.DOMFormatter($('.cucumber-report'));formatter.uri("file:src/test/resources/FunctionalTests/test.feature");
formatter.feature({
  "name": "test",
  "description": "",
  "keyword": "Feature"
});
formatter.scenario({
  "name": "Google : 2",
  "description": "",
  "keyword": "Scenario"
});
formatter.before({
  "status": "skipped"
});
formatter.step({
  "name": "Google",
  "rows": [
    {
      "cells": [
        "Execute",
        "browser",
        "TestCaseID",
        "Search Keyword",
        "index",
        "sheet",
        "TestCaseDescription"
      ]
    },
    {
      "cells": [
        "No",
        "Chrome",
        "2",
        "jhol",
        "3",
        "Google",
        "Google"
      ]
    }
  ],
  "keyword": "Given "
});
formatter.match({
  "location": "Glue.Google(DataTable)"
});
formatter.result({
  "status": "skipped"
});
formatter.after({
  "status": "skipped"
});
formatter.scenario({
  "name": "windows : 4",
  "description": "",
  "keyword": "Scenario"
});
formatter.before({
  "status": "skipped"
});
formatter.step({
  "name": "windows",
  "rows": [
    {
      "cells": [
        "Execute",
        "browser",
        "TestCaseID",
        "index",
        "sheet",
        "TestCaseDescription"
      ]
    },
    {
      "cells": [
        "No",
        "Chrome",
        "4",
        "5",
        "Google",
        "windows"
      ]
    }
  ],
  "keyword": "Given "
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
formatter.after({
  "status": "skipped"
});
});