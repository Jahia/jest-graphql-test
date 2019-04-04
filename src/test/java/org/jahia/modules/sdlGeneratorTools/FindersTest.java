package org.jahia.modules.sdlGeneratorTools;


import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class FindersTest extends GeneratorToolsRepository{

    @BeforeMethod()
    private void goToGeneratorTools() {
        goToTools("jahia", "password");
        getDriver().navigate().to(getPath("/modules/sdl-generator-tools/tools/sdlGeneratorTools.jsp"));
    }

    @Test(alwaysRun = true)
    public void findersListTest() {
        addType("jnt:news", "news");

        addProperty("jcr:title", "title");
        addProperty("desc", "description");
        addProperty("date", "created");

        clickNext();

        waitForElementToBeClickable(findByXpath("//p[contains(.,'Add a finder')]"));
        clickOn(findByXpath("//p[contains(.,'Add a finder')]"));

        checkAddFinderDialog();

        clickOn(findByXpath("//input[@id='finder-name']/parent::div/div"));

        Assert.assertEquals(findElementsByXpath("//ul[@role='listbox']/li").size(), 8,
                "Number of finders in finders list is incorrect");
    }

    @Test(alwaysRun = true, priority = 1)
    public void addFindersTest() {
        addType("jnt:news", "NewsEntry");

        addProperty("jcr:title", "title");

        clickNext();

        addFinder("all", "news");

        Assert.assertTrue(findByXpath("//div[@class='ace_content'][contains(.,'type NewsEntry @mapping(node: \"jnt:news\") " +
                "{    metadata: Metadata     title: String @mapping(property: \"jcr:title\")}extend type Query {    allNews: [NewsEntry]}')]").isDisplayed(),
                "Failed to create schema");
    }

    @Test(alwaysRun = true, priority = 2)
    public void editFindersTest() {
        addType("jnt:news", "NewsEntry");

        addProperty("jcr:title", "title");

        clickNext();

        addFinder("all", "news");

        waitForElementToBeClickable(findByXpath("//span[contains(.,'allNews')]/parent::div/parent::div[@role='button']"));
        clickOn(findByXpath("//span[contains(.,'allNews')]/parent::div/parent::div[@role='button']"));

        checkEditFinderDialog();

        clickOn(findByXpath("//input[@id='finder-name']/parent::div/div"));
        waitForElementToBeClickable(findByXpath("//li[@data-value='byTitle']"));

        clickOn(findByXpath("//li[@data-value='byTitle']"));
        shortSleep();
        Assert.assertEquals(findByXpath("//p[contains(@class,'FinderPreviewComp')]/span").getText(), "ByTitle",
                "finder preview failed to reflect the newly chosen finder");

        findByXpath("//input[@id='propertyName']").clear();
        findByXpath("//input[@id='propertyName']").sendKeys("myNews");
        shortSleep();

        Assert.assertEquals(findByXpath("//p[contains(@class,'FinderPreviewComp')]/em").getText(), "myNews",
                "finder preview failed to reflect the new custom name");

        clickUpdate();

        Assert.assertEquals(findByXpath("//div[@id='gqlschema']/div[2]/div/div[3]/div[7]/span[1]").getText(), "myNewsByTitle",
                "the added finder did not appear in GraphQL Schema view");
    }

    private void checkEditFinderDialog() {
        Assert.assertTrue(findByXpath("//h2[contains(.,'Edit a finder')]").isDisplayed(),
                "Edit finder dialog failed to open");
        Assert.assertTrue(findByXpath("//p[contains(.,'Select a finder')]").isDisplayed(),
                "Edit finder dialog failed to open");
        Assert.assertTrue(findByXpath("//input[@id='finder-name']/parent::div").isDisplayed(),
                "Edit finder dialog failed to open");
        Assert.assertTrue(findByXpath("//p[contains(.,'Custom name')]").isDisplayed(),
                "Edit finder dialog failed to open");
        Assert.assertTrue(findByXpath("//input[@id='propertyName']").isDisplayed(),
                "Edit finder dialog failed to open");
        Assert.assertTrue(findByXpath("//span[text()='Delete']/parent::button").isDisplayed(),
                "Edit finder dialog failed to open");
        Assert.assertTrue(findByXpath("//span[text()='Cancel']/parent::button").isDisplayed(),
                "Edit finder dialog failed to open");
        Assert.assertTrue(findByXpath("//span[text()='Update']/parent::button").isDisplayed(),
                "Edit finder dialog failed to open");
    }


    //findByXpath("//p[contains(.,'Node type')]/parent::ul/li[3]");  ---start of list of created types
}
