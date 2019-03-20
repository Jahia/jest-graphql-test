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
    public void propertiesListTest(String typeName, String nodeType, int listSize) {
        addType(typeName, nodeType);
        //findByXpath("//p[contains(.,'Node type')]/parent::ul/li[3]");

        clickOn(findByXpath("//span/p[contains(., 'Add a new property')]"));

        Assert.assertTrue(findByXpath("//button[contains(.,'Select and map property to type')]").isDisplayed(), "Failed to open property dialog box");
        Assert.assertTrue(findByXpath("//button[contains(.,'Select property')]").isDisplayed(), "Failed to open property dialog box");

        Assert.assertTrue(findByID("form-dialog-title").isDisplayed(), "Failed to display Add new property dialog box");

        clickOn(findByXpath("//div[contains(@class, 'MuiSelect-selectMenu-')]"));
        waitForElementToStopMoving(findByXpath("//ul[@role='listbox']"));
        //--check properties dropdown list
        Assert.assertEquals(findElementsByXpath("//ul[@role='listbox']/li").size(), listSize, "properties dropdown list is incorrect");
    }

    @Test(alwaysRun = true)
    public void addPropertyTest() {
        goToTools("jahia", "password");
        getDriver().navigate().to(getPath("/modules/sdl-generator-tools/tools/sdlGeneratorTools.jsp"));

        addType("jnt:article", "Article");

        clickOn(findByXpath("//span/p[contains(., 'Add a new property')]"));
        Assert.assertTrue(findByID("form-dialog-title").isDisplayed(), "Failed to display Add new property dialog box");

        clickOn(findByXpath("//div[contains(@aria-pressed,'false')]"));
        clickOn(findByXpath("//span[contains(., 'description')]"));

        Assert.assertTrue(findByXpath("//span[contains(., 'description')]").isDisplayed(), "Failed to select the property");
        Assert.assertTrue(findByXpath("//p[contains(., 'String')]").isDisplayed(), "Failed to find the property type");


        findByID("propertyName").sendKeys("article");

        clickSave();
        Assert.assertTrue(findByXpath("//span[contains(.,'article')]").isDisplayed(), "Failed to add property");

    }

    @Test(alwaysRun = true)
    public void mapPropertyToType(){

        goToTools("jahia", "password");
        getDriver().navigate().to(getPath("/modules/sdl-generator-tools/tools/sdlGeneratorTools.jsp"));

        addType("jnt:news", "News");

        addMapPropertyToType("Asset", "startPage", "page");

        clickSave();
        Assert.assertTrue(findByXpath("//span[contains(.,'page')]").isDisplayed(), "Failed to add property");

    }

    @Test(alwaysRun = true)
    public void validatePropertiesList(){
        goToTools("jahia", "password");
        getDriver().navigate().to(getPath("/modules/sdl-generator-tools/tools/sdlGeneratorTools.jsp"));

        addType("jnt:news", "News");

        //TODO




    }

    private void clickSave() {
        WebElement saveButton = waitForElementToBeClickable(findByXpath("//button[contains(.,'Save')]"));
        clickOn(saveButton);
    }


}
