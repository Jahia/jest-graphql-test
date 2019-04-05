package org.jahia.modules.sdlGeneratorTools;

import org.jahia.modules.graphQLtests.GqlApiController;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.DataProvider;

public class GeneratorToolsRepository extends GqlApiController {

    String xpathAddNewType = "//span[contains(text(),'Add new type')]";
    String xpathAddNewProperty = "//span[contains(.,'Add new property')]/parent::button";
    String xpathSelectProperty = "//span[contains(.,'Select property')]/parent::button";
    String xpathSelectMapProperty = "//span[contains(.,'Select and map property to type')]/parent::button";
    String xpathAddNewFinder = "//span[text()='Add a finder']/parent::button";

    protected void goToGeneratorTools() {
        goToTools("jahia", "password");
        getDriver().navigate().to(getPath("/modules/sdl-generator-tools/tools/sdlGeneratorTools.jsp"));
    }

    protected void clickAdd() {
        WebElement addButton = findByXpath("//span[text()='Add']/parent::button");
        waitForElementToBeClickable(addButton);
        clickOn(addButton);
    }

    protected void clickClear(){
        WebElement clearButton = findByXpath("//button[contains(.,'Clear')]");
        clickOn(clearButton);
    }

    protected void clickNext(){
        WebElement nextButton = findByXpath("//button[contains(.,'Next')]");
        clickOn(nextButton);
    }

    protected void clickBack(){
        WebElement backButton = findByXpath("//button[contains(.,'Back')]");
        clickOn(backButton);
    }

    protected void clickUpdate(){
        WebElement updateButton = findByXpath("//button[contains(.,'Delete')]");
        clickOn(updateButton);
    }

    protected void clickDelete(){
        WebElement deleteButton = findByXpath("//button[contains(.,'Update')]");
        clickOn(deleteButton);
    }

    protected void addType(String nodeType, String typeName) {
        WebElement addNewTypeBtn = waitForElementToBeVisible(findByXpath(xpathAddNewType));
        Assert.assertTrue(addNewTypeBtn.isDisplayed(), "Failed to find Add new type button");

        clickOn(addNewTypeBtn);
        checkCreateTypeDialog();
        selectNodeType(nodeType, typeName);

        WebElement customTypeName = findByXpath("//input[@id='typeName']");
        String prefilledCustomName = customTypeName.getAttribute("value");
        String prefilledCustomNameLabel = customTypeName.getAttribute("value").substring(0, 1).toLowerCase() +
                customTypeName.getAttribute("value").substring(1);

        Assert.assertTrue(findByXpath("//p[contains(.,'Remove "+ prefilledCustomNameLabel +"ByPath and " + prefilledCustomNameLabel + "ById entry points')]").isDisplayed(),
                "Failed to display entry points for selected node type");
        clickAdd();

        Assert.assertTrue(findByXpath("(//span[contains(.,'"+ prefilledCustomName +"')])").isDisplayed(), "Failed to add type");
        Assert.assertTrue(findByXpath("//div[@class='ace_content'][contains(.,'type "+ prefilledCustomName +" @mapping(node: \""+ nodeType +"\") {    metadata: Metadata }')]").isDisplayed(),
                "Failed to create schema");

    }

    protected void selectNodeType(String nodeType, String searchTerm) {

        WebElement addNodeTypeInput = findByXpath("//div[@type='text']//input");

        performSendKeys(addNodeTypeInput, searchTerm);
        shortSleep();

        Assert.assertTrue(findByXpath("//div/p[contains(.,'"+nodeType+"')]").isDisplayed(), "node type was not on the list");

        clickOn(findByXpath("//div/p[contains(.,'"+nodeType+"')]"));
    }

    protected void addProperty(String property, String propertyName) {
        clickOn(findByXpath(xpathAddNewProperty));

        waitForElementToBeClickable(findByXpath(xpathSelectProperty));
        clickOn(findByXpath(xpathSelectProperty));
        shortSleep();

        checkAddPropertyDialog();

        clickOn(findByXpath("//div[contains(@aria-haspopup,'true')]"));
        shortSleep();

        clickOn(findByXpath("//li[@data-value='"+property+"']"));
        WebElement propertyInput = findByXpath("//input[@id='propertyName']");
        propertyInput.clear();
        propertyInput.sendKeys(propertyName);

        clickAdd();
    }

    protected void addMapPropertyToType(String predefinedType, String property, String propertyName) {
        clickOn(findByXpath(xpathAddNewProperty));

        WebElement mapPropertyButton = waitForElementToBeClickable(findByXpath(xpathSelectMapProperty));
        clickOn(mapPropertyButton);
        waitForElementToBeVisible(findByID("form-dialog-title"));

        checkMapPropertyDialog();

        clickOn(findElementsByXpath("//div[contains(@aria-haspopup,'true')]").get(0));
        clickOn(findByXpath("//li[@data-value='" + predefinedType + "']"));
        Assert.assertTrue(findByXpath("//span[contains(.,'" +predefinedType+ "')]/parent::div").isDisplayed(), "Failed to select predefined type");


        clickOn(findElementsByXpath("//div[contains(@aria-haspopup,'true')]").get(1));
        clickOn(findByXpath("//li[@data-value='" + property + "']"));
        Assert.assertTrue(findByXpath("//span[contains(.,'" + property + "')]/parent::div").isDisplayed(), "Failed to select predefined type");


        WebElement propertyNameInput = findByXpath("//input[@id='propertyName']");
        performSendKeys(propertyNameInput, propertyName);
        Assert.assertTrue(propertyNameInput.getAttribute("value").contains(propertyName), "Failed to enter property name");
    }



        protected void addFinder(String finder, String customName) {
        waitForElementToBeVisible(findByXpath(xpathAddNewFinder));

        clickOn(findByXpath(xpathAddNewFinder));

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

        clickAdd();
    }

    protected void checkAddPropertyDialog() {
        Assert.assertTrue(waitForElementToBeVisible(findByXpath("//h2[contains(text(),'Add new property')]")).isDisplayed(),
                "Add new property dialog box failed to open");
        Assert.assertTrue(findByXpath("//p[contains(text(),'Select a property')]").isDisplayed(),
                "Add new property dialog box failed to open");
        Assert.assertTrue(findByXpath("//p[contains(text(),'Custom property name')]").isDisplayed(),
                "Add new property dialog box failed to open");
        Assert.assertTrue(findByXpath("//input[@id='propertyName']").isDisplayed(),
                "Add new property dialog box failed to open");
        Assert.assertTrue(findByXpath("//span[contains(., 'Cancel')]/parent::button").isDisplayed(),
                "Add property dialog box failed to load as expected");
        Assert.assertTrue(findByXpath("//span[text()='Add']/parent::button").isDisplayed(),
                "Add property dialog box failed to load as expected");
    }

    protected void checkMapPropertyDialog() {
        checkAddPropertyDialog();
        Assert.assertTrue(findByXpath("//p[contains(text(),'Predefined type')]").isDisplayed(),
                "Add new property dialog box failed to open");
    }

    protected void checkCreateTypeDialog() {
        Assert.assertTrue(waitForElementToBeVisible(findByXpath("//h2[contains(., 'Add new type')]")).isDisplayed(),
                "Add new type dialog box failed to load as expected");
        Assert.assertTrue(findElementsByXpath("//div[contains(.,'Select or search a node')]").get(9).isDisplayed(),
                "Add new type dialog box failed to load as expected");
        Assert.assertTrue(findByXpath("//label/p[contains(., 'Custom type name')]").isDisplayed(),
                "Add new type dialog box failed to load as expected");
        Assert.assertTrue(findByXpath("//input[@id='typeName']").isDisplayed(),
                "Add new type dialog box failed to load as expected");
        Assert.assertTrue(findByXpath("//p[contains(., 'Remove ByPath and ById entry points')]").isDisplayed(),
                "Add new type dialog box failed to load as expected");
        Assert.assertTrue(findByXpath("//span[contains(., 'Cancel')]/parent::button").isDisplayed(),
                "Add new type dialog box failed to load as expected");
        Assert.assertTrue(findByXpath("//span[text()='Add']/parent::button").isDisplayed(),
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
        Assert.assertTrue(findByXpath("//span[contains(., 'Cancel')]/parent::button").isDisplayed(),
                "Add new type dialog box failed to load as expected");
        Assert.assertTrue(findByXpath("//span[text()='Add']/parent::button").isDisplayed(),
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
        Assert.assertTrue(findByXpath("//span[contains(., 'Cancel')]/parent::button").isDisplayed(),
                "Cancel button is not visible");
        Assert.assertTrue(findByXpath("//span[text()='Add']/parent::button").isDisplayed(),
                "Save button is not visible");
    }

    @DataProvider(name = "nodeTypeList")
    public Object[][] createNodeTypeLists() {
        return new Object[][]{
                new Object[]{"article","jnt:article", 2},
                {"para","jnt:paragraph", 5},
                {"news","jnt:news", 5},
                {"bann","jnt:banner", 2},
                {"company","jdnt:company", 5},
                {"text","jnt:bigText", 34},
                {"content","jnt:content", 30}
        };
    }

    @DataProvider(name = "propertiesList")
    public Object[][] createPropertiesLists() {
        return new Object[][]{
                new Object[]{"jnt:article","article", 10},
                {"jnt:news","news", 9},
                {"jnt:banner","bann", 10},
                {"jdnt:company","company", 12},
                {"jnt:bigText","text", 7},
                {"jnt:content","content", 15}
        };
    }

    @DataProvider(name = "typeList")
    public Object[][] typeList() {
        return new Object[][]{
                new Object[]{"jnt:article","article"},
                {"jnt:paragraph", "paragraph"},
                {"jnt:news", "news"},
                {"jnt:banner", "banner"},
                {"jdnt:company", "company"},
                {"jnt:bigText", "Text"},
                {"jnt:content", "content"}
        };
    }

}
