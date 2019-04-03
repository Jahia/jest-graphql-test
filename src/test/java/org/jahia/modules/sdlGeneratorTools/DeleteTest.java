package org.jahia.modules.sdlGeneratorTools;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

public class DeleteTest extends GeneratorToolsRepository {

    @BeforeMethod()
    private void goToGeneratorTools() {
        goToTools("jahia", "password");
        getDriver().navigate().to(getPath("/modules/sdl-generator-tools/tools/sdlGeneratorTools.jsp"));
    }

    @Test(alwaysRun = true)
    public void deleteTypeTest() {
        goToGeneratorTools();
        clickClear();

        addType("jnt:article", "article");
        addType("jnt:news", "newsEntry");
        addType("jdnt:company", "Company");

        waitForElementToBeClickable(findByXpath("//p[contains(.,'Node type')]/parent::li/parent::ul/li[4]"));
        clickOn(findByXpath("//p[contains(.,'Node type')]/parent::li/parent::ul/li[4]"));

        waitForElementToBeClickable(findByXpath("//p[contains(.,'Add new property')]"));

        addProperty("jcr:title", "title");
        addProperty("date", "date");

        clickOn(findByXpath("//p[contains(.,'Node type')]/parent::li/parent::ul/li[5]"));

        addProperty("website", "website");
        addProperty("jcr:title", "title");

        clickOn(findByXpath("//p[contains(.,'Next')]/parent::span/parent::button"));

        addFinder("all", "company");
        addFinder("byTitleConnection", "company");
        addFinder("byWebsite", "company");

        clickBack();

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
