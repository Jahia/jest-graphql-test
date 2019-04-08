package org.jahia.modules.sdlGeneratorTools;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

public class NavigationButtonsTest extends GeneratorToolsRepository {

    @Test(alwaysRun = true)
    public void clearButtonTest(){
        goToGeneratorTools();
        clickClear();

        addType("jnt:article", "article");
        addProperty("jcr:description", "description");
        Assert.assertTrue(findByXpath("//span[contains(.,'description')]").isDisplayed(), "Failed to add property");

        clickClear();
        addType("jnt:article", "article");
        addProperty("jcr:description", "description");
        Assert.assertTrue(findByXpath("//span[contains(.,'description')]").isDisplayed(), "Failed to add property");

        clickNext();

        waitForElementToBeVisible(findByXpath("//p[contains(.,'Finders')]"));
        waitForElementToBeVisible(findByXpath(xpathAddNewFinder));
        addFinder("all", "article");
        clickBack();

    }


    @Test(alwaysRun = true)
    public void nextButtonTest(){
        goToGeneratorTools();
        clickClear();

        Assert.assertFalse(checkButtonEnabled(), "Next button should be disabled when there is no type");
        addType("jnt:article", "article");

        Assert.assertTrue(checkButtonEnabled(), "Next button should be enabled when type is added");

        clickOn(findElementsByXpath("//span[text()='metadata']/parent::div").get(0));
        waitForElementToBeVisible(findByXpath("//div[@id='form-dialog-title']"));
        clickDelete();

        Assert.assertFalse(checkButtonEnabled(), "Next button should be disabled when there is no property");

        addProperty("jcr:description", "description");
        Assert.assertTrue(checkButtonEnabled(), "Next button should be enabled when property is added");

        clickNext();
        Assert.assertTrue(checkButtonEnabled(), "Next button should be always enabled in Finders step");
    }


    private boolean checkButtonEnabled(){
        return noWaitingFindBy(By.xpath(xpathNextButton)).isEnabled();
    }

}
