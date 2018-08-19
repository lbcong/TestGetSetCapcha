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
            // img truoc khi nhap capcha
            String img1 = ((RemoteWebElement) webDriver.findElement(By.xpath(Constant.xpathCapcha))).getId();
            System.out.println("hash img1 truoc submit:" + img1);
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
            element.click();
            String img2 = "";
            int counter = 0;
            while (counter < 2000) {
                Thread.sleep(300);
                try {
                    //ktr con dang tren trang vs load xong
                    img2 = ((RemoteWebElement) webDriver.findElement(By.xpath(Constant.xpathCapcha))).getId();
                    if (!(img1.equals(img2))) {
                        taskController.reportError("nhap that bai do nhap sai capcha");
                        System.out.println("nhap that bai do nhap sai capcha");
                        return Constant.Fail;
                    }
                } catch (Exception e) {
                    try {
                        // ko can ktr con dang tren trang ko
                        //dang load vs load xong roi
                        // element sau submit
                        webDriver.findElement(By.xpath("//input[@id='iSignupAction']"));
                        // khong tra ve exception >> dang o? tra verifi
                        // tra ve exception >> dang o? trang success
//                                            >> dang load
                        taskController.reportError("nhap that bai do verifi mobi");
                        System.out.println("nhap that bai do verifi mobi");
                        return Constant.Fail;
//                        }
                    } catch (Exception ex1) {
                        //load xong roi tim sai vs dang load
                        try {
                            // load xong nhung tim sai
                            webDriver.findElement(By.xpath("//i[@class='ms-Icon ms-Icon--ChevronRight']"));
                            taskController.reportError("tao thanh cong :");
                            System.out.println("nhap than cong");
                            return Constant.Sucess;
                        } catch (Exception ex2) {
                            taskController.reportError("dang load trang sau khi nhap capcha");
                            // load chua xong se xuong day
                            // tiep tuc lap
                        }
                    }
                }
                counter++;
            }
            return Constant.Fail;
        } catch (Exception ex) {
            // ip loi
            System.out.println(ex.getMessage());
            return Constant.Fail;
        }
    }

}
