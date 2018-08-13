/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Service;

import Utils.Utils;
import java.io.IOException;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Codenvy {

    @Autowired
    Utils utils;
    @Autowired
    DowloadService dowloadService;
    @Autowired
    GetTextFromGit getTextFromGit;

    public byte[] Start(WebDriver webDriver) {
        List<String> lists = null;
        try {
            lists = getTextFromGit.getStringFromGithubRaw("https://raw.githubusercontent.com/lbcong/SaveFileTemp/master/AccountSignUpOutLook.txt");
        } catch (IOException ex) {
            System.out.println("gettext:"+ex.getMessage());
        }
        boolean flag_wait = false;
        String str_username = lists.get(0);
//        String str_password = "Ahfweh123@#$";
        String str_password = "Zxcv123123";
        String str_LastName = "cailong";
        String str_FirstName = "cailong1";
        try {
            webDriver.get("https://outlook.live.com/owa/?nlp=1&signup=1");
            // wait
            while (!flag_wait) {
                flag_wait = utils.waitForPresence(webDriver, 5000, "//input[@id='MemberName']");
            }
            flag_wait = false;
            WebElement input_signin = webDriver.findElement(By.xpath("//input[@id='MemberName']"));
            input_signin.sendKeys(str_username);
            webDriver.findElement(By.xpath("//input[@id='iSignupAction']")).click();
            // wait
            while (!flag_wait) {
                flag_wait = utils.waitForPresence(webDriver, 5000, "//input[@id='PasswordInput']");
            }
            flag_wait = false;
            WebElement PasswordInput = webDriver.findElement(By.xpath("//input[@id='PasswordInput']"));
            PasswordInput.sendKeys(str_password);
            webDriver.findElement(By.xpath("//input[@id='iSignupAction']")).click();
            // wait
            while (!flag_wait) {
                flag_wait = utils.waitForPresence(webDriver, 5000, "//input[@id='LastName']");
            }
            flag_wait = false;
            WebElement LastName = webDriver.findElement(By.xpath("//input[@id='LastName']"));
            LastName.sendKeys(str_LastName);
            WebElement FirstName = webDriver.findElement(By.xpath("//input[@id='FirstName']"));
            FirstName.sendKeys(str_FirstName);
            webDriver.findElement(By.xpath("//input[@id='iSignupAction']")).click();

            // wait
            while (!flag_wait) {
                flag_wait = utils.waitForPresence(webDriver, 5000, "//select[@id='BirthDay']");
            }
            flag_wait = false;
            WebElement Body = webDriver.findElement(By.tagName("body"));
            String languae = Body.getAttribute("lang");
            Select BirthDay = new Select(webDriver.findElement(By.xpath("//select[@id='BirthDay']")));
            Select BirthMonth = new Select(webDriver.findElement(By.id("BirthMonth")));
            Select BirthYear = new Select(webDriver.findElement(By.id("BirthYear")));
            if (languae.equals("vi-VN")) {
                BirthDay.selectByVisibleText("3");
                BirthMonth.selectByVisibleText("Th�ng Ba");
                BirthYear.selectByVisibleText("1991");
            } else {
                BirthDay.selectByVisibleText("3");
                BirthMonth.selectByVisibleText("May");
                BirthYear.selectByVisibleText("1991");
            }
            webDriver.findElement(By.xpath("//input[@id='iSignupAction']")).click();

            // wait
            while (!flag_wait) {
                flag_wait = utils.waitForPresence(webDriver, 5000, "//img[@aria-label='Visual Challenge']");
            }
            flag_wait = false;

            byte[] rs = dowloadService.dowloadImgTypeByte(webDriver);
            return rs;
        } catch (Exception e) {
            System.out.println("exception:" + e.getMessage());
            webDriver.quit();
            return null;
        }
    }

}
