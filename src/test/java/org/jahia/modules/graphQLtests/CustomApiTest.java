package org.jahia.modules.graphQLtests;

import org.drools.core.command.ExecuteCommand;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import java.io.File;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;


/**
 * Created by parveer on 2019-01-15.
 */
public class CustomApiTest extends GqlApiController {


    @Test(dataProvider = "getModuleData", alwaysRun = true)
    public void sdlReportToolTest(String moduleName, String moduleStatus) {
        getModuleStatus(moduleName, moduleStatus);

    }

    @DataProvider
    public Object[][] getModuleData() {
        return new Object[][]{
                {"graphql-dxm-provider", "success"},
                {"graphql-extension-example", "success"},
                {"sdl-tests", "success"}};
    }

    @Test(dependsOnMethods = "sdlReportToolTest", alwaysRun = true)
    public void errorManagement(){

        String extendsNonExistingType = "extendsNonExistingType";
        String fieldTypeDoesNotExist = "fieldTypeDoesNotExist";
        String nodeTypeDoesNotExist = "nodeTypeDoesNotExist";
        String propertyDoesNotExist = "propertyDoesNotExist";
        String redefinesExistingType = "redefinesExistingType ";
        String typos = "typos";
        String originalSdl = "original";

        //store the original Sdl file
        cpSdlFile(getPathToSdlFile(null, true), tempDirOfOriginalSdlFile() + "/");

        //replace sdl file in sdl-test-module with extendsNonExistingType SDL file
        cpSdlFile(getPathToSdlFile(extendsNonExistingType, false), sdlFilePathInSdlTestModule() + "/");

        //Go to sdlReportTool and check that the expected errors are present
        goToSdlReportTool();

        Assert.assertTrue(findByXpath(
                "//div[contains(.,'errors=[InvalidSyntaxError{ message=Invalid Syntax ,locations=[SourceLocation{line=3, column=13}]}]')]")
                .isDisplayed(), "Expected error is not visible");

        Assert.assertEquals(findElementsByXpath("//div[@class='status-icon error']").size(), 1);

        //replace sdl file in sdl-test-module with fieldTypeDoesNotExist SDL file
        cpSdlFile(getPathToSdlFile(fieldTypeDoesNotExist, false), sdlFilePathInSdlTestModule() + "/");

        goToSdlReportTool();

        Assert.assertTrue(findByXpath(
                "//div[contains(.,'DEFINITION: Slider maps to type null from module null with id null. STATUS: ListType{type=TypeName{name='SliderPanel'}} gql type is unavailable')]")
                .isDisplayed(), "Expected error is not visible");
        Assert.assertTrue(findByXpath(
                "//div[contains(.,'Definition Slider : ListType{type=TypeName{name='SliderPanel'}} gql type is unavailable')]")
                .isDisplayed(), "Expected error is not visible");
        Assert.assertTrue(findByXpath(
                "//div[contains(.,'Definition Query : ListType{type=TypeName{name='Slider'}} gql type is unavailable')]")
                .isDisplayed(), "Expected error is not visible");

        Assert.assertEquals(findElementsByXpath("//div[@class='status-icon error']").size(), 2);

        //replace sdl file in sdl-test-module with nodeTypeDoesNotExist SDL file
        cpSdlFile(getPathToSdlFile(nodeTypeDoesNotExist, false), sdlFilePathInSdlTestModule() + "/");

        goToSdlReportTool();

        Assert.assertTrue(findByXpath(
                "//div[contains(.,'DEFINITION: BoozSDL maps to type null from module null with id null. STATUS: jnt:booz node type was not found')]")
                .isDisplayed(), "Expected error is not visible");
        Assert.assertTrue(findByXpath(
                "//div[contains(.,'Definition BoozSDL : jnt:booz node type was not found')]")
                .isDisplayed(), "Expected error is not visible");
        Assert.assertTrue(findByXpath(
                "//div[contains(.,'Definition Query : ListType{type=TypeName{name='BoozSDL'}} gql type is unavailable')]")
                .isDisplayed(), "Expected error is not visible");

        Assert.assertEquals(findElementsByXpath("//div[@class='status-icon error']").size(), 2);

        //replace sdl file in sdl-test-module with propertyTypeDoesNotExist SDL file
        cpSdlFile(getPathToSdlFile(propertyDoesNotExist, false), sdlFilePathInSdlTestModule() + "/");

        goToSdlReportTool();

        Assert.assertTrue(findByXpath(
                "//div[contains(.,'DEFINITION: CalendarSDL maps to type jnt:calendar from module Jahia Calendar with id calendar. STATUS: jnt:startDate property is missing from node type')]")
                .isDisplayed(), "Expected error is not visible");
        Assert.assertTrue(findByXpath(
                "//div[contains(.,'Definition CalendarSDL : jnt:startDate property is missing from node type')]")
                .isDisplayed(), "Expected error is not visible");
        Assert.assertTrue(findByXpath(
                "//div[contains(.,'Definition Query : ListType{type=TypeName{name='CalendarSDL'}} gql type is unavailable')]")
                .isDisplayed(), "Expected error is not visible");

        Assert.assertEquals(findElementsByXpath("//div[@class='status-icon error']").size(), 2);

        //replace sdl file in sdl-test-module with redefinesExistingType SDL file
        cpSdlFile(getPathToSdlFile(redefinesExistingType, false), sdlFilePathInSdlTestModule() + "/");

        goToSdlReportTool();

        Assert.assertTrue(findByXpath(
                "//div[contains(.,'DEFINITION: Slider maps to type null from module null with id null. STATUS: ListType{type=TypeName{name='SliderPanel'}} gql type is unavailable')]")
                .isDisplayed(), "Expected error is not visible");
        Assert.assertTrue(findByXpath(
                "//div[contains(.,'errors=['Metadata' type [@18:1] tried to redefine existing 'Metadata' type [@1:1]]')]")
                .isDisplayed(), "Expected error is not visible");
        Assert.assertTrue(findByXpath(
                "//div[contains(.,'Definition Slider : ListType{type=TypeName{name='SliderPanel'}} gql type is unavailable')]")
                .isDisplayed(), "Expected error is not visible");
        Assert.assertTrue(findByXpath(
                "//div[contains(.,'Definition Query : ListType{type=TypeName{name='Slider'}} gql type is unavailable')]")
                .isDisplayed(), "Expected error is not visible");

        Assert.assertEquals(findElementsByXpath("//div[@class='status-icon error']").size(), 2);

        //replace sdl file in sdl-test-module with typos SDL file
        cpSdlFile(getPathToSdlFile(typos, false), sdlFilePathInSdlTestModule() + "/");

        goToSdlReportTool();

        Assert.assertTrue(findByXpath(
                "//div[contains(.,'errors=[InvalidSyntaxError{ message=Invalid Syntax ,locations=[SourceLocation{line=8, column=26}]}]')]")
                .isDisplayed(), "Expected error is not visible");

        Assert.assertEquals(findElementsByXpath("//div[@class='status-icon error']").size(), 1);

        //replace sdl file in sdl-test-module with the original SDL file
        cpSdlFile(getPathToSdlFile(originalSdl, false), sdlFilePathInSdlTestModule() + "/");
    }

