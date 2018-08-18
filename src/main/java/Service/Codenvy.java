/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Service;

import ConstantVariable.Constant;
import Entity.ObjectJson;
import Utils.Chuyen_tu_Object_sang_byte_de_doc_hoac_ghi_file;
import Utils.Utils;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Codenvy {

    @Autowired
    SendRequest sendRequest;
    @Autowired
    Utils utils;
    @Autowired
    DowloadService dowloadService;
    @Autowired
    GetTextFromGit getTextFromGit;
    @Autowired
    CheckCapcha checkCapcha;
    @Autowired
    ProxyWithSSH proxyWithSSH;
    private List<String> listAccountCreated = new ArrayList<>();
    private int number_acc_must_create = 3;
    private int number_acc_created = 0;
    private int number_acc_fail = 0;

    public String Start(WebDriver webDriver) {
        List<String> lists = null;
        try {
            lists = getTextFromGit.getStringFromGithubRaw("https://raw.githubusercontent.com/lbcong/SaveFileTemp/master/AccountSignUpOutLook.txt");
        } catch (IOException ex) {
            System.out.println("loi get acc from git:" + ex.getMessage());
        }
        boolean flag_wait = false;
        Random rd = new Random();
        String str_username = lists.get(number_acc_created);
//        String str_password = "Ahfweh123@#$";
        String str_password = "Zxcv123123";
        String str_LastName = "cailong";
        String str_FirstName = "cailong1";
        String status_capcha_result = "";

        try {
            while (number_acc_created <= number_acc_must_create) {
                str_username = lists.get(number_acc_created) + rd.nextInt(9999);
                //check connect 
                checkConnect();
                webDriver.get("https://outlook.live.com/owa/?nlp=1&signup=1");
                // wait
                while (!flag_wait) {
                    flag_wait = utils.waitForPresence(webDriver, 5000, "//input[@id='MemberName']");
                    //check connect 
                    checkConnect();
                }
                flag_wait = false;
                WebElement input_signin = webDriver.findElement(By.xpath("//input[@id='MemberName']"));
                input_signin.sendKeys(str_username);
                webDriver.findElement(By.xpath("//input[@id='iSignupAction']")).click();
                // wait
                while (!flag_wait) {
                    flag_wait = utils.waitForPresence(webDriver, 5000, "//input[@id='PasswordInput']");
                    //check connect 
                    checkConnect();
                }
                flag_wait = false;
                WebElement PasswordInput = webDriver.findElement(By.xpath("//input[@id='PasswordInput']"));
                PasswordInput.sendKeys(str_password);
                webDriver.findElement(By.xpath("//input[@id='iSignupAction']")).click();
                // wait
                while (!flag_wait) {
                    flag_wait = utils.waitForPresence(webDriver, 5000, "//input[@id='LastName']");
                    //check connect 
                    checkConnect();
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
                    //check connect 
                    checkConnect();
                }
                flag_wait = false;
                WebElement Body = webDriver.findElement(By.tagName("body"));
                String languae = Body.getAttribute("lang");
                Select BirthDay = new Select(webDriver.findElement(By.xpath("//select[@id='BirthDay']")));
                Select BirthMonth = new Select(webDriver.findElement(By.id("BirthMonth")));
                Select BirthYear = new Select(webDriver.findElement(By.id("BirthYear")));
                if (languae.equals("vi-VN")) {
                    BirthDay.selectByVisibleText("3");
                    BirthMonth.selectByIndex(6);
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
                    //check connect 
                    checkConnect();
                }
                flag_wait = false;

                // tao img
                String rs = dowloadService.dowloadImgTypeBase64(webDriver);

                //check connect 
                checkConnect();
                // gui base64 img qua cho service send bang method post
                ObjectJson responseObject = sendRequest.sendPost(Constant.API_KEY, rs);

                // doi 5-10sgui request de get text
                String text_capcha = null;

                Thread.sleep(5000);
                //check connect 
                checkConnect();

                responseObject = sendRequest.sendGet(Constant.API_KEY, responseObject.getRequest());

                switch (responseObject.getRequest()) {
                    case "CAPCHA_NOT_READY":
                        Thread.sleep(5000);
                        responseObject = sendRequest.sendGet(Constant.API_KEY, responseObject.getRequest());
                        break;
                    case "ERROR_CAPTCHA_UNSOLVABLE":
                }

                // kiem tra request ma no gui ve la gi so sanh voi cac ma~ loi neu gap loi thi` bat gui lai
                text_capcha = responseObject.getRequest();

                // get text va` kiem tra status cua json neu thanh cong thi input text vao capcha
                status_capcha_result = checkCapcha.Check(webDriver, text_capcha);

//                 dung' + chua tao du so luong acc can thiet >> tao acc moi
                if ((Constant.Sucess.equals(status_capcha_result)) && (number_acc_created < number_acc_must_create)) {
                    listAccountCreated.add(str_username);
                    logout(webDriver);
                    webDriver.manage().deleteAllCookies();
                    number_acc_created++;
                    while (proxyWithSSH.getSession() == null) {
                        Thread.sleep(500);
                    }
                    proxyWithSSH.changeIp();
                } else // dung' + da tao du so luong acc can thiet >> out
                if ((Constant.Sucess.equals(status_capcha_result)) && (number_acc_created == number_acc_must_create)) {
                    listAccountCreated.add(str_username);
                    break;
                } else // sai >> doi ip tao lai acc khac
                if (Constant.Fail.equals(status_capcha_result)) {
                    while (proxyWithSSH.getSession() == null) {
                        Thread.sleep(500);
                    }
                    proxyWithSSH.changeIp();
                    number_acc_fail++;
                }
            }

            // tam thoi se xuat ra file txt sau nay se day? len database
            File dir = new File("D:\\accountOutlook.txt");
            byte[] bytes = Chuyen_tu_Object_sang_byte_de_doc_hoac_ghi_file.ObjectToByte(listAccountCreated);
            BufferedOutputStream stream;
            try {
                stream = new BufferedOutputStream(
                        new FileOutputStream(dir));
                stream.write(bytes);
                stream.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return "";
        } catch (Exception e) {
            System.out.println("exception:" + e.getMessage());
            webDriver.quit();
            return null;
        }
    }

    public List<String> getListAccountCreated() {
        return listAccountCreated;
    }

    public void logout(WebDriver webDriver) throws InterruptedException {

        webDriver.get("https://outlook.live.com/");
        WebElement element = null;
        Select select = null;
        boolean status = false;
        boolean flag_wait = false;
        List<WebElement> listElement = null;
        try {
            element = webDriver.findElement(By.xpath("//span[@class='signinTxt']"));
            status = true;
        } catch (Exception e) {
            status = false;
        }
        // neu tao account lan dau phai cau hinh` language
        if (status) {
            while (!flag_wait) {
                flag_wait = utils.waitForPresence(webDriver, 5000, "//span[@class='signinTxt']");

            }
            flag_wait = false;
            select = new Select(webDriver.findElement(By.xpath("//select[@id='selTz']")));
            select.selectByIndex(3);

            select = new Select(webDriver.findElement(By.xpath("//select[@name='lcid' and @class='languageInputText']")));
            listElement = select.getOptions();

            int index = 0;
            for (int i = 0; i < listElement.size(); i++) {
                if ("1066".equals(listElement.get(i).getAttribute("value"))) {
                    index = i;
                    break;
                }
            }
            select.selectByIndex(index);
            WebElement signinTxt = webDriver.findElement(By.xpath("//span[@class='signinTxt']"));
            signinTxt.click();
        }

        // wait doi xuat hien page mail box
        while (!flag_wait) {
            flag_wait = utils.waitForPresence(webDriver, 5000, "//div[contains(@class,'ms-Persona ms-Persona--') and contains(@size,'11')]");
        }
        flag_wait = false;

        int counter = 0;
        while (counter <= 5) {
            counter++;
            Thread.sleep(1000);
            try {
                element = webDriver.findElement(By.xpath("//div[contains(@class,'ms-Panel-navigation')]//button"));
                element.click();
                break;
            } catch (Exception e) {
            }
        }
        element = webDriver.findElement(By.xpath("//div[contains(@class,'ms-Persona ms-Persona--') and contains(@size,'11')]"));
        if (utils.isClickable(element, webDriver)) {
            // truong hop ko xuat hien modal
            element.click();
        }

        element = webDriver.findElement(By.xpath("//div[contains(text(),'Đăng xuất')]"));

        while (utils.isClickable(element, webDriver)) {
            element.click();
        }

    }

    public int getNumber_acc_must_create() {
        return number_acc_must_create;
    }

    public int getNumber_acc_created() {
        return number_acc_created;
    }

    public int getNumber_acc_fail() {
        return number_acc_fail;
    }

    public void checkConnect() throws InterruptedException {
        while (proxyWithSSH.getSession() == null) {
            Thread.sleep(500);
        }
        if (!proxyWithSSH.checkSshlive()) {
            proxyWithSSH.changeIp();
        }
    }
}
