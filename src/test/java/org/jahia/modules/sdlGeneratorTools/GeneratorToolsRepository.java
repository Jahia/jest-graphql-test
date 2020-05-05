package org.jahia.modules.sdlGeneratorTools;

import org.apache.commons.lang.StringUtils;
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
    String xpathDeleteButton = "//button[contains(.,'Delete')]";
    String xpathUpdateButton = "//button[contains(.,'Update')]";
    String xpathBackButton = "//span[text()='Back']/parent::button";
    String xpathNextButton = "//button[contains(.,'Next')]";
    String xpathClearButton = "//button[contains(.,'Clear')]";


    protected void goToGeneratorTools() {
        goToTools("jahia", "password");
        getDriver().get(getPath("/modules/sdl-generator-tools/tools/sdlGeneratorTools.jsp"));
        waitForLoad(getDriver());
        sleep(1000);
    }

    protected void clickAdd() {
        WebElement addButton = findByXpath("//span[text()='Add']/parent::button");
        verifyElementEnabled(addButton, 6);
        clickOn(addButton);
    }

    protected void clickClear(){
        WebElement clearButton = findByXpath(xpathClearButton);
        clickOn(clearButton);
    }

    protected void clickNext(){
        WebElement nextButton = findByXpath(xpathNextButton);
        clickOn(nextButton);
    }

    protected void clickBack(){
        WebElement backButton = findByXpath(xpathBackButton);
        clickOn(backButton);
    }

    protected void clickUpdate(){
        WebElement updateButton = findByXpath(xpathUpdateButton);
        clickOn(updateButton);
    }

    protected void clickDelete(){
        WebElement deleteButton = findByXpath(xpathDeleteButton);
        clickOn(deleteButton);
    }

    protected void backToTools() {
        clickOn(findByXpath("//button/span/p[contains(., 'Back to tools')]"));
        assertTitle("Jahia Support Tools");

        Assert.assertTrue(findByXpath("//h1[contains(.,'Support Tools (Jahia')]").isDisplayed(), "Failed to locate Export result text");
    }

    protected void addType(String nodeType, String typeName) {
        WebElement addNewTypeBtn = findByXpath(xpathAddNewType);
        Assert.assertTrue(addNewTypeBtn.isDisplayed(), "Failed to find Add new type button");

        clickOn(addNewTypeBtn);
       // checkCreateTypeDialog();
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

        Assert.assertTrue(findByXpath("//div/p[contains(.,'"+nodeType+"')]").isDisplayed(), "node type was not on the list");
        clickOn(findByXpath("//div/p[contains(.,'"+nodeType+"')]"));
    }

    protected void addProperty(String property, String propertyName) {
        clickOn(findByXpath(xpathAddNewProperty));

        verifyElementClickable(findByXpath(xpathSelectProperty));
        clickOn(findByXpath(xpathSelectProperty));

        checkAddPropertyDialog();

        clickOn(findByXpath("//div[contains(@aria-haspopup,'true')]"));

        clickOn(findByXpath("//li[@data-value='"+property+"']"));
        WebElement propertyInput = findByXpath("//input[@id='propertyName']");
        propertyInput.clear();
        propertyInput.sendKeys(propertyName);

        clickAdd();
    }

    protected void addMapPropertyToType(String predefinedType, String property, String propertyName) {
        clickOn(findByXpath(xpathAddNewProperty));

        WebElement mapPropertyButton = findByXpath(xpathSelectMapProperty);
        verifyElementClickable(findByXpath(xpathSelectMapProperty));
        clickOn(mapPropertyButton);
        verifyElementDisplayed(findByID("form-dialog-title"));

        checkMapPropertyDialog();

        clickOn(findElementsByXpath("//div[contains(@aria-haspopup,'true')]").get(0));
        clickOn(findByXpath("//li[@data-value='" + predefinedType + "']"));
        Assert.assertTrue(findByXpath("//span[contains(.,'" +predefinedType+ "')]/parent::div").isDisplayed(), "Failed to select predefined type");

        verifyElementDisplayed(findElementsByXpath("//div[contains(@aria-haspopup,'true')]").get(1));
        clickOn(findElementsByXpath("//div[contains(@aria-haspopup,'true')]").get(1));
        clickOn(findByXpath("//li[@data-value='" + property + "']"));
        Assert.assertTrue(findByXpath("//span[contains(.,'" + property + "')]/parent::div").isDisplayed(), "Failed to select predefined type");


        WebElement propertyNameInput = findByXpath("//input[@id='propertyName']");
        performSendKeys(propertyNameInput, propertyName);
        Assert.assertTrue(propertyNameInput.getAttribute("value").contains(propertyName), "Failed to enter property name");

        clickAdd();
    }



        protected void addFinder(String finder, String customName) {
        verifyElementDisplayed(findByXpath(xpathAddNewFinder));

        clickOn(findByXpath(xpathAddNewFinder));

        checkAddFinderDialog();

        clickOn(findByXpath("//input[@id='finder-name']/parent::div/div"));

        verifyElementDisplayed(findByXpath("//ul[@role='listbox']"));

        clickOn(findByXpath("//li[@data-value='"+finder+"']"));

        findByXpath("//input[@id='propertyName']").clear();
        findByXpath("//input[@id='propertyName']").sendKeys(customName);

        if (finder.equals("all")) {
            Assert.assertEquals(findByXpath("//span[text()='all']/parent::p").getText().toLowerCase(), finder+customName, "the finder preview is incorrect");

        } else if (finder.equals("allConnection")) {
            Assert.assertTrue(findByXpath("//span[text()='all']/parent::p").isDisplayed(), "all finder not found");
            Assert.assertTrue(findByXpath("//span[text()='Connection']/parent::p").isDisplayed(), "all finder not found");

            Assert.assertTrue(findByXpath("//p/em[text()='"+ StringUtils.capitalize(customName) +"']").isDisplayed(), "Finder " + StringUtils.capitalize(customName) + "not found");

        } else {
            Assert.assertTrue(findByXpath("//span[text()='"+ StringUtils.capitalize(finder)+"']/parent::p").isDisplayed(), finder +" not added in add Finder dialog box");
            Assert.assertTrue(findByXpath("//em[text()='"+ customName +"']/parent::p").isDisplayed(), customName + "not found in add Finder dialog box");
        }

        clickAdd();
    }

    protected void checkAddPropertyDialog() {
        verifyElementDisplayed(findByXpath("//h2[contains(text(),'Add new property')]"));

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
        verifyElementDisplayed(findByXpath("//h2[contains(., 'Add new type')]"));

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
                {"content","jnt:content", 31}
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
