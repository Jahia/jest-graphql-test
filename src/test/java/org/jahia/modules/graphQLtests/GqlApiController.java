package org.jahia.modules.graphQLtests;
import org.jahia.modules.tests.core.ModuleTest;
import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;

/**
 * Created by parveer on 2019-01-15.
 */
public class GqlApiController extends ModuleTest {
    private static Logger logger = LoggerFactory.getLogger(GqlApiController.class);

    @BeforeSuite
    public void importDigitall(){
        getDriver().navigate().to(getPath("cms/admin/default/en/settings.webProjectSettings.html"));

        driver.switchTo().frame(findByXpath("//iframe[contains(@src,'/cms/adminframe/default/en/settings.webProjectSettings.html')]"));
        performSelectDropdownVisibleText(findByName("selectedPrepackagedSite"), "Digitall Prepackaged Demo Website");
        clickOn(findByName("importPrepackaged"));
        clickOn(findByXpath("//button[contains(.,'Import')]"));


        waitForElementToBeInvisible(By.xpath("//div[contains(., 'Work in progress, please wait...')]"));
        sleepMultipleTime(30);
        waitForWorkToEnd();

        Assert.assertTrue(findByXpath("//td/a[contains(.,'Digitall')]").isDisplayed(), "Failed to import Digitall site");
    }
}
