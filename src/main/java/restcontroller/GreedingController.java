package restcontroller;

import Utils.Chuyen_tu_Object_sang_byte_de_doc_hoac_ghi_file;
import ConstantVariable.Constant;
import Service.CreateWebdriver;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import java.io.IOException;

import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.web.bind.annotation.RestController;
import static Utils.Doc_file_kieu_binary.readFileBinary;
import Service.DowloadService;
import Service.GetTextFromGit;
import Utils.Utils;
import java.awt.Robot;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.bind.annotation.ResponseBody;

@RestController
public class GreedingController {

    public static WebDriver webDriver = null;
    @Autowired
    DowloadService dowloadService;
    @Autowired
    CreateWebdriver createWebdriver;
    @Autowired
    GetTextFromGit getTextFromGit;
    @Autowired
    Utils utils;

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String greeding() {
        return "Hello ";
    }

    @RequestMapping(value = "/save", method = RequestMethod.GET)
    public String save() throws IOException {
        Set<Cookie> cookies = webDriver.manage().getCookies();
        Iterator<Cookie> itr = cookies.iterator();
        while (itr.hasNext()) {
            Cookie cookie = itr.next();
            System.out.println(cookie.getName() + "\n" + cookie.getPath()
                    + "\n" + cookie.getDomain() + "\n" + cookie.getValue()
                    + "\n" + cookie.getExpiry());
        }
        File dir = new File("D:\\cookie.txt");
        byte[] bytes = Chuyen_tu_Object_sang_byte_de_doc_hoac_ghi_file.ObjectToByte(cookies);
        BufferedOutputStream stream;
        try {
            stream = new BufferedOutputStream(
                    new FileOutputStream(dir));
            stream.write(bytes);
            stream.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return "Hello ";
    }

    @RequestMapping(value = "/opencookie", method = RequestMethod.GET)
    public String open() {

        try {
            Set<Cookie> cookies = readFileBinary("D:\\cookie.txt");
            Iterator<Cookie> itr = cookies.iterator();
            while (itr.hasNext()) {
                Cookie cookie = itr.next();
                System.out.println(cookie.getName() + "\n" + cookie.getPath()
                        + "\n" + cookie.getDomain() + "\n" + cookie.getValue()
                        + "\n" + cookie.getExpiry());
                webDriver.manage().addCookie(cookie);
            }

        } catch (Exception e) {
            e.getStackTrace();
        }

        return "Hello";
    }

    @RequestMapping(value = "/clear", method = RequestMethod.GET)
    public String clear() {

        try {
            webDriver.manage().deleteAllCookies();
        } catch (Exception e) {
            e.getStackTrace();
        }

        return "Hello";
    }

    @RequestMapping(value = "/openbrowser", method = RequestMethod.GET)
    public @ResponseBody
    String selenium(HttpServletResponse response) throws IOException {
        String output = "";
        try {
            webDriver = createWebdriver.getGoogle(Constant.binaryGoogleHeroku);
//            webDriver.get("http://checkip.dyndns.org/");
            openTestSite();
            login("admin", "12345");
            return getText();
        } catch (Exception e) {
            e.getMessage();
            return "loi : " + e.getMessage();
        }

    }

    @RequestMapping(value = "/checkIp", method = RequestMethod.GET)
    public @ResponseBody
    String checkIp(HttpServletResponse response) throws IOException {
        return getTextFromGit.getStringFromGithubRaw("http://checkip.dyndns.org/").get(0);
    }

    @RequestMapping(value = "/getInfoServer", method = RequestMethod.GET)
    public @ResponseBody
    Map<String, String> getInfoServer(HttpServletRequest request) throws IOException {
        Map<String, String> result = new HashMap<>();

        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            result.put(key, value);
        }
        return result;
    }

    @RequestMapping(value = "/cmd", method = RequestMethod.GET)
    public String greeding(@RequestParam(value = "cmd", required = true) String cmd) {
        String output = "";
        try {
            output = executeCommand(cmd);
            return output;
        } catch (Exception e) {
            e.getMessage();
            return e.getMessage();
        }

    }

    public String executeCommand(String command) {

        StringBuffer output = new StringBuffer();

        Process p;
        try {
            p = Runtime.getRuntime().exec(command);
            Thread.sleep(2000);
            p.waitFor();
            Thread.sleep(2000);
            BufferedReader reader
                    = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line = "";
            while ((line = reader.readLine()) != null) {
                output.append(line + "\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return output.toString();

    }

    public void login(String username, String Password) throws Exception, InterruptedException {

        Thread.sleep(1000);
        WebElement userName_editbox = webDriver.findElement(By.xpath("//input[@id='usr']"));
        Thread.sleep(1000);
        WebElement password_editbox = webDriver.findElement(By.xpath("//input[@id='pwd']"));
        Thread.sleep(1000);
        WebElement submit_button = webDriver.findElement(By.xpath("//input[@value='Login']"));
        Thread.sleep(1000);
        userName_editbox.click();
        Thread.sleep(1000);
        Robot robot = new Robot();
        utils.sendKeys(robot, username);
//        userName_editbox.sendKeys(username);
        Thread.sleep(1000);
        password_editbox.sendKeys(Password);
        Thread.sleep(1000);
        submit_button.click();
        System.out.println("xong ham login");
    }

    public String getText() throws IOException, InterruptedException ,Exception {

        Thread.sleep(1000);
        String text = webDriver.findElement(By.xpath("//div[@id='case_login']/h3")).getText();
        return text;

    }

    public void closeBrowser() {
        webDriver.close();
    }

    public void openTestSite() {
        webDriver.navigate().to("http://testing-ground.scraping.pro/login");
    }

}
