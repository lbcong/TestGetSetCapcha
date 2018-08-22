/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Service;

import ConstantVariable.Constant;
import Entity.ObjectJson;
import Repository.MailOutLookRepository;
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
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import restcontroller.TaskController;
import Entity.MailOutLookEntity;
import java.util.Date;

@Service
public class Codenvy {

    @Autowired
    TaskController taskController;
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
    @Autowired
    MailOutLookRepository mailOutLookRepository;
    private List<String> listAccountCreated = new ArrayList<>();
    private int number_acc_must_create = 3;
    private int number_acc_created = 0;
    private int number_acc_fail = 0;

    public String Start(WebDriver webDriver) {
        List<String> listsGithub = null;
        try {
            listsGithub = getTextFromGit.getStringFromGithubRaw("https://raw.githubusercontent.com/lbcong/SaveFileTemp/master/AccountSignUpOutLook.txt");
        } catch (IOException ex) {
            System.out.println("loi get acc from git:" + ex.getMessage());
        }
        boolean flag_wait = false;
        Random rd = new Random();
        String str_username = "";
//        String str_password = "Ahfweh123@#$";
        String str_password = "Zxcv123123";
        String str_LastName = "cailoadqng";
        String str_FirstName = "cailqwdqwong1";
        String status_capcha_result = "";
        WebElement element = null;
        Select select = null;
        int counter = 0;
        String button_after_subit = null;
        String button_next = "";
        MailOutLookEntity mailEntity = null;
        try {
            while (number_acc_created <= number_acc_must_create) {
                str_username = listsGithub.get(0) + rd.nextInt(9999);
                insertInfoAccount(webDriver, str_username, str_password, str_LastName, str_FirstName);
                button_after_subit = ((RemoteWebElement) webDriver.findElement(By.xpath("//input[@id='iSignupAction']"))).getId();

                // ktra truong hop bi verifi truoc khi nhap capcha
                counter = 0;
                // dat thoi gian toi da 50s
                while (counter < 100) {
                    Thread.sleep(500);
                    try {
                        button_next = ((RemoteWebElement) webDriver.findElement(By.xpath("//input[@id='iSignupAction']"))).getId();
                        // if true >> load xong roi
                        if (!(button_after_subit.equals(button_next))) {
                            // co 2 th xay ra verifi hoac capcha
                            webDriver.findElement(By.xpath(Constant.xpathCapcha));
                            break;
                        }
                        // if false chua load xong
                    } catch (Exception e) {
                        // dang load hoac verifi
                        try {
                            webDriver.findElement(By.xpath("//input[@id='iSignupAction']"));
                            while (proxyWithSSH.status_proxy.equals(Constant.Creating)) {
                                Thread.sleep(500);
                            }
                            proxyWithSSH.changeIp();
                            continue;
                        } catch (Exception ex) {
                            // dang load tiep tuc lap
                        }
                    }
                    counter++;
                }

                // wait
                while (!flag_wait) {
                    flag_wait = utils.waitForPresence(webDriver, 5000, "//img[@aria-label='Visual Challenge']");
                    //check connect 
                    checkConnect();
                }
                flag_wait = false;

                // get string base64 tu img
                String rs = dowloadService.dowloadImgTypeBase64(webDriver);
//-------------------------------------------------------------------------------
                checkConnect();
                // gui string base64 img qua cho service send bang method post va nhan response dang object
                Thread.sleep(3000);
                ObjectJson responseObjectPost = sendRequest.sendPost(Constant.API_KEY, rs);
                taskController.reportError("ket qua tra ve method post: " + responseObjectPost.getRequest());
                switch (responseObjectPost.getRequest()) {
                    case "ERROR_NO_SLOT_AVAILABLE":
                        System.out.println("Service.Codenvy.Start()");
                        break;
                    case "ERROR_UPLOAD":
                        System.out.println("Service.Codenvy.Start()");
                        break;
                    case "ERROR_IP_NOT_ALLOWED":
                        System.exit(0);
                        break;
                    case "IP_BANNED":
                        System.exit(0);
                        break;
                    case "MAX_USER_TURN":
                        webDriver.manage().deleteAllCookies();
                        continue;
                    case "NNNN":
                        webDriver.manage().deleteAllCookies();
                        continue;

                }

                // doi 5-10s gui request de get text
                Thread.sleep(5000);
                //check connect 
                checkConnect();
                // gui request len service de get text ket qua dang object
                ObjectJson responseObjectGet = sendRequest.sendGet(Constant.API_KEY, responseObjectPost.getRequest());
                taskController.reportError("ket qua tra ve method get: " + responseObjectGet.getRequest());
                // kiem tra request ma no gui ve la gi so sanh voi cac ma~ loi neu gap loi thi` bat gui lai
                switch (responseObjectGet.getRequest()) {
                    case "CAPCHA_NOT_READY":
                        Thread.sleep(7000);
                        responseObjectGet = sendRequest.sendGet(Constant.API_KEY, responseObjectPost.getRequest());
                        taskController.reportError("ket qua tra ve method get: " + responseObjectGet.getRequest());
                        break;
                    case "ERROR_CAPTCHA_UNSOLVABLE":
                        webDriver.manage().deleteAllCookies();
                        continue;
                    case "ERROR_KEY_DOES_NOT_EXIST":
                        webDriver.manage().deleteAllCookies();
                        continue;
                    case "ERROR_WRONG_CAPTCHA_ID":
                        webDriver.manage().deleteAllCookies();
                        continue;
                    case "ERROR_BAD_DUPLICATES":
                        webDriver.manage().deleteAllCookies();
                        continue;
                    case "NNNN	":
                        webDriver.manage().deleteAllCookies();
                        continue;
                }

                // get text va` kiem tra status cua json neu thanh cong thi input text vao capcha
                status_capcha_result = checkCapcha.Check(webDriver, responseObjectGet.getRequest());
//--------------------------------------------------------------------------------------------------

//                 dung' + chua tao du so luong acc can thiet >> tao acc moi
                if ((Constant.Sucess.equals(status_capcha_result)) && (number_acc_created < number_acc_must_create)) {
                    // add account vao list muc dich sau nay ghi ra file txt
                    // neu ko muon ghi file txt co the ko can lam cai nay
                    listAccountCreated.add(str_username);
                    mailEntity = new MailOutLookEntity();
                    mailEntity.setCreated_at(new Date());
                    mailEntity.setIdserver("1");
                    mailEntity.setStatus("new");
                    mailEntity.setName(str_username);
                    mailEntity.setMain(true);
                    // luu account vao databse
                    mailOutLookRepository.save(mailEntity);
                    taskController.reportError("account duoc tao thanh cong: " + str_username);
                    logout(webDriver);
                    number_acc_created++;
                    while (proxyWithSSH.status_proxy.equals(Constant.Creating)) {
                        Thread.sleep(500);
                    }
                    proxyWithSSH.changeIp();
                    webDriver.manage().deleteAllCookies();
                } else // dung' + da tao du so luong acc can thiet >> out
                if ((Constant.Sucess.equals(status_capcha_result)) && (number_acc_created == number_acc_must_create)) {
                    listAccountCreated.add(str_username);
                    mailEntity = new MailOutLookEntity();
                    mailEntity.setCreated_at(new Date());
                    mailEntity.setIdserver("1");
                    mailEntity.setStatus("new");
                    mailEntity.setName(str_username);
                    mailEntity.setMain(true);
                    mailOutLookRepository.save(mailEntity);
                    taskController.reportError("account duoc tao thanh cong: " + str_username);
                    taskController.reportError("da tao du so luong acc setup");
                    logout(webDriver);
                    number_acc_created++;
                    break;
                } else // sai >> doi ip tao lai acc khac
                if (Constant.Fail.equals(status_capcha_result)) {
                    while (proxyWithSSH.status_proxy.equals(Constant.Creating)) {
                        Thread.sleep(500);
                    }
                    proxyWithSSH.changeIp();
                    webDriver.manage().deleteAllCookies();
                    number_acc_fail++;
                }
            }

            // xuat ra txt 1 ban? dua len database 1 ban
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
            return "complete";
        } catch (Exception e) {
            System.out.println("exception:" + e.getMessage());
            webDriver.quit();
            return null;
        }
    }

