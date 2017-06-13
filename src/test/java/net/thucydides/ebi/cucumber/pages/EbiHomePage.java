package net.thucydides.ebi.cucumber.pages;

import net.serenitybdd.core.annotations.findby.FindBy;
import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.pages.WebElementFacade;
import net.thucydides.core.annotations.DefaultUrl;

/**
 * Created by rakeshnambiar on 31/12/2016.
 */

//@DefaultUrl("htttp://www.ebi.ac.uk")
public class EbiHomePage extends PageObject{

    @FindBy(id="query")
    WebElementFacade queryField;

    @FindBy(id="search_submit")
    WebElementFacade searchButton;

    public boolean verifyHomePageAvailable(){
        return queryField.isPresent();
    }

    public void enterQueryString(String text) throws Exception {
        try{
            if(text.length() > 0){
                queryField.waitUntilClickable();
                typeInto(queryField, text);
            }
        }catch (Exception e){
            throw new Exception("ERROR: While Performing entering the Query String");

        }
    }

    public void performSearch() throws Exception {
        try{
            searchButton.waitUntilClickable();
            searchButton.click();
        }catch (Exception e){
            throw new Exception("ERROR: While Clicks on the Search button");
        }
    }

}
