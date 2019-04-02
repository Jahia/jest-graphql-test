package org.jahia.modules.sdlGeneratorTools;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class PropertiesTest extends GeneratorToolsRepository {

    @BeforeMethod()
    private void goToGeneratorTools() {
        goToTools("jahia", "password");
        getDriver().navigate().to(getPath("/modules/sdl-generator-tools/tools/sdlGeneratorTools.jsp"));
    }

    @Test(dataProvider = "propertiesList", alwaysRun = true)
    public void checkPropertiesListTest(String nodeType, String typeName, int listSize) {
        addType(nodeType, typeName);
        //findByXpath("//p[contains(.,'Node type')]/parent::ul/li[3]");

        clickOn(findByXpath("//span/p[contains(., 'Add new property')]"));

        Assert.assertTrue(findByXpath("//button[contains(.,'Select and map property to type')]").isDisplayed(), "Failed to open property dialog box");
        Assert.assertTrue(findByXpath("//button[contains(.,'Select property')]").isDisplayed(), "Failed to open property dialog box");
        clickOn(findByXpath("//p[contains(.,'Select property')]/parent::span/parent::button"));

        checkAddPropertyDialog();

        clickOn(findByXpath("//div[contains(@aria-haspopup,'true')]"));
        waitForElementToStopMoving(findByXpath("//ul[@role='listbox']"));
        //--check properties dropdown list
        Assert.assertEquals(findElementsByXpath("//ul[@role='listbox']/li").size(), listSize, "properties dropdown list is incorrect");
    }

    @Test(alwaysRun = true, priority = 1)
    public void addPropertyTest() {
        clickClear();

        addType("jnt:article", "article");

        addProperty("jcr:description", "description");

        Assert.assertTrue(findByXpath("//span[contains(.,'description')]").isDisplayed(), "Failed to add property");

    }

    @Test(alwaysRun = true, priority = 2)
    public void mapPropertyToType(){
        clickClear();

        addType("jdnt:allStories", "allNews");

        addMapPropertyToType("Asset", "startPage", "page");

        clickAdd();
        Assert.assertTrue(findByXpath("//span[contains(.,'page')]").isDisplayed(), "Failed to add property");

    }

    @Test(alwaysRun = true, priority = 3)
    public void editPropertiesTest(){

        goToGeneratorTools();
        clickClear();
        addType("jnt:news", "newsEntry");

        Assert.assertTrue(findByXpath("//span[contains(@class,'Mui')][contains(.,'metadata')]").isDisplayed(), "Failed to add metadata property by default");

        clickOn(findByXpath("//p[contains(.,'Add new property')]/parent::span/parent::button"));

        waitForElementToBeClickable(findByXpath("//p[contains(.,'Select property')]/parent::span/parent::button"));
        clickOn(findByXpath("//p[contains(.,'Select property')]/parent::span/parent::button"));

        clickOn(findByXpath("//div[contains(@aria-haspopup,'true')]"));

        clickOn(findByXpath("//span[contains(@class,'Mui')][contains(.,'description')]"));
        Assert.assertTrue(findByXpath("//span[contains(.,'description')]/parent::div").isDisplayed(), "Failed to select property type");
        clickAdd();

        Assert.assertTrue(findByXpath("//span[contains(@class,'Mui')][contains(.,'description')]").isDisplayed(), "Failed to add property");
        clickOn(findByXpath("//span[contains(@class,'Mui')][contains(.,'description')]"));

        WebElement propertyInput = findByXpath("//input[@id='propertyName']");
        propertyInput.clear();
        propertyInput.sendKeys("descEdited");

        clickUpdate();

        Assert.assertTrue(findByXpath("//span[contains(@class,'Mui')][contains(.,'descEdited')]").isDisplayed(), "Failed to edit/update property");


    }


}
