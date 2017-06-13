package net.thucydides.ebi.cucumber.runCukes;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runner.Runner;

@RunWith(Cucumber.class)
@CucumberOptions(
        format = {"pretty", "html:HTML_Report/cucumber/",
                "json:target/cucumber-report.json"},
        glue = {"net.thucydides.ebi.cucumber"}
)

@Category(Runner.class)
public class RunnerTest {


}