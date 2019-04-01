package org.jahia.modules.sdlGeneratorTools;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class NavigationButtonsTest extends GeneratorToolsRepository {

    @BeforeMethod()
    private void goToGeneratorTools() {
        goToTools("jahia", "password");
        getDriver().navigate().to(getPath("/modules/sdl-generator-tools/tools/sdlGeneratorTools.jsp"));
    }


    @Test(alwaysRun = true)
    public void clearButtonTest(){
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
        waitForElementToBeVisible(findByXpath("//p[contains(.,'Add a finder')]"));
        addFinder("all", "article");
        clickBack();

    }


    @Test(alwaysRun = true, priority = 1)
    public void nextButtonTest(){
        //TODO

    }

    @Test(alwaysRun = true, priority = 2)
    public void backButtonTest(){
        //TODO
    }

}
