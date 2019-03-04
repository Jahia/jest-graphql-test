package org.jahia.modules.sdlGeneratorTools;


import org.jahia.modules.graphQLtests.GqlApiController;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by parveer on 2019-03-01.
 */
public class CreateTypeTest extends GqlApiController {

    @Test(alwaysRun = true)
    public void navigateTest() {

        goToTools("jahia", "password");
        getDriver().navigate().to(getPath("/modules/sdl-generator-tools/tools/sdlGeneratorTools.jsp"));

        Assert.assertTrue(findByXpath("//p[contains(., 'Build your GraphQL')]").isDisplayed(), "Failed to navigate to SDL Generator Tools page");

    }
}
