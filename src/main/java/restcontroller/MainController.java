/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package restcontroller;

import ConstantVariable.VariableSession;
import ConstantVariable.Constant;
import Service.CheckCapcha;
import Service.Codenvy;
import Service.CreateWebdriver;
import Service.ProxyWithSSH;
import java.io.IOException;
import java.net.URL;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {

    @Autowired
    CreateWebdriver createWebdriver;
    @Autowired
    Codenvy codenvy;
    @Autowired
    CheckCapcha checkCapcha;
    @Autowired
    ProxyWithSSH proxyWithSSH;
    WebDriver webDriver = null;

    @RequestMapping(value = "/startAuto", method = RequestMethod.GET)
    public @ResponseBody
    String getCapTypeBase64() {
        try {
            if (VariableSession.flag_status_is_first_run_app) {
                webDriver = createWebdriver.getGoogle(Constant.binaryGoogleWindows);
                VariableSession.flag_status_is_first_run_app = false;
            }
            Thread startThread = new Thread() {
                @Override
                public void run() {
                    try {
                        codenvy.Start(webDriver);
                    } catch (Exception e) {
                        e.getMessage();
                    }
                }
            };
            startThread.start();
        } catch (Exception e) {
            e.getMessage();
        }
        return "running";
    }

//    @RequestMapping(value = "/getCapTypeImg",
//            method = RequestMethod.GET,
//            produces = MediaType.IMAGE_JPEG_VALUE)
//    public @ResponseBody
//    byte[] getCapTypeImg() {
//        try {
//            if (VariableSession.flag_status_is_first_run_app) {
//                if (webDriver != null) {
//                    webDriver.manage().deleteAllCookies();
//                } else {
//                    webDriver = createWebdriver.getGoogle(Constant.binaryGoogleHeroku);
//                }
//                VariableSession.flag_status_is_first_run_proxy = false;
//                return codenvy.Start(webDriver);
//            }
//        } catch (Exception e) {
//            e.getMessage();
//        }
//        return null;
//    }
    @RequestMapping(value = "/startProxy", method = RequestMethod.GET)
    public @ResponseBody
    String startProxy() throws IOException {
        try {
            if (VariableSession.flag_status_is_first_run_proxy) {
                Thread startThread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            VariableSession.flag_status_is_first_run_proxy = false;
                            URL Urlssh = MainController.class
                                    .getClassLoader().getResource("ssh.txt");
                            proxyWithSSH.setting(Urlssh.getPath());
                            proxyWithSSH.start();
                        } catch (Exception e) {
                            e.getMessage();
                        }
                    }
                };
                startThread.start();
            }

        } catch (Exception e) {
            e.getMessage();
            return "loi : " + e.getMessage();
        }
        return "running";
    }

    @RequestMapping(value = "/stopProxy", method = RequestMethod.GET)
    public @ResponseBody
    String stopProxy() throws IOException {
        try {
            if (!VariableSession.flag_status_is_first_run_proxy) {
                proxyWithSSH.stop();
                VariableSession.flag_status_is_first_run_proxy = true;
            }
        } catch (Exception e) {
            e.getMessage();
            return "loi : " + e.getMessage();
        }
        return "stoped";
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() {
        return "index";
    }
}