    public void insertInfoAccount(WebDriver webDriver, String str_username, String str_password, String str_LastName, String str_FirstName) throws InterruptedException {

        boolean flag_wait = false;
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
    }

    public void logout(WebDriver webDriver) throws InterruptedException {
        checkConnect();
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
            checkConnect();
        }
        flag_wait = false;
        element = webDriver.findElement(By.xpath("//div[contains(@class,'ms-Persona ms-Persona--') and contains(@size,'11')]"));
        if (utils.isClickable(element, webDriver)) {
            // truong hop ko xuat hien modal
            element.click();
        }

        while (!flag_wait) {

            flag_wait = utils.waitForPresence(webDriver, 5000, "//div[contains(text(),'Đăng xuất')]");
        }
        flag_wait = false;
        element = webDriver.findElement(By.xpath("//div[contains(text(),'Đăng xuất')]/ancestor::div[2]"));
        while (utils.isClickable(element, webDriver)) {
            checkConnect();
            element.click();
        }
        webDriver.manage().deleteAllCookies();

    }

    public void checkConnect() throws InterruptedException {
        while (proxyWithSSH.status_proxy.equals(Constant.Creating)) {
            Thread.sleep(500);
        }
        if (!proxyWithSSH.checkSshlive()) {
            proxyWithSSH.changeIp();
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

    public List<String> getListAccountCreated() {
        return listAccountCreated;
    }

}
