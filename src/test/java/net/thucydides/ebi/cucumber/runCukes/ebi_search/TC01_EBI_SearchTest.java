package net.thucydides.ebi.cucumber.runCukes.ebi_search;

import cucumber.api.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import net.thucydides.ebi.cucumber.tags.SanityTest;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;


@RunWith(CucumberWithSerenity.class)
@CucumberOptions(tags = {"@TC01_EBI_SearchTest"},
        format = {"pretty", "html:target/Destination/TC01_EBI_SearchTest",
                "json:target/cucumber-report/TC01_EBI_SearchTest.json"},
        features= {"src/test/resources/features/search/search_by_word.feature"},
        glue = {"net.thucydides.ebi.cucumber"})

@Category({SanityTest.class})
public class TC01_EBI_SearchTest {
}