    //Get path to a specific SDL file
    private String getPathToSdlFile(String sdlDescription, Boolean isOriginal) {

        StringBuilder sdlPath = new StringBuilder();

        Process process;
        try {
            if (!isOriginal) {
                process = Runtime.getRuntime().exec(
                        new String[]{"bash", "-c", "find ~ -path '*jest-graphql-test/resources/sdl-extension-files/" + sdlDescription + "/*.sdl'"});
                process.waitFor();
                BufferedReader reader =
                        new BufferedReader(new InputStreamReader(process.getInputStream()));

                String line;
                while ((line = reader.readLine()) != null || !(line = reader.readLine()).equals("")) {
                    sdlPath.append(line);
                }
            } else {
                process = Runtime.getRuntime().exec(
                        new String[]{"bash", "-c", "find ~ -path '*sdl-test-module/src/main/resources/META-INF/*.sdl'"});
                process.waitFor();
                BufferedReader reader =
                        new BufferedReader(new InputStreamReader(process.getInputStream()));

                String line;
                while ((line = reader.readLine()) != null || !(line = reader.readLine()).equals("")) {
                    System.out.print(line);
                    sdlPath.append(line);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return sdlPath.toString();
    }

    //Get path to tempDir for where the original sdl will be/is stored
    private String sdlFilePathInSdlTestModule() {

        StringBuilder tempDirPath = new StringBuilder();

        Process p;
        try {
            p = Runtime.getRuntime().exec(
                    new String[]{"bash", "-c", "find ~ -path '*sdl-test-module/src/main/resources/META-INF'"});
            p.waitFor();
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line = "";
            while ((line = reader.readLine())!= null) {
                tempDirPath.append(line);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return tempDirPath.toString();
    }

    //Get path to sdl file located in the sdl-test-module
    private String tempDirOfOriginalSdlFile() {

        StringBuilder sdlPath = new StringBuilder();

        Process p;
        try {
            p = Runtime.getRuntime().exec(
                    new String[]{"bash", "-c", "find ~ -path '*jest-graphql-test/resources/sdl-extension-files/original'"});
            p.waitFor();
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line = "";
            while ((line = reader.readLine())!= null) {
                sdlPath.append(line);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return sdlPath.toString();
    }

    //copy / paste an SDL file
    private void cpSdlFile(String sdlPath, String sdlLocation) {

        ProcessBuilder builder = new ProcessBuilder();

        builder.command("bash", "-c", "cp "+ sdlPath +" "+ sdlLocation);

        try {
            Process process = builder.start();

            int exitCode = process.waitFor();
            System.out.println("\nExited with error code : " + exitCode);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
