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

        verifyElementClickable(findByXpath("//li//span[text()='NewsEntry']"));
        clickOn(findByXpath("//li//span[text()='NewsEntry']"));

        verifyElementClickable(findByXpath(xpathAddNewProperty));

        addProperty("jcr:title", "title");
        addProperty("date", "date");

        verifyElementClickable(findByXpath("//li//span[text()='Company']"));
        clickOn(findByXpath("//li//span[text()='Company']"));

        addProperty("website", "website");
        addProperty("jcr:title", "title");

        clickNext();

        addFinder("all", "company");
        addFinder("byTitleConnection", "company");
        addFinder("byWebsite", "company");

        clickBack();

        Assert.assertTrue(findByXpath("//div[@class='ace_content'][contains(.,'type ArticleTitleAndIntroduction @mapping(node: \"jnt:article\") {    metadata: Metadata }type NewsEntry @mapping(node: \"jnt:news\") " +
                "{    metadata: Metadata     title: String @mapping(property: \"jcr:title\")    date: Date @mapping(property: \"date\")}type Company @mapping(node: \"jdnt:company\") " +
                "{    metadata: Metadata     website: String @mapping(property: \"website\")    title: String @mapping(property: \"jcr:title\")}extend type Query " +
                "{    allCompany: [Company]    companyByTitleConnection: CompanyConnection    companyByWebsite: [Company]}')]").isDisplayed(),
                "Failed to find types and definitions in the generated schema ");


        List<WebElement> editTypeButtons = findElementsByXpath("//button[@aria-label='Edit']");
        clickOn(editTypeButtons.get(0));

        Assert.assertTrue(findByXpath(xpathDeleteButton).isDisplayed(),
                "Edit type dialog failed to load as expected");
        Assert.assertTrue(findByXpath(xpathUpdateButton).isDisplayed(),
                "Edit type dialog failed to load as expected");

        clickOn(findByXpath(xpathDeleteButton));

        Assert.assertEquals(findByXpath("//p[contains(.,'Node type')]/parent::li/parent::ul/li[2]").getText(), "NewsEntry",
                "the created types list failed to update after deleting a type");

        Assert.assertNull(findByXpath("//span[@class='ace_identifier' and contains(.,'ArticleTitleAndIntroduction')]"),
                "GraphQL Schema view did not update after deleting a type");
        Assert.assertEquals(findElementsByXpath("//span[@class='ace_identifier' and contains(.,'metadata')]").size(), 2,
                "GraphQL Schema view did not update after deleting a type");

        editTypeButtons = findElementsByXpath("//button[@aria-label='Edit']");
        clickOn(editTypeButtons.get(0));

        clickDelete();

        Assert.assertEquals(findByXpath("//p[contains(.,'Node type')]/parent::li/parent::ul/li[2]").getText(), "Company",
                "the created types list failed to update after deleting a type");
        Assert.assertNull(findByXpath("//span[@class='ace_identifier' and contains(.,'NewsEntry')]"),
                "GraphQL Schema view did not update after deleting a type");
        Assert.assertEquals(findElementsByXpath("//span[@class='ace_identifier' and contains(.,'metadata')]").size(), 1,
                "GraphQL Schema view did not update after deleting a type");
        Assert.assertEquals(findElementsByXpath("//span[@class='ace_identifier' and contains(.,'title')]").size(), 2,
                "GraphQL Schema view did not update after deleting a type");



        editTypeButtons = findElementsByXpath("//button[@aria-label='Edit']");
        clickOn(editTypeButtons.get(0));

        clickOn(findByXpath(xpathDeleteButton));

        Assert.assertEquals(findElementsByXpath("//span[@class='ace_identifier']").size(), 0,
                "GraphQL Schema view did not update after deleting a type");
    }
}
