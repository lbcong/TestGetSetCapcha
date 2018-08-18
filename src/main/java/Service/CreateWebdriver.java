package Service;

import Bean.SystemConfig;
import java.io.File;
import java.net.InetAddress;
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
//                    String str_proxy_linux = "--proxy-server=socks4://"+InetAddress.getLocalHost().getHostAddress()+":1080";
                    String str_proxy_linux = "--proxy-server=socks4://127.0.0.1:1080";
                    options.addArguments(str_proxy_linux);

                    webDriver = new ChromeDriver(options);
                    break;
                case "Windows":

                    ChromeOptions optionswindow = new ChromeOptions();
//                    String str_proxy_windows = "--proxy-server=socks4://"+InetAddress.getLocalHost().getHostAddress()+":1080";
                    String str_proxy_windows = "--proxy-server=socks4://127.0.0.1:1080";
                    optionswindow.addArguments(str_proxy_windows);
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
