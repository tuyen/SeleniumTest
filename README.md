# SeleniumTest
SeleniumTest is an automation testing tool based-on Selenium WebDriver. This tool has good support for integrating with CI

# Supporting for local testing

Steps for run your first test:

- Create a test script file and write your script. Referring to the Keyword.xlsx file for choosing keyword.

- Register your test script file with the test suite.

- Run the tool (All the test script have registered with test suite will be run)

- Check your report

- You're done.

This repository included basic sample for test script, test suite and test data. Please get your reference!

# Integrating with CI (Jenkins)
This tool also supported for DevOps. 

Jenkins administrator please configure a step to run this tool by add a command line (or Shell script on Linux). This tool will return 0 for passed all test scripts, if any script failed then -1.

For testers: working on your local test, after the test scripts are run successfully, commit test scripts to the repository => Jenkins will auto trigger and run the test!

# Wiki page

Please check Wiki page for detail documentation about this repository.