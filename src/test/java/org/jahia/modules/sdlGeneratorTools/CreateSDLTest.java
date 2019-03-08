package org.jahia.modules.sdlGeneratorTools;


import org.jahia.modules.graphQLtests.GqlApiController;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

/**
 * Created by parveer on 2019-03-01.
 */
public class CreateSDLTest extends GqlApiController {


    @Test(alwaysRun = true)
    public void navigateTest() {

        goToTools("jahia", "password");
        getDriver().navigate().to(getPath("/modules/sdl-generator-tools/tools/sdlGeneratorTools.jsp"));


        Assert.assertTrue(findByXpath("//p[contains(., 'SDL Generator Tools')]").isDisplayed(), "Failed to locate header SDL Generator Tools");
        Assert.assertTrue(findByXpath("//p[contains(., 'Build your GraphQL')]").isDisplayed(), "Failed to navigate to SDL Generator Tools page");

        Assert.assertTrue(findByXpath("//span[contains(., 'Create types')]").isDisplayed(), "Failed to locate Create types text");
        Assert.assertTrue(findByXpath("//span[contains(., 'Define finder')]").isDisplayed(), "Failed to locate Define finder text");
        Assert.assertTrue(findByXpath("//span[contains(., 'Export result')]").isDisplayed(), "Failed to locate Export result text");


        clickOn(findByXpath("//button/span[contains(., 'Back to tools')]"));
        Assert.assertTrue(findByXpath("//h1[contains(., 'Support Tools')]").isDisplayed(), "Failed to locate Export result text");
    }


    @Test(alwaysRun = true)
    public void createTypeTest() {
        goToTools("jahia", "password");
        getDriver().navigate().to(getPath("/modules/sdl-generator-tools/tools/sdlGeneratorTools.jsp"));

        WebElement addNewTypeBtn = findByXpath("//span[contains(text(),'Add new type')]");
        Assert.assertTrue(addNewTypeBtn.isDisplayed(), "Failed to find Add new type button");

        addNewTypeBtn.click();

        checkCreateTypeDialog();

        WebElement addNodeTypeDropDown = findByXpath("//label[contains(text(), 'Select a node type')]/parent::div/div");

        addNodeTypeDropDown.click();

        Assert.assertTrue(findByXpath("//ul[@role='listbox']").isDisplayed(), "Select node type dropdown failed to load");

        findByXpath("//span[contains(text(),'Article (title and introduction)')]").click();

        findByXpath("//input[@id='typeName']").sendKeys("Article");

        findByXpath("//span[contains(text(), 'Save')]").click();

        WebElement createdTypesList = findByXpath("//li[contains(text(),'Node type')]/parent::ul/li[3]");
        Assert.assertTrue(createdTypesList.isDisplayed(), "List of created types failed to load");
        Assert.assertTrue(createdTypesList.findElement(By.xpath("//span[contains(text(),'Article')]")).isDisplayed(),
                "the created type 'Article' does not exist in the list of created types");

        List<WebElement> divsInSchemaView = findElementsByXpath("//div[@id='gqlschema']/div[2]/div/div[3]/div");

        Assert.assertEquals(divsInSchemaView.size(), 3, "the schema view is incorrect");
        Assert.assertEquals(divsInSchemaView.get(0).findElements(By.tagName("span")).size(), 9, "the schema view is incorrect");
        Assert.assertEquals(divsInSchemaView.get(1).findElements(By.tagName("span")).size(), 0, "the schema view is incorrect");
        Assert.assertEquals(divsInSchemaView.get(2).findElements(By.tagName("span")).size(), 1, "the schema view is incorrect");
    }


    private void checkCreateTypeDialog() {
        Assert.assertTrue(findByXpath("//h2[contains(text(), 'Add new type')]").isDisplayed(),
                "Add new type dialog box failed to load as expected");
        Assert.assertTrue(findByXpath("//label[contains(text(), 'Select a node type')]").isDisplayed(),
                "Add new type dialog box failed to load as expected");
        Assert.assertTrue(findByXpath("//label[contains(text(), 'Custom type name')]").isDisplayed(),
                "Add new type dialog box failed to load as expected");
        Assert.assertTrue(findByXpath("//input[@id='typeName']").isDisplayed(),
                "Add new type dialog box failed to load as expected");
        Assert.assertTrue(findByXpath("//span[contains(text(), 'Ignore Default Queries')]").isDisplayed(),
                "Add new type dialog box failed to load as expected");
        Assert.assertTrue(findByXpath("//span[contains(text(), 'Cancel')]").isDisplayed(),
                "Add new type dialog box failed to load as expected");
        Assert.assertTrue(findByXpath("//span[contains(text(), 'Save')]").isDisplayed(),
                "Add new type dialog box failed to load as expected");
    }

}

