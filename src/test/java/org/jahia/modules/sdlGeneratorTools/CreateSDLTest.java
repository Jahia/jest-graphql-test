package org.jahia.modules.sdlGeneratorTools;


import org.jahia.modules.graphQLtests.GqlApiController;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by parveer on 2019-03-01.
 */
public class CreateSDLTest extends GqlApiController {


    @Test(alwaysRun = true)
    public void navigateTest() {

        goToTools("jahia", "password");
        getDriver().navigate().to(getPath("/modules/sdl-generator-tools/tools/sdlGeneratorTools.jsp"));


        Assert.assertTrue(findByXpath("//p[contains(., 'SDL Generator Tools')]").isDisplayed(), "Failed to locate header SDL Generator Tools");
        Assert.assertTrue(findByXpath("//p[contains(., 'Build your GraphQL')]").isDisplayed(), "Failed to navigate to SDL Generator Tools page");

        Assert.assertTrue(findByXpath("//span[contains(., 'Create types')]").isDisplayed(), "Failed to locate Create types text");
        Assert.assertTrue(findByXpath("//span[contains(., 'Define finder')]").isDisplayed(), "Failed to locate Define finder text");
        Assert.assertTrue(findByXpath("//span[contains(., 'Export result')]").isDisplayed(), "Failed to locate Export result text");


        clickOn(findByXpath("//button/span[contains(., 'Back to tools')]"));
        Assert.assertTrue(findByXpath("//h1[contains(., 'Support Tools')]").isDisplayed(), "Failed to locate Export result text");
    }


    @Test(alwaysRun = true, dependsOnMethods = "navigateTest")
    public void createTypeTest() {
        goToTools("jahia", "password");
        getDriver().navigate().to(getPath("/modules/sdl-generator-tools/tools/sdlGeneratorTools.jsp"));

        WebElement addNewTypeBtn = findByXpath("//span[contains(text(),'Add new type')]");
        Assert.assertTrue(addNewTypeBtn.isDisplayed(), "Failed to find Add new type button");

        addNewTypeBtn.click();

    }
}
