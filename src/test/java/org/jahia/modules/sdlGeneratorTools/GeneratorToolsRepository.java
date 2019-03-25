package org.jahia.modules.sdlGeneratorTools;

import org.jahia.modules.graphQLtests.GqlApiController;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.DataProvider;

public class GeneratorToolsRepository extends GqlApiController {

    protected void clickAdd() {
        WebElement addButton = findByXpath("//p[contains(.,'Add')]/parent::span/parent::button");
        waitForElementToBeClickable(addButton);
        clickOn(addButton);
    }

    protected void addType(String nodeType, String typeName) {
        WebElement addNewTypeBtn = findByXpath("//span/p[contains(text(),'Add new type')]");
        Assert.assertTrue(addNewTypeBtn.isDisplayed(), "Failed to find Add new type button");

        clickOn(addNewTypeBtn);

        checkCreateTypeDialog();

        selectNodeType(nodeType, typeName);

        WebElement customTypeName = findByXpath("//input[@id='typeName']");
        Assert.assertEquals(customTypeName.getAttribute("value").toLowerCase(), typeName.toLowerCase(), "Prefill of typename failed");

        WebElement entryPoints = findByXpath("//p[contains(.,'Remove "+ typeName +"ByPath and " + typeName + "ById entry points')]");
        assertEqualsCaseInsensitive(entryPoints.getText(), "Remove "+typeName+"ByPath and "+typeName+"ById entry points");

        //Assert.assertTrue(findByXpath("//p[contains(.,'Remove "+ typeName +"ByPath and " + typeName + "ById entry points')]").isDisplayed(), "Expected label not found");
        clickAdd();
    }

    protected void selectNodeType(String nodeType, String searchTerm) {

        WebElement addNodeTypeInput = findByXpath("//div[@type='text']//input");

        performSendKeys(addNodeTypeInput, searchTerm);
        shortSleep();

        Assert.assertTrue(findByXpath("//div/p[contains(.,'"+nodeType+"')]").isDisplayed(), "node type was not on the list");

        clickOn(findByXpath("//div/p[contains(.,'"+nodeType+"')]"));
    }

    protected void addProperty(String property, String propertyName) {
        clickOn(findByXpath("//p[contains(.,'Add new property')]/parent::span/parent::button"));

        waitForElementToBeClickable(findByXpath("//p[contains(.,'Select property')]/parent::span/parent::button"));
        clickOn(findByXpath("//p[contains(.,'Select property')]/parent::span/parent::button"));
        shortSleep();

        checkAddPropertyDialog();

        clickOn(findByXpath("//div[contains(@class, 'MuiSelect-selectMenu-')]"));
        shortSleep();

        clickOn(findByXpath("//li[@data-value='"+property+"']"));

        findByXpath("//input[@id='propertyName']").sendKeys(propertyName);

        clickAdd();
    }

    protected void addMapPropertyToType(String predefinedType, String property, String propertyName) {
        clickOn(findByXpath("//span/p[contains(., 'Add new property')]"));

        WebElement mapPropertyButton = waitForElementToBeClickable(findByXpath("//p[contains(.,'Select and map property to type')]/parent::span/parent::button"));
        clickOn(mapPropertyButton);
        waitForElementToBeVisible(findByID("form-dialog-title"));

        checkMapPropertyDialog();

        clickOn(findElementsByXpath("//div[contains(@class, 'MuiSelect-selectMenu-')]").get(0));
        clickOn(findByXpath("//li[@data-value='" + predefinedType + "']"));
        Assert.assertTrue(findByXpath("//span[contains(.,'" +predefinedType+ "')]/parent::div").isDisplayed(), "Failed to select predefined type");


        clickOn(findElementsByXpath("//div[contains(@class, 'MuiSelect-selectMenu-')]").get(1));
        clickOn(findByXpath("//li[@data-value='" + property + "']"));
        Assert.assertTrue(findByXpath("//span[contains(.,'" + property + "')]/parent::div").isDisplayed(), "Failed to select predefined type");


        WebElement propertyNameInput = findByXpath("//input[@id='propertyName']");
        performSendKeys(propertyNameInput, propertyName);
        Assert.assertTrue(propertyNameInput.getAttribute("value").contains(propertyName), "Failed to enter property name");
    }



        protected void addFinder(String finder, String customName) {
        waitForElementToBeVisible(findByXpath("//p[contains(.,'Add a finder')]"));

        clickOn(findByXpath("//p[contains(.,'Add a finder')]"));

        checkAddFinderDialog();

        clickOn(findByXpath("//input[@id='finder-name']/parent::div/div"));

        waitForElementToBeVisible(findByXpath("//ul[@role='listbox']"));

        clickOn(findByXpath("//li[@data-value='"+finder+"']"));

        findByXpath("//input[@id='propertyName']").sendKeys(customName);

        if (finder.equals("all")) {
            Assert.assertEquals(findByXpath("//p[contains(@class,'FinderPreviewComp')]/span[1]").getText(), finder,
                    "the finder preview is incorrect");
            String partialFinderPreview = findByXpath("//p[contains(@class,'FinderPreviewComp')]/em").getText().toLowerCase();
            Assert.assertEquals(partialFinderPreview, customName,
                    "the finder preview is incorrect");
        } else if (finder.equals("allConnection")) {
            Assert.assertEquals(findByXpath("//p[contains(@class,'FinderPreviewComp')]/span[1]").getText(), "all",
                    "the finder preview is incorrect");
            String partialFinderPreview = findByXpath("//p[contains(@class,'FinderPreviewComp')]/em").getText().toLowerCase();
            Assert.assertEquals(partialFinderPreview, customName,
                    "the finder preview is incorrect");
            Assert.assertEquals(findByXpath("//p[contains(@class,'FinderPreviewComp')]/span[2]").getText(), "Connection",
                    "the finder preview is incorrect");
        } else {
            String partialFinderPreview = findByXpath("//p[contains(@class,'FinderPreviewComp')]/em").getText().toLowerCase();
            Assert.assertEquals(partialFinderPreview, customName,
                    "the finder preview is incorrect");
            Assert.assertEquals(findByXpath("//p[contains(@class,'FinderPreviewComp')]/span").getText(), finder,
                    "the finder preview is incorrect");
        }

        clickOn(findByXpath("//p[contains(.,'Add')]/parent::span/parent::button"));
    }

