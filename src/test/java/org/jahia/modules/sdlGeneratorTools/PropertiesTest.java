package org.jahia.modules.sdlGeneratorTools;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

public class PropertiesTest extends GeneratorToolsRepository {

    @BeforeMethod()
    private void goToGeneratorTools() {
        goToTools("jahia", "password");
        getDriver().navigate().to(getPath("/modules/sdl-generator-tools/tools/sdlGeneratorTools.jsp"));
    }

    @Test(dataProvider = "propertiesList", alwaysRun = true)
    public void propertiesListTest(String typeName, String nodeType, int listSize) {
        addType(nodeType, typeName);
        //findByXpath("//p[contains(.,'Node type')]/parent::ul/li[3]");

        clickOn(findByXpath("//span/p[contains(., 'Add new property')]"));

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

        addType("jnt:article", "articleTitleAndIntroduction");

        addProperty("jcr:description", "desc");

        Assert.assertTrue(findByXpath("//span[contains(.,'desc')]").isDisplayed(), "Failed to add property");

    }

    @Test(alwaysRun = true)
    public void mapPropertyToType(){

        addType("jdnt:allStories", "News");

        addMapPropertyToType("Asset", "startPage", "page");

        clickSave();
        Assert.assertTrue(findByXpath("//span[contains(.,'page')]").isDisplayed(), "Failed to add property");

    }

    @Test(dataProvider = "propertyList", alwaysRun = true)
    public void validatePropertiesList(String property, String propertyName){

        addType("jnt:news", "newsEntry");

        clickOn(findByXpath("//span/p[contains(., 'Add new property')]"));

        waitForElementToBeClickable(findByXpath("//p[contains(.,'Select property')]/parent::span/parent::button"));
        clickOn(findByXpath("//p[contains(.,'Select property')]/parent::span/parent::button"));
        shortSleep();

        checkAddPropertyDialog();

        clickOn(findByXpath("//div[contains(@class, 'MuiSelect-selectMenu-')]"));
        shortSleep();


        List<WebElement> propertyList = findElementsByXpath("//li[@role='option']");
        System.out.println(propertyList.size());

        for(int i=0; i<propertyList.size(); i++){
            System.out.println(propertyList.get(i).getText());
        }

        System.out.println(propertyList.size());




    }

    @DataProvider(name = "propertyList")
    public Object[][] propertyLists() {
        return new Object[][]{
                new Object[]{"date","Date"},
                {"desc","String"},
                {"description","String"},
                {"fullpath","String"},
                {"invalidLanguages","String"},
                {"lastPublished","date"},
                {"lastPublishedBy","String"},
                {"legacyRuleSettings","String"},
                {"lockIsDeep","Boolean"},
                {"mixinTypes","Name"},
                {"nodename","String"}

        };
    }


}
