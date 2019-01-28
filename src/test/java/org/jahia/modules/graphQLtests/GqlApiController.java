package org.jahia.modules.graphQLtests;

import org.jahia.modules.tests.core.ModuleTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import java.util.List;

/**
 * Created by parveer on 2019-01-15.
 */
public class GqlApiController extends ModuleTest {
    private static Logger logger = LoggerFactory.getLogger(GqlApiController.class);

    @BeforeSuite
    public void importDigitall(){
        getDriver().navigate().to(getPath("/cms/admin/default/en/settings.webProjectSettings.html"));
        switchToDXAdminFrame();

        WebElement site = findByXpath("//a[contains(.,'Digitall')]");
        if(site == null){
            performSelectDropdownVisibleText(findByName("selectedPrepackagedSite"), "Digitall Prepackaged Demo Website");
            clickOn(findByName("importPrepackaged"));
            clickOn(findByXpath("//button[contains(.,'Import')]"));


            waitForElementToBeInvisible(By.xpath("//div[contains(., 'Work in progress, please wait...')]"));
            sleepMultipleTime(30);
            waitForWorkToEnd();

            Assert.assertNotNull(waitForElementToBeVisible(findByXpath("//a[contains(.,'Digitall')]")), "Failed to import Digitall site");
        }

        else {
            Assert.assertNotNull(waitForElementToBeVisible(findByXpath("//a[contains(.,'Digitall')]")), "Failed to import Digitall site");
            logger.info("Site already exists");
        }
    }

    @AfterSuite
    public void deleteSite(){
        getDriver().navigate().to(getPath("/cms/admin/default/en/settings.webProjectSettings.html"));

        switchToDXAdminFrame();

        List<WebElement> sites = findElementsByXpath("//a[@title='Delete']");
        for(WebElement deleteButton:sites) {
            int i=0;
            clickOn(sites.get(i));

            clickOn(findByXpath("//button[contains(., 'Delete')]"));
            waitForElementToBeVisible(findByXpath("//div[contains(., 'Work in progress, please wait...')]"));
            waitForWorkToEnd();
        }

    }

    private void switchToDXAdminFrame() {
        driver.switchTo().frame(findByXpath("//iframe[contains(@src,'/cms/adminframe/default/en/settings.webProjectSettings.html')]"));
    }

}
