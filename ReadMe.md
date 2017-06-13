*********************************************************************************************************************************************************************************************************************************************
Serenity BDD Framework with customized features:
Author : Rakesh Choorikkadu
**********************************************************************************************************************************************************************************************************************************************

Custom Feature : 
	1. Used sure-fire plugin to run the Test cases in paralle
	2. Included Java log to print the output messages and service service which elimintae systerm.out practice
	3. Capable to view both Serenity Report and Cucumber JVM Report
	4. Lots of Helpers are included in the Framework to fast track the scripting and avoid duplication
	5. Front-end, API [Rest & SOAP] and Mobile Automation can be scripted in this single framework
	6. Run time JAX-WS Class generation using 'jaxws-maven-plugin' which is mainly for SOAP Service Automation
	7. Java multi-threadding concept used to run the single test case with multiple test data [Multiple browser]
	8. Framework also capable to read the Test Data from Excel file apart from the feature file
*********************************************************************************************************************************************************************************************************************************************
Commands :

1. To run the Sanity Test cases
	clean install -P domain-test -Dwebdriver.driver=chrome -Dwebdriver.base.url=https://yoururl.com/ -Dtest.tagnames=SanityTest -Dmaven.test.skip=false -Dtest.threadcount=1
2. To generate the JAX-WS class run time
	clean install generate-sources -Ppackage-test [Use this command in Jenkins]
	
**********************************************************************************************************************************************************************************************************************************************
Test Reports Path :
	Serenity Report - target/site/serenity/index.html
	HTML Report     - target/Destination/TestCaseName/index.html
    Json Report     - target/cucumber-report/TestCaseName.json	
	
**********************************************************************************************************************************************************************************************************************************************
Additional Configuration /Settings :
	Refer serenity.properties and src/test/resources/log4j.properties
	
**********************************************************************************************************************************************************************************************************************************************
