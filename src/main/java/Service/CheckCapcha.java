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
import org.openqa.selenium.remote.RemoteWebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import restcontroller.TaskController;

@Service
public class CheckCapcha {

    @Autowired
    TaskController taskController;
    @Autowired
    Utils utils;
    @Autowired
    ProxyWithSSH proxyWithSSH;

    public String Check(WebDriver webDriver, String capchaText) {
        try {
            WebElement element = null;
            element = webDriver.findElement(By.xpath("//input[@type='text']"));
            element.sendKeys(capchaText);
            element = webDriver.findElement(By.xpath("//input[@type='submit' and @id='iSignupAction']"));
            Thread.sleep(2000);
            //check connect 
            while (proxyWithSSH.getSession() == null) {
                Thread.sleep(500);
            }
            if (!proxyWithSSH.checkSshlive()) {
                proxyWithSSH.changeIp();
            }
            String hash_element_truoc_submit = ((RemoteWebElement) element).getId();
            taskController.reportError("hash trc submit:" + hash_element_truoc_submit);
            element.click();
//            int counter = 0;

//            while (counter <= 5) {
//                counter++;
//                Thread.sleep(1000);
//                try {
//                    element = webDriver.findElement(By.xpath("//div[contains(@aria-label,'try again')]"));
//                    System.out.println("nhap that bai do capcha sai");
//                    return Constant.Fail;
//                } catch (Exception e) {
//                }
//            }
            String hash_element_sau_submit = hash_element_truoc_submit;
            while (true) {
                Thread.sleep(500);
                try {
                    // element sau submit
                    hash_element_sau_submit = ((RemoteWebElement) webDriver.findElement(By.xpath("//input[@id='iSignupAction']"))).getId();
                    taskController.reportError("hash sau submit:" + hash_element_sau_submit);
                    //neu if true  >> da load xong va chay vao trang verifi mobile
                    if (!(hash_element_truoc_submit.equals(hash_element_sau_submit))) {
                        taskController.reportError("nhap that bai do verifi mobi");
                        System.out.println("nhap that bai do verifi mobi");
                        return Constant.Fail;
                    }

                } catch (Exception e) {
                    try {
                        // load xong nhung tim sai
                        element = webDriver.findElement(By.xpath("//i[@class='ms-Icon ms-Icon--ChevronRight']"));
                        taskController.reportError("tao thanh cong :");
                        System.out.println("nhap than cong");
                        return Constant.Sucess;
                    } catch (Exception ex) {
                        taskController.reportError("dang load trang sau khi nhap capcha");
                        // load chua xong se xuong day
                        // tiep tuc lap
                    }
                }
            }

        } catch (Exception ex) {
            // ip loi
            System.out.println(ex.getMessage());
            return Constant.Fail;
        }
    }

}
