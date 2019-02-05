package org.jahia.modules.graphQLtests;

import org.apache.commons.lang.ArrayUtils;
import org.jahia.api.Constants;
import org.jahia.services.content.JCRNodeWrapper;
import org.jahia.services.content.JCRTemplate;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class QueryAllTest extends GqlApiController {

    @Test
    public void allNewsTest() throws Exception {

        JSONObject result = executeQuery("{"
                + "    allNews {"
                + "         title"
                + "    }"
                + "}");

        JSONArray data = result.getJSONObject("data").getJSONArray("allNews");

        Assert.assertEquals(9, data.length());

    }
}


