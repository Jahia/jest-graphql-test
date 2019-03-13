package org.jahia.modules.sdlGeneratorTools;


import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

/**
 * Created by parveer on 2019-03-01.
 */
public class NodeTypeTest extends GeneratorToolsRepository {

    @BeforeMethod()
    private void goToGeneratorTools() {
        goToTools("jahia", "password");
        getDriver().navigate().to(getPath("/modules/sdl-generator-tools/tools/sdlGeneratorTools.jsp"));
    }


    @Test(alwaysRun = true)
    public void navigateTest() {
        Assert.assertTrue(findByXpath("//p[contains(., 'SDL Generator Tools')]").isDisplayed(), "Failed to locate header SDL Generator Tools");
        Assert.assertTrue(findByXpath("//p[contains(., 'Build your GraphQL')]").isDisplayed(), "Failed to navigate to SDL Generator Tools page");

        Assert.assertTrue(findByXpath("//span[contains(., 'Create types')]").isDisplayed(), "Failed to locate Create types text");
        Assert.assertTrue(findByXpath("//span[contains(., 'Define finder')]").isDisplayed(), "Failed to locate Define finder text");
        Assert.assertTrue(findByXpath("//span[contains(., 'Export result')]").isDisplayed(), "Failed to locate Export result text");


        clickOn(findByXpath("//button/span[contains(., 'Back to tools')]"));
        Assert.assertTrue(findByXpath("//h1[contains(., 'Support Tools')]").isDisplayed(), "Failed to locate Export result text");
    }

    @Test(dataProvider = "nodeTypeList", alwaysRun = true)
    public void nodeTypeListTest(String searchTerm, String nodeTypeInList, int listSize) {

        WebElement addNewTypeBtn = findByXpath("//span/p[contains(text(),'Add new type')]");
        waitForElementToBeVisible(addNewTypeBtn);
        clickOn(addNewTypeBtn);

        WebElement addNodeTypeInput = findByXpath("//div[@type='text']/div[1]/div[2]/div/input");
        addNodeTypeInput.sendKeys(searchTerm);

        List<WebElement> selectNodeTypeList = findElementsByXpath("//div[contains(@id,'-option-')]");
        Assert.assertEquals(selectNodeTypeList.size(), listSize, "node type list filtered incorrectly");
        Assert.assertTrue(findByXpath("//p[contains(.,'"+nodeTypeInList+"')]").isDisplayed(), "node type list filtered incorrectly");

        WebElement cancelButton = findByXpath("//p[contains(.,'Cancel')]");
        clickOn(cancelButton);
    }

    @Test(alwaysRun = true, dependsOnMethods = "navigateTest")
    public void createTypeTest() {
        WebElement addNewTypeBtn = findByXpath("//span/p[contains(text(),'Add new type')]");
        waitForElementToBeVisible(addNewTypeBtn);
        clickOn(addNewTypeBtn);

        checkCreateTypeDialog();

        WebElement addNodeTypeDropDown = findElementsByXpath("//div[contains(.,'Select or search a node')]").get(9);
        clickOn(addNodeTypeDropDown);
        shortSleep();

        selectNodeType("article", "jnt:article");

        clickOn(findByXpath("//p[contains(.,'jnt:article')]"));

        findByXpath("//input[@id='typeName']").sendKeys("Article");

        clickOn(findByXpath("//span/p[contains(text(), 'Save')]"));

        WebElement createdTypesList = findByXpath("//*[@id='tools-container']/div/div[1]/div/div/div[2]/div[1]/div/ul/li[3]");
        Assert.assertTrue(createdTypesList.isDisplayed(), "List of created types failed to load");
        Assert.assertTrue(createdTypesList.findElement(By.xpath("//span[contains(text(),'Article')]")).isDisplayed(),
                "the created type 'Article' does not exist in the list of created types");

        List<WebElement> divsInSchemaView = findElementsByXpath("//div[@id='gqlschema']/div[2]/div/div[3]/div");

        Assert.assertEquals(divsInSchemaView.size(), 3, "the schema view is incorrect");
        Assert.assertEquals(divsInSchemaView.get(0).findElements(By.tagName("span")).size(), 9, "the schema view is incorrect");
        Assert.assertEquals(divsInSchemaView.get(1).findElements(By.tagName("span")).size(), 0, "the schema view is incorrect");
        Assert.assertEquals(divsInSchemaView.get(2).findElements(By.tagName("span")).size(), 1, "the schema view is incorrect");
    }

    private void selectNodeType(String searchTerm, String nodeType) {

        WebElement addNodeTypeInput = findElementsByXpath("//div[contains(.,'Select or search a node')]").get(9);
        addNodeTypeInput.sendKeys(searchTerm);

        Assert.assertTrue(findByXpath("//p[contains(.,'"+nodeType+"')]").isDisplayed(), "node type was not on the list");

        clickOn(findByXpath("//p[contains(.,'"+nodeType+"')]"));
    }


    private void checkCreateTypeDialog() {
        Assert.assertTrue(findByXpath("//h2[contains(., 'Add new type')]").isDisplayed(),
                "Add new type dialog box failed to load as expected");
        Assert.assertTrue(findElementsByXpath("//div[contains(.,'Select or search a node')]").get(9).isDisplayed(),
                "Add new type dialog box failed to load as expected");
        Assert.assertTrue(findByXpath("//label/p[contains(text(), 'Custom type name')]").isDisplayed(),
                "Add new type dialog box failed to load as expected");
        Assert.assertTrue(findByXpath("//input[@id='typeName']").isDisplayed(),
                "Add new type dialog box failed to load as expected");
        Assert.assertTrue(findByXpath("//span/p[contains(text(), 'Ignore Default Queries')]").isDisplayed(),
                "Add new type dialog box failed to load as expected");
        Assert.assertTrue(findByXpath("//span/p[contains(text(), 'Cancel')]").isDisplayed(),
                "Add new type dialog box failed to load as expected");
        Assert.assertTrue(findByXpath("//span/p[contains(text(), 'Save')]").isDisplayed(),
                "Add new type dialog box failed to load as expected");
    }

}

