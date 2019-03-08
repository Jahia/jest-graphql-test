package org.jahia.modules.sdlGeneratorTools;

import org.jahia.modules.graphQLtests.GqlApiController;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
/**
 * Created by astrit on 2019-03-07.
 */
public class EditSDLTest extends GqlApiController {

    @Test(alwaysRun = true)
    public void editTypeTest() {
        goToTools("jahia", "password");
        getDriver().navigate().to(getPath("/modules/sdl-generator-tools/tools/sdlGeneratorTools.jsp"));

        addAType("Article", "Article");

        WebElement editTypeBtn = findByXpath("//button[@aria-label='Edit']");
        Assert.assertTrue(editTypeBtn.isDisplayed(), "edit button for a created type doesn't exist");

        editTypeBtn.click();

        checkEditTypeDialog();

        WebElement ignoreDefQueriesSwitch = findByXpath("//input[contains(@class,'MuiPrivateSwitchBase-input-')]");

        ignoreDefQueriesSwitch.click();
        findByXpath("//span[contains(text(), 'Save')]").click();

        List<WebElement> divsInSchemaView = findElementsByXpath("//div[@id='gqlschema']/div[2]/div/div[3]/div");
        Assert.assertEquals(divsInSchemaView.get(0).findElements(By.tagName("span")).size(), 11, "the schema did not update");

        editTypeBtn.click();

        checkEditTypeDialog();

        ignoreDefQueriesSwitch = findByXpath("//input[contains(@class,'MuiPrivateSwitchBase-input-')]");
        ignoreDefQueriesSwitch.click();
        findByXpath("//span[contains(text(), 'Save')]").click();

        Assert.assertEquals(divsInSchemaView.get(0).findElements(By.tagName("span")).size(), 9, "the schema did not update");
    }

    private void checkEditTypeDialog() {
        Assert.assertTrue(findByXpath("//h2[contains(text(), 'Edit type')]").isDisplayed(),
                "Add new type dialog box failed to load as expected");
        Assert.assertTrue(findByXpath("//label[contains(text(), 'Select a node type')]").isDisplayed(),
                "Add new type dialog box failed to load as expected");
        Assert.assertTrue(findByXpath("//label[contains(text(), 'Custom type name')]").isDisplayed(),
                "Add new type dialog box failed to load as expected");
        Assert.assertTrue(findByXpath("//input[@id='typeName']").isDisplayed(),
                "Add new type dialog box failed to load as expected");
        Assert.assertTrue(findByXpath("//span[contains(text(), 'Ignore Default Queries')]").isDisplayed(),
                "Add new type dialog box failed to load as expected");
        Assert.assertTrue(findByXpath("//span[contains(text(), 'Cancel')]").isDisplayed(),
                "Add new type dialog box failed to load as expected");
        Assert.assertTrue(findByXpath("//span[contains(text(), 'Save')]").isDisplayed(),
                "Add new type dialog box failed to load as expected");
    }

}
