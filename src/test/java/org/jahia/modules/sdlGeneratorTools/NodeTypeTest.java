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

    @Test(alwaysRun = true, priority = 3)
    public void deleteTypeTest() {

        clickClear();

        addType("jnt:article", "article");
        addType("jnt:news", "newsentry");
        addType("jdnt:company", "company");

        waitForElementToBeClickable(findByXpath("//p[contains(.,'Node type')]/parent::li/parent::ul/li[4]"));
        clickOn(findByXpath("//p[contains(.,'Node type')]/parent::li/parent::ul/li[4]"));

        waitForElementToBeClickable(findByXpath("//p[contains(.,'Add new property')]"));

        addProperty("jcr:title", "title");
        addProperty("Date", "date");

        clickOn(findByXpath("//p[contains(.,'Node type')]/parent::li/parent::ul/li[5]"));

        addProperty("website", "website");
        addProperty("jcr:title", "title");

        clickOn(findByXpath("//p[contains(.,'Next')]/parent::span/parent::button"));

        addFinder("all", "company");
        addFinder("byTitleConnection", "company");
        addFinder("byWebsite", "company");

        clickOn(findByXpath("//p[contains(.,'Back')]/parent::span/parent::button"));

        //asserts for GraphQL Schema view before deleting types
        Assert.assertTrue(findByXpath("//span[@class='ace_identifier' and contains(.,'ArticleTitleAndIntroduction')]").isDisplayed(),
                "GraphQL Schema view is incorrect");
        Assert.assertTrue(findByXpath("//span[@class='ace_identifier' and contains(.,'NewsEntry')]").isDisplayed(),
                "GraphQL Schema view is incorrect");
        Assert.assertTrue(findByXpath("//span[@class='ace_identifier' and contains(.,'Company')]").isDisplayed(),
                "GraphQL Schema view is incorrect");
        Assert.assertTrue(findByXpath("//span[@class='ace_identifier' and contains(.,'allCompany')]").isDisplayed(),
                "GraphQL Schema view is incorrect");
        Assert.assertTrue(findByXpath("//span[@class='ace_identifier' and contains(.,'companyByTitleConnection')]").isDisplayed(),
                "GraphQL Schema view is incorrect");
        Assert.assertTrue(findByXpath("//span[@class='ace_identifier' and contains(.,'companyByWebsite')]").isDisplayed(),
                "GraphQL Schema view is incorrect");
        Assert.assertEquals(findElementsByXpath("//span[@class='ace_identifier' and contains(.,'title')]").size(), 4,
                "GraphQL Schema view is incorrect");
        Assert.assertEquals(findElementsByXpath("//span[@class='ace_identifier' and contains(.,'date')]").size(), 2,
                "GraphQL Schema view is incorrect");
        Assert.assertEquals(findElementsByXpath("//span[@class='ace_identifier' and contains(.,'website')]").size(), 2,
                "GraphQL Schema view is incorrect");
        Assert.assertEquals(findElementsByXpath("//span[@class='ace_identifier' and contains(.,'metadata')]").size(), 3,
                "GraphQL Schema view is incorrect");

        List<WebElement> editTypeButtons = findElementsByXpath("//button[@aria-label='Edit']");
        clickOn(editTypeButtons.get(0));

        Assert.assertTrue(findByXpath("//p[contains(.,'Delete')]/parent::span/parent::button").isDisplayed(),
                "Edit type dialog failed to load as expected");
        Assert.assertTrue(findByXpath("//p[contains(.,'Update')]/parent::span/parent::button").isDisplayed(),
                "Edit type dialog failed to load as expected");

        clickOn(findByXpath("//p[contains(.,'Delete')]/parent::span/parent::button"));
        shortSleep();

        Assert.assertEquals(findByXpath("//p[contains(.,'Node type')]/parent::li/parent::ul/li[3]").getText(), "NewsEntry",
                "the created types list failed to update after deleting a type");
        Assert.assertFalse(findByXpath("//span[@class='ace_identifier' and contains(.,'ArticleTitleAndIntroduction')]").isDisplayed(),
                "GraphQL Schema view did not update after deleting a type");
        Assert.assertEquals(findElementsByXpath("//span[@class='ace_identifier' and contains(.,'metadata')]").size(), 2,
                "GraphQL Schema view did not update after deleting a type");

        editTypeButtons = findElementsByXpath("//button[@aria-label='Edit']");
        clickOn(editTypeButtons.get(0));

        clickOn(findByXpath("//p[contains(.,'Delete')]/parent::span/parent::button"));
        shortSleep();

        Assert.assertEquals(findByXpath("//p[contains(.,'Node type')]/parent::li/parent::ul/li[3]").getText(), "Company",
                "the created types list failed to update after deleting a type");
        Assert.assertFalse(findByXpath("//span[@class='ace_identifier' and contains(.,'NewsEntry')]").isDisplayed(),
                "GraphQL Schema view did not update after deleting a type");
        Assert.assertEquals(findElementsByXpath("//span[@class='ace_identifier' and contains(.,'metadata')]").size(), 1,
                "GraphQL Schema view did not update after deleting a type");
        Assert.assertEquals(findElementsByXpath("//span[@class='ace_identifier' and contains(.,'title')]").size(), 2,
                "GraphQL Schema view did not update after deleting a type");



        editTypeButtons = findElementsByXpath("//button[@aria-label='Edit']");
        clickOn(editTypeButtons.get(0));

        clickOn(findByXpath("//p[contains(.,'Delete')]/parent::span/parent::button"));
        shortSleep();

        Assert.assertEquals(findElementsByXpath("//span[@class='ace_identifier']").size(), 0,
                "GraphQL Schema view did not update after deleting a type");
    }

}

