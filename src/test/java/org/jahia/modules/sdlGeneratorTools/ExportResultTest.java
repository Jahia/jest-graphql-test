package org.jahia.modules.sdlGeneratorTools;

import org.apache.commons.io.FileUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

public class ExportResultTest extends GeneratorToolsRepository {

    private String downloadsFolderPath = new File(getDownloadsFolder()).getAbsolutePath();
    private File downloadedSDL = null;

    @BeforeMethod(alwaysRun = true)
    protected void cleanDownloadsFolder() {
        downloadedSDL = null;

        try {
            FileUtils.cleanDirectory(new File(downloadsFolderPath));
        } catch (IOException e) {
            getLogger().error(e.getMessage());
        } catch (IllegalArgumentException ee){
            getLogger().error(ee.getMessage());
        }
    }

    @Test(alwaysRun = true)
    protected void downloadAsAFileTest(){
        goToGeneratorTools();
        clickClear();


        addType("jnt:news", "NewsEntry");
        addProperty("jcr:title", "title");

        clickNext();
        addFinder("all", "news");

        Assert.assertTrue(findByXpath("//div[@class='ace_content'][contains(.,'type NewsEntry @mapping(node: \"jnt:news\") " +
                        "{    metadata: Metadata     title: String @mapping(property: \"jcr:title\")}extend type Query {    allNews: [NewsEntry]}')]").isDisplayed(),
                "Failed to create schema");

        clickNext();
        Assert.assertTrue(findByXpath("//p[contains(.,'All types have been correctly configured. You can now export your SDL file')]").isDisplayed());
        clickOn(findByXpath("//button[contains(.,'Download as a file')]"));

        downloadedSDL = waitForFile(downloadsFolderPath, "", ".sdl", 10000L);
        Assert.assertNotNull(downloadedSDL, "No .sdl files found in the Downloads folder after clicking 'Download' button. Downloads folder is: " + downloadsFolderPath);
        System.out.println(downloadedSDL + "... " + downloadsFolderPath);
    }

    @Test(alwaysRun = true)
    protected void copyToClipboardTest() {
        goToGeneratorTools();
        clickClear();

        addType("jnt:news", "NewsEntry");
        addProperty("jcr:title", "title");

        clickNext();
        addFinder("all", "news");

        Assert.assertTrue(findByXpath("//div[@class='ace_content'][contains(.,'type NewsEntry @mapping(node: \"jnt:news\") " +
                        "{    metadata: Metadata     title: String @mapping(property: \"jcr:title\")}extend type Query {    allNews: [NewsEntry]}')]").isDisplayed(),
                "Failed to create schema");
        clickNext();
        Assert.assertTrue(findByXpath("//p[contains(.,'All types have been correctly configured. You can now export your SDL file')]").isDisplayed());
        clickOn(findByXpath("//button[contains(.,'Copy to clipboard')]"));

        String expectedSchema= "type NewsEntry @mapping(node: \"jnt:news\") {\n" +
        "\tmetadata: Metadata \n" +
        "\ttitle: String @mapping(property: \"jcr:title\")\n" +
        "}\n" +
        "\n" +
        "extend type Query {\n" +
        "\tallNews: [NewsEntry]\n" +
        "}";

        System.setProperty("java.awt.headless", "false");
        //display what is currently on the clipboard
        log("Clipboard contains:" + getClipboardContents());

        Assert.assertEquals(getClipboardContents(), expectedSchema, "Copy to Clipboard failed to copy the schema");
    }

}
