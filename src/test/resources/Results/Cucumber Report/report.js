$(document).ready(function() {var formatter = new CucumberHTML.DOMFormatter($('.cucumber-report'));formatter.uri("file:src/test/resources/FunctionalTests/test.feature");
formatter.feature({
  "name": "test",
  "description": "",
  "keyword": "Feature"
});
formatter.scenario({
  "name": "Google : 1",
  "description": "",
  "keyword": "Scenario"
});
formatter.step({
  "name": "Google",
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
        "1",
        "2",
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
  "error_message": "java.lang.AssertionError\r\n\tat org.junit.Assert.fail(Assert.java:86)\r\n\tat org.junit.Assert.assertTrue(Assert.java:41)\r\n\tat org.junit.Assert.assertTrue(Assert.java:52)\r\n\tat stepDefinition.Glue.Google(Glue.java:25)\r\n\tat ✽.Google(file:src/test/resources/FunctionalTests/test.feature:4)\r\n",
  "status": "failed"
});
formatter.scenario({
  "name": "Google : 2",
  "description": "",
  "keyword": "Scenario"
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
  "status": "passed"
});
formatter.scenario({
  "name": "Google : 3",
  "description": "",
  "keyword": "Scenario"
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
        "Save Search Result",
        "sheet",
        "TestCaseDescription"
      ]
    },
    {
      "cells": [
        "Yes",
        "Chrome",
        "3",
        "Another Jhol",
        "4",
        "Yes",
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
  "error_message": "java.lang.AssertionError\r\n\tat org.junit.Assert.fail(Assert.java:86)\r\n\tat org.junit.Assert.assertTrue(Assert.java:41)\r\n\tat org.junit.Assert.assertTrue(Assert.java:52)\r\n\tat stepDefinition.Glue.Google(Glue.java:25)\r\n\tat ✽.Google(file:src/test/resources/FunctionalTests/test.feature:14)\r\n",
  "status": "failed"
});
formatter.scenario({
  "name": "windows : 4",
  "description": "",
  "keyword": "Scenario"
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
});