package org.jahia.modules.sdlGeneratorTools;

import org.testng.Assert;
import org.testng.annotations.Test;

public class PropertiesTest extends GeneratorToolsRepository {

    @Test(alwaysRun = true)
    public void addPropertyTest() {
        goToTools("jahia", "password");
        getDriver().navigate().to(getPath("/modules/sdl-generator-tools/tools/sdlGeneratorTools.jsp"));

        addAType("Article", "Article");

        clickOn(findByXpath("//span/p[contains(., 'Add a new property')]"));
        Assert.assertTrue(findByID("form-dialog-title").isDisplayed(), "Failed to display Add new property dialog box");



    }



}