package net.thucydides.ebi.cucumber.framework.hooks;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.gl.E;
import cucumber.api.junit.Cucumber;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.core.pages.PageObject;
import net.thucydides.core.annotations.Managed;
import net.thucydides.core.annotations.Screenshots;
import net.thucydides.core.model.TakeScreenshots;
import net.thucydides.core.util.EnvironmentVariables;
import net.thucydides.ebi.cucumber.framework.helpers.FileHelper;
import net.thucydides.ebi.cucumber.framework.helpers.PropertyHelper;
import org.apache.commons.io.FileUtils;
import org.junit.runner.RunWith;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by rakeshnbr on 05/12/2016.
 */
public class initialization extends PageObject{
    public static String downLoadDirectory;
    public static ScenarioHook scenarioHook;
    public static boolean browserTest = false;
    EnvironmentVariables environmentVariables;


    @Managed
    public WebDriver webdriver = getDriver();

    @Before("~@NoBrowser")
    public void prepareTest() throws Exception {
        try{
            browserTest = true;
            if (!PropertyHelper.getProperty("webdriver.driver").equalsIgnoreCase("appium")) {
                getDriver().manage().window().maximize();
                getDriver().manage().window().setSize(new Dimension(1920,1080));
            }
        }catch (Exception e){
            throw new Exception("ERROR: While Preparing for the Test");
        }
    }

    public static byte[] extractBytes (String ImageName) throws IOException {
        byte[] bytes = new byte[0];
        try {
            RandomAccessFile f = new RandomAccessFile(ImageName, "r");
            bytes = new byte[(int) f.length()];
            f.read(bytes);
            f.close();
            return bytes;
        }catch (Exception e){
            return bytes;
        }
    }
}
