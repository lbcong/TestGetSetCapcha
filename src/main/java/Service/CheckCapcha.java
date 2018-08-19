/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Service;

import ConstantVariable.Constant;

import Utils.Utils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CheckCapcha {

    @Autowired
    Utils utils;
    @Autowired
    ProxyWithSSH proxyWithSSH;

    public String Check(WebDriver webDriver, String capchaText) {
        try {
            WebElement input_capcha = webDriver.findElement(By.xpath("//input[@type='text']"));
            WebElement button_submit = webDriver.findElement(By.xpath("//input[@type='submit' and @id='iSignupAction']"));
            input_capcha.sendKeys(capchaText);
            Thread.sleep(2000);
            //check connect 
            while (proxyWithSSH.getSession() == null) {
                Thread.sleep(500);
            }
            if (!proxyWithSSH.checkSshlive()) {
                proxyWithSSH.changeIp();
            }
            button_submit.click();
            int counter = 0;
            while (counter <= 5) {
                counter++;
                Thread.sleep(1000);
                try {
                    input_capcha = webDriver.findElement(By.xpath("//div[contains(@aria-label,'try again')]"));
                    System.out.println("nhap that bai do capcha sai");
                    return Constant.Fail;
                } catch (Exception e) {
                }
            }

            // doi load trang
            while (true) {
                try {
                    input_capcha = webDriver.findElement(By.xpath(Constant.xpathCapcha));
                } catch (Exception e) {
                    break;
                }
            }
            // ktra nhap capcha dung hay khong
            if ("Creating your mailbox".equals(webDriver.getTitle())) {
                System.out.println("nhap than cong");
                return Constant.Sucess;
            } else {
                System.out.println("nhap that bai do verifi mobi");
                return Constant.Fail;
            }

        } catch (Exception ex) {
            // ip loi
            System.out.println(ex.getMessage());
            return Constant.Fail;
        }
    }

}
