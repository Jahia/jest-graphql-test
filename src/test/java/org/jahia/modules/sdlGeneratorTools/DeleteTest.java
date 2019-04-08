package org.jahia.modules.sdlGeneratorTools;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class DeleteTest extends GeneratorToolsRepository {

    @Test(alwaysRun = true)
    public void deleteTypeTest() {

        goToGeneratorTools();
        clickClear();

        addType("jnt:article", "article");
        addType("jnt:news", "newsEntry");
        addType("jdnt:company", "Company");

        waitForElementToBeClickable(findByXpath("//li//span[text()='NewsEntry']"));
        clickOn(findByXpath("//li//span[text()='NewsEntry']"));

        waitForElementToBeClickable(findByXpath(xpathAddNewProperty));

        addProperty("jcr:title", "title");
        addProperty("date", "date");

        waitForElementToBeClickable(findByXpath("//li//span[text()='Company']"));
        clickOn(findByXpath("//li//span[text()='Company']"));

        addProperty("website", "website");
        addProperty("jcr:title", "title");

        clickNext();

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

        Assert.assertTrue(findByXpath(xpathDeleteButton).isDisplayed(),
                "Edit type dialog failed to load as expected");
        Assert.assertTrue(findByXpath(xpathUpdateButton).isDisplayed(),
                "Edit type dialog failed to load as expected");

        clickOn(findByXpath(xpathDeleteButton));
        shortSleep();

        Assert.assertEquals(findByXpath("//p[contains(.,'Node type')]/parent::li/parent::ul/li[2]").getText(), "NewsEntry",
                "the created types list failed to update after deleting a type");

        Assert.assertNull(noWaitingFindBy(By.xpath("//span[@class='ace_identifier' and contains(.,'ArticleTitleAndIntroduction')]")),
                "GraphQL Schema view did not update after deleting a type");
        Assert.assertEquals(findElementsByXpath("//span[@class='ace_identifier' and contains(.,'metadata')]").size(), 2,
                "GraphQL Schema view did not update after deleting a type");

        editTypeButtons = findElementsByXpath("//button[@aria-label='Edit']");
        clickOn(editTypeButtons.get(0));

        clickDelete();
        shortSleep();

        Assert.assertEquals(findByXpath("//p[contains(.,'Node type')]/parent::li/parent::ul/li[2]").getText(), "Company",
                "the created types list failed to update after deleting a type");
        Assert.assertNull(noWaitingFindBy(By.xpath("//span[@class='ace_identifier' and contains(.,'NewsEntry')]")),
                "GraphQL Schema view did not update after deleting a type");
        Assert.assertEquals(findElementsByXpath("//span[@class='ace_identifier' and contains(.,'metadata')]").size(), 1,
                "GraphQL Schema view did not update after deleting a type");
        Assert.assertEquals(findElementsByXpath("//span[@class='ace_identifier' and contains(.,'title')]").size(), 2,
                "GraphQL Schema view did not update after deleting a type");



        editTypeButtons = findElementsByXpath("//button[@aria-label='Edit']");
        clickOn(editTypeButtons.get(0));

        clickOn(findByXpath(xpathDeleteButton));
        shortSleep();

        Assert.assertEquals(noWaitingFindsBy(By.xpath("//span[@class='ace_identifier']")).size(), 0,
                "GraphQL Schema view did not update after deleting a type");
    }
}
