package org.jahia.modules.graphQLtests;


import net.htmlparser.jericho.Attribute;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.jahia.modules.tests.core.ModuleTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;

import java.io.IOException;
import java.util.List;


/**
 * Created by parveer on 2019-01-15.
 */
public class GqlApiController extends ModuleTest {
    private static Logger logger = LoggerFactory.getLogger(GqlApiController.class);

    private HttpClient httpClient;
    GetMethod get = new GetMethod(getsdlreporttoolPath());

   // private static OsgiGraphQLServlet servlet;

   // public static void setServlet(OsgiGraphQLServlet servlet) {
   //     GqlApiController.servlet = servlet;
   // }

    @BeforeSuite
    public void importDigitall() {
        getDriver().navigate().to(getPath("/cms/admin/default/en/settings.webProjectSettings.html"));
        switchToDXAdminFrame();

        WebElement site = null;

        try {
            site = createWaitDriver().until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[contains(.,'Digitall')]")));
        } catch (Exception e) {
            getLogger().error("Site not found {}", "Digitall");
        }

        if (site == null) {
            performSelectDropdownVisibleText(findByName("selectedPrepackagedSite"), "Digitall Prepackaged Demo Website");
            clickOn(findByName("importPrepackaged"));
            clickOn(findByXpath("//button[contains(.,'Import')]"));
            sleep();
            driver.switchTo().parentFrame();
            waitForElementToDisappear(findByXpath("//*[@id=\"x-auto-19\"]/*/div[contains(., 'Work in progress, please wait...')]"),600);
            switchToDXAdminFrame();
            Assert.assertNotNull(waitForElementToBeVisible(findByXpath("//a[contains(.,'Digitall')]")), "Failed to import Digitall site");
        } else {
            Assert.assertNotNull(waitForElementToBeVisible(findByXpath("//a[contains(.,'Digitall')]")), "Failed to import Digitall site");
            getLogger().info("Site already exists");
        }
    }

    @BeforeClass
    public void setHttpClient(){
        httpClient = new HttpClient();
        httpClient.getState().setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("jahia", "password"));
    }

//    @AfterSuite
    public void deleteSite(){
        getDriver().navigate().to(getPath("/cms/admin/default/en/settings.webProjectSettings.html"));

        switchToDXAdminFrame();

        List<WebElement> sites = findElementsByXpath("//a[@title='Delete']");
        for(WebElement deleteButton:sites) {
            int i=0;
            clickOn(sites.get(i));

            clickOn(deleteButton);
            waitForElementToBeInvisible(By.xpath("//div[contains(., 'Work in progress, please wait...')]"));
            sleepMultipleTime(60);
            waitForWorkToEnd();
        }

    }

    private void switchToDXAdminFrame() {
        getDriver().switchTo().frame(findByXpath("//iframe[contains(@src,'/cms/adminframe/default/en/settings.webProjectSettings')]"));
    }

    private String getsdlreporttoolPath() {
        return getBaseURL()+ "/modules/graphql-dxm-provider/tools/sdlreporttool.jsp";
    }

    private String getSdlGeneratorToolPath() {
        return getBaseURL()+ "/modules/sdl-generator-tools/tools/sdlGeneratorTools.jsp";
    }

    public void getSourceOfSDLReportTool(HttpClient client) {

        try {
            get.setDoAuthentication(true);
            String url = getsdlreporttoolPath();
            get = new GetMethod(url);
            int statusCode = client.executeMethod(get);
            Assert.assertEquals(HttpStatus.SC_OK, statusCode, "Failed");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected void getModuleStatus(String moduleName, String expectedStatus) {

        getSourceOfSDLReportTool(httpClient);
        Source source = null;
        try {
            source = new Source(get.getResponseBodyAsStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Element> elems = source.getAllElementsByClass("status-row");

        Element found = null;
        for (Element el: elems) {
            for (Attribute attr: el.getAttributes()) {
                if (attr.getName().equals("data-sdl-module") && attr.getValue().equals(moduleName)) {
                    found = el;
                    break;
                }
            }
        }
        String Actualstatus = found.getChildElements().get(2).getAttributeValue("data-sdl-module-status");
        System.out.println("Actual status is : " + Actualstatus);
        Assert.assertEquals(Actualstatus, expectedStatus, "Module status has error");
    }

    protected void goToSdlReportTool() {
        goToTools("jahia", "password");
        getDriver().navigate().to(getPath("/modules/graphql-dxm-provider/tools/sdlreporttool.jsp"));
    }

//    protected static JSONObject executeQuery(String query) throws JSONException {
//        String result = servlet.executeQuery(query);
//        if (result != null) {
//            if (result.contains("Validation error")) {
//                logger.error("Validation error {} for query: {}", result, query);
//            } else if (result.contains("Invalid Syntax")) {
//                logger.error("Invalid Syntax\" {} for query: {}", result, query);
//            }
//        }
//        return new JSONObject(result);
//    }

//    protected static Map<String, JSONObject> toItemByKeyMap(String key, JSONArray items) throws JSONException {
//        HashMap<String, JSONObject> itemByName = new HashMap<JSONArray>(items.length());
//        for (int i = 0; i < items.length(); i++) {
//            JSONObject item = items.getJSONObject(i);
//            itemByName.put(item.getString(key), item);
//        }
//        return itemByName;
//    }

}
