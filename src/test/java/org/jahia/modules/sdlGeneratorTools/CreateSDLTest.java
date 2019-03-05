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

        Assert.assertTrue(findByXpath("//p[contains(., 'Build your GraphQL')]").isDisplayed(), "Failed to navigate to SDL Generator Tools page");

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
