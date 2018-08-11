/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package restcontroller;

import ConstantVariable.Constant;
import Service.CheckCapcha;
import Service.Codenvy;
import Service.CreateWebdriver;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class Index {

    private static boolean flag = false;
    @Autowired
    CreateWebdriver createWebdriver;
    @Autowired
    Codenvy codenvy;
    @Autowired
    CheckCapcha checkCapcha;
    WebDriver webDriver;

    @RequestMapping(value = "/setCap", method = RequestMethod.GET)
    public @ResponseBody String setCap(
            @RequestParam(value = "captext", defaultValue = "", required = false) String captext
    ) {
        return checkCapcha.Check(webDriver, captext);
            
    }

    @RequestMapping(value = "/getCapTypeBase64", method = RequestMethod.GET)
    public String getCapTypeBase64() {
        try {
            if (!flag) {
                webDriver = createWebdriver.getGoogle(Constant.binaryGoogleHeroku);
                codenvy.Start(webDriver, "tokudaJAVhd50cmBBwbigco");

                // viet code gui capcha base64 string sang 2capcha
            }
        } catch (Exception e) {
            e.getMessage();
        }
        return "index";
    }

    @RequestMapping(value = "/getCapTypeImg",
            method = RequestMethod.GET,
            produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody
    byte[] getCapTypeImg() {
        try {
            if (!flag) {
                webDriver = createWebdriver.getGoogle(Constant.binaryGoogleHeroku);
                return codenvy.Start(webDriver, "samuraiJAVhd50cmBBc");
            }
        } catch (Exception e) {
            e.getMessage();
        }
        return null;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() {
        return "index";
    }
}
