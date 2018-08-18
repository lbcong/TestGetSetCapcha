/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ConstantVariable;

import java.io.File;
import org.springframework.beans.factory.annotation.Autowired;

public interface Constant {

    String Sucess = "Success";
    String Fail = "Fail";
    String Actived = "Actived";
    String Creating = "Creating";
    String Close = "Close";
    int Time_Limit_Waiting_Change_Ssh = 10;
    
    String API_KEY = "ceacd60a62c2d9bd987294cece4684ba";
    String Url_2Capcha_Get = "http://2captcha.com/res.php";
    String Url_2Capcha_Post = "http://2captcha.com/in.php";
    
    String Key_Tracer = "Tracer";
    String Key_Auto = "Auto";

    String typeKeyPub = ".pub";
    String typeKeyPPK = ".ppk";

    String dirFileAccount = "/app/Account.txt";
//    String dirFileAccount = "D:\\NetBeansProjects\\Service-Cloud\\Crawler\\CrawlerSelenium-ubuntu\\Account.txt";
//    String dirKey = "D:\\NetBeansProjects\\Service-Cloud\\Crawler\\CrawlerSelenium-ubuntu\\key\\";
    String SSHKey_hacklslol1 = "ssh-rsa AAAAB3NzaC1yc2EAAAABJQAAAQEA08ZiIsVseUVkG6bsV8SunT2SurNuY9M8OdpeM10Mz/fC0IgGn1oo60AjsruU7cTLezcR8Rvmcbfj0KQIxJzTzIFLRrRJVPzi6rgXY0ASn43ntRkK7Z/4jlEKVPIWnZbsJ9ztOJ7LQ6qZP66/BDaXJu2KQ/NboZk/yVs9kG9ls1SIQHlplxdmGPBPXD58u8P9+kg8wIW3Xk4vf/0L2RDHUcAPmYENhG9Rr4jMZmwVT2Hlb6W9SY8bv3DgTwa5VLv2o/r2wTDRr3R/lzpUzjJsDl5RJFUL466EcgS54QZ+f0HF70PrexO0wYA/3/wGYZXTbDqUHD8ENpa6/LbXQsqcXw== rsa-key-20170724";

    String dirFileResult = "/app/Result.txt";
    String dirKey = "/app/key/";
    String binaryFirefoxLinux = "/usr/bin/firefox";
    String binaryFirefoxWindows = "E:\\Soft\\FirefoxPortable\\App\\Firefox64\\firefox.exe";

    String dirDriverFirefoxGoogleCloud = "";
    String dirDriverGoogleChromeGoogleCloud = "";
    String dirDriverGoogleHeroku = "/app/chromedriver";
    String dirDriverFirefoxHeroku = "/app/firefox";

    String binaryGoogleHeroku = "/app/.apt/usr/bin/google-chrome";
    String binaryGoogleLinux = "/usr/bin/google-chrome";
    String binaryGoogleWindows = "E:\\Soft\\gg\\App\\Chrome-bin\\chrome.exe";

    String xpathCapcha = "//img[@aria-label='Visual Challenge']";

    String userCodenvy = "hacklslol3@yahoo.com";
    String passCodenvy = "lisatthu35";
}
