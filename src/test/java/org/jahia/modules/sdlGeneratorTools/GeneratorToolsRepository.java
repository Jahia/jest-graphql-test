package org.jahia.modules.sdlGeneratorTools;

import org.jahia.modules.graphQLtests.GqlApiController;
import org.openqa.selenium.By;
import org.testng.annotations.DataProvider;

public class GeneratorToolsRepository extends GqlApiController {


    @DataProvider(name = "nodeTypeList")
    public Object[][] createSNodeTypeLists() {
        return new Object[][]{
            new Object[]{"article","jnt:article", 1},
                {"para","jnt:paragraph", 3},
                {"news","jnt:news", 4},
                {"bann","jnt:banner", 2},
                {"company","jdnt:company", 2},
                {"text","jnt:bigText", 9},
                {"content","jnt:content", 25}
        };
    }

}
