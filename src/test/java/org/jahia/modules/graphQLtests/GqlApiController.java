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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
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


    @BeforeSuite
    public void importDigitall(){
        getDriver().navigate().to(getPath("/cms/admin/default/en/settings.webProjectSettings.html"));
        switchToDXAdminFrame();

        WebElement site = findByXpath("//a[contains(.,'Digitall')]");
        if(site == null){
            performSelectDropdownVisibleText(findByName("selectedPrepackagedSite"), "Digitall Prepackaged Demo Website");
            clickOn(findByName("importPrepackaged"));
            clickOn(findByXpath("//button[contains(.,'Import')]"));

            waitForElementToBeInvisible(By.xpath("//div[contains(., 'Work in progress, please wait...')]"));
            sleepMultipleTime(60);
            waitForWorkToEnd();

            Assert.assertNotNull(waitForElementToBeVisible(findByXpath("//a[contains(.,'Digitall')]")), "Failed to import Digitall site");
        }
        else {
            Assert.assertNotNull(waitForElementToBeVisible(findByXpath("//a[contains(.,'Digitall')]")), "Failed to import Digitall site");
            logger.info("Site already exists");
        }
    }

    @BeforeClass
    public void setHttpClient(){
        httpClient = new HttpClient();
        httpClient.getState().setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("jahia", "password"));
    }

    @AfterSuite
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
        driver.switchTo().frame(findByXpath("//iframe[contains(@src,'/cms/adminframe/default/en/settings.webProjectSettings.html')]"));
    }

    private String getsdlreporttoolPath() {
        return getBaseURL()+ "/modules/graphql-dxm-provider/tools/sdlreporttool.jsp";
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

}
