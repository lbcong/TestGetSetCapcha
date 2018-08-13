package Service;

import Bean.SystemConfig;
import ConstantVariable.Constant;
import java.io.File;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateWebdriver {

    @Autowired
    PathDriver setPathDriver;
    @Autowired
    SystemConfig SystemCofig;

    public WebDriver getFirefox(String binaryFirefox) {
        setPathDriver.setPathFireFox();
        //
        WebDriver webDriver = null;
        try {
            System.setProperty(PathDriver.webDriverFirefox, PathDriver.dirDriverFirefox);
            File pathToBinary = new File(binaryFirefox);
            FirefoxBinary ffBinary = new FirefoxBinary(pathToBinary);
            FirefoxProfile firefoxProfile = new FirefoxProfile();
            webDriver = new FirefoxDriver(ffBinary, firefoxProfile);
            return webDriver;

        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            return webDriver;
        }
    }

    // neu file binary de o? thuc muc khac' khong phai /user/bin , vi du heroku
    public WebDriver getGoogle(String binaryGoogle) {
        System.out.println(System.getProperty("java.version"));
        System.out.println(System.getProperty("java.specification.version"));
        //
        setPathDriver.setPathGoogle();
        //
        WebDriver webDriver = null;
        try {
            //
            System.setProperty(PathDriver.webDriverGoogle, PathDriver.dirDriverGoogle);

            switch (SystemCofig.os) {
                case "Linux":
                    ChromeOptions options = new ChromeOptions();
                    options.setBinary(binaryGoogle);
                    options.addArguments("--proxy-server=socks4://localhost:1080");
                    webDriver = new ChromeDriver(options);
                    break;
                case "Windows":
                   
                    ChromeOptions optionswindow = new ChromeOptions();
                    optionswindow.addArguments("--proxy-server=socks4://localhost:1080");
                    try {
                        webDriver = new ChromeDriver(optionswindow);
                    } catch (Exception e) {
                        e.getMessage();
                    }
                    break;
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return webDriver;

    }

}