    protected void checkAddPropertyDialog() {
        Assert.assertTrue(findByXpath("//h2[contains(text(),'Add new property')]").isDisplayed(),
                "Add new property dialog box failed to open");
        Assert.assertTrue(findByXpath("//p[contains(text(),'Select a property')]").isDisplayed(),
                "Add new property dialog box failed to open");
        Assert.assertTrue(findByXpath("//div[contains(@class,'MuiSelect-selectMenu-')]").isDisplayed(),
                "Add new property dialog box failed to open");
        Assert.assertTrue(findByXpath("//p[contains(text(),'Custom property name')]").isDisplayed(),
                "Add new property dialog box failed to open");
        Assert.assertTrue(findByXpath("//input[@id='propertyName']").isDisplayed(),
                "Add new property dialog box failed to open");
        Assert.assertTrue(findByXpath("//p[contains(., 'Cancel')]/parent::span/parent::button").isDisplayed(),
                "Add property dialog box failed to load as expected");
        Assert.assertTrue(findByXpath("//p[contains(., 'Add')]/parent::span/parent::button").isDisplayed(),
                "Add property dialog box failed to load as expected");
    }

    protected void checkMapPropertyDialog() {
        checkAddPropertyDialog();
        Assert.assertTrue(findByXpath("//p[contains(text(),'Predefined type')]").isDisplayed(),
                "Add new property dialog box failed to open");
    }

    protected void checkCreateTypeDialog() {
        Assert.assertTrue(findByXpath("//h2[contains(., 'Add new type')]").isDisplayed(),
                "Add new type dialog box failed to load as expected");
        Assert.assertTrue(findElementsByXpath("//div[contains(.,'Select or search a node')]").get(9).isDisplayed(),
                "Add new type dialog box failed to load as expected");
        Assert.assertTrue(findByXpath("//label/p[contains(., 'Custom type name')]").isDisplayed(),
                "Add new type dialog box failed to load as expected");
        Assert.assertTrue(findByXpath("//input[@id='typeName']").isDisplayed(),
                "Add new type dialog box failed to load as expected");
        Assert.assertTrue(findByXpath("//p[contains(., 'Remove ByPath and ById entry points')]").isDisplayed(),
                "Add new type dialog box failed to load as expected");
        Assert.assertTrue(findByXpath("//p[contains(., 'Cancel')]/parent::span/parent::button").isDisplayed(),
                "Add new type dialog box failed to load as expected");
        Assert.assertTrue(findByXpath("//p[contains(., 'Add')]/parent::span/parent::button").isDisplayed(),
                "Add new type dialog box failed to load as expected");
    }

    protected void checkEditTypeDialog() {
        Assert.assertTrue(findByXpath("//h2[contains(., 'Edit type')]").isDisplayed(),
                "Add new type dialog box failed to load as expected");
        Assert.assertTrue(findByXpath("//label[contains(., 'Select a node type')]").isDisplayed(),
                "Add new type dialog box failed to load as expected");
        Assert.assertTrue(findByXpath("//label[contains(., 'Custom type name')]").isDisplayed(),
                "Add new type dialog box failed to load as expected");
        Assert.assertTrue(findByXpath("//input[@id='typeName']").isDisplayed(),
                "Add new type dialog box failed to load as expected");
        Assert.assertTrue(findByXpath("//span[contains(., 'Ignore Default Queries')]").isDisplayed(),
                "Add new type dialog box failed to load as expected");
        Assert.assertTrue(findByXpath("//p[contains(., 'Cancel')]/parent::span/parent::button").isDisplayed(),
                "Add new type dialog box failed to load as expected");
        Assert.assertTrue(findByXpath("//p[contains(., 'Add')]/parent::span/parent::button").isDisplayed(),
                "Add new type dialog box failed to load as expected");
    }

    protected void checkAddFinderDialog() {
        waitForElementToBeVisible(findByID("form-dialog-title"));
        Assert.assertTrue(findByXpath("//p[contains(.,'Select a finder')]").isDisplayed(),
                "Add finder dialog failed to load correctly");
        Assert.assertTrue(findByXpath("//input[@id='finder-name']/parent::div/div").isDisplayed(),
                "Select a finder input field is not visible");
        Assert.assertTrue(findByXpath("//p[contains(.,'Custom name')]").isDisplayed(),
                "title for Custom name input is not visible");
        Assert.assertTrue(findByXpath("//input[@id='propertyName']").isDisplayed(),
                "Custom name input field is not visible");
        Assert.assertTrue(findByXpath("//p[contains(.,'Cancel')]/parent::span/parent::button").isDisplayed(),
                "Cancel button is not visible");
        Assert.assertTrue(findByXpath("//p[contains(.,'Save')]/parent::span/parent::button").isDisplayed(),
                "Save button is not visible");
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
