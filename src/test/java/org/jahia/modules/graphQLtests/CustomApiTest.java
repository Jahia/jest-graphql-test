package org.jahia.modules.graphQLtests;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Created by parveer on 2019-01-15.
 */
public class CustomApiTest extends GqlApiController{


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
        //TODO
    }
}
