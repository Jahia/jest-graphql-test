package org.jahia.modules.sdlGeneratorTools;


import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
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
    public void navigateSDLToolTest() {
        Assert.assertTrue(findByXpath("//p[contains(., 'SDL Generator Tools')]").isDisplayed(), "Failed to locate header SDL Generator Tools");
        Assert.assertTrue(findByXpath("//p[contains(., 'Build your GraphQL')]").isDisplayed(), "Failed to navigate to SDL Generator Tools page");

        Assert.assertTrue(findByXpath("//span[contains(., 'Create types')]").isDisplayed(), "Failed to locate Create types text");
        Assert.assertTrue(findByXpath("//span[contains(., 'Define finder')]").isDisplayed(), "Failed to locate Define finder text");
        Assert.assertTrue(findByXpath("//span[contains(., 'Export result')]").isDisplayed(), "Failed to locate Export result text");


        clickOn(findByXpath("//button/span/p[contains(., 'Back to tools')]"));
        assertTitle("Digital Experience Manager Support Tools");

        Assert.assertTrue(findByXpath("//h1[contains(.,'Support Tools (Digital Experience Manager')]").isDisplayed(), "Failed to locate Export result text");
    }

    @Test(dataProvider = "nodeTypeList", alwaysRun = true, priority = 1)
    public void nodeTypeListTest(String searchTerm, String nodeTypeInList, int listSize) {

        waitForElementToBeVisible(findByXpath("//span/p[contains(text(),'Add new type')]"));
        clickOn(findByXpath("//span/p[contains(text(),'Add new type')]"));

        WebElement addNodeTypeInput = findByXpath("//div[@type='text']//input");
        addNodeTypeInput.sendKeys(searchTerm);

        shortSleep();
        List<WebElement> selectNodeTypeList = findElementsByXpath("//div[contains(@id,'-option-')]");

//        System.out.println(selectNodeTypeList.size());
//        for(int i=0; i<selectNodeTypeList.size(); i++){
//            System.out.println(selectNodeTypeList.get(i).getText());
//        }

        Assert.assertEquals(selectNodeTypeList.size(), listSize, "node type list filtered incorrectly");
        Assert.assertTrue(findByXpath("//p[contains(.,'"+nodeTypeInList+"')]").isDisplayed(), "node type list filtered incorrectly");
        clickOn(selectNodeTypeList.get(0));
        WebElement cancelButton = findByXpath("//p[contains(.,'Cancel')]");
        clickOn(cancelButton);
    }

    @Test(alwaysRun = true, dataProvider = "typeList", priority = 2)
    public void createTypeTest(String nodeType, String searchTerm) {

        addType(nodeType, searchTerm);
    }

}

