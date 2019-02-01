package org.jahia.modules.graphQLtests;

import org.testng.annotations.Test;

/**
 * Created by parveer on 2019-01-15.
 */
public class CustomApiTest extends GqlApiController{


    @Test()
    public void sdlReportToolTest() {
        getModuleStatus("graphql-dxm-provider", "success");

        getModuleStatus("graphql-extension-example", "success");

        getModuleStatus("sdl-tests", "success");
    }
}
