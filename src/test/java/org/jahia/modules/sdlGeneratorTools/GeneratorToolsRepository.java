package org.jahia.modules.sdlGeneratorTools;

import org.jahia.modules.graphQLtests.GqlApiController;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.DataProvider;

public class GeneratorToolsRepository extends GqlApiController {

    protected void addAType(String nodeType, String typeName) {
        WebElement addNewTypeBtn = findByXpath("//span/p[contains(text(),'Add new type')]");
        Assert.assertTrue(addNewTypeBtn.isDisplayed(), "Failed to find Add new type button");

        clickOn(addNewTypeBtn);

        selectNodeType(nodeType, typeName);

        findByXpath("//input[@id='typeName']").sendKeys(typeName);

        findByXpath("//span/p[contains(text(), 'Save')]").click();
    }

    protected void selectNodeType(String nodeType, String searchTerm) {

        WebElement addNodeTypeInput = findByXpath("//div[@type='text']//input");

        performSendKeys(addNodeTypeInput, searchTerm);
        shortSleep();

        Assert.assertTrue(findByXpath("//div/p[contains(.,'"+nodeType+"')]").isDisplayed(), "node type was not on the list");

        clickOn(findByXpath("//div/p[contains(.,'"+nodeType+"')]"));
    }

    @DataProvider(name = "nodeTypeList")
    public Object[][] createNodeTypeLists() {
        return new Object[][]{
            new Object[]{"article","jnt:article", 2},
                {"para","jnt:paragraph", 5},
                {"news","jnt:news", 6},
                {"bann","jnt:banner", 2},
                {"company","jdnt:company", 2},
                {"text","jnt:bigText", 8},
                {"content","jnt:content", 25}
        };
    }

    @DataProvider(name = "propertiesList")
    public Object[][] createPropertiesLists() {
        return new Object[][]{
                new Object[]{"jnt:article","article", 25},
                {"jnt:news","news", 24},
                {"jnt:banner","bann", 25},
                {"jdnt:company","company", 27},
                {"jnt:bigText","text", 22},
                {"jnt:content","content", 30}
        };
    }
}
