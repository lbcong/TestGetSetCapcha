/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Service;

import ConstantVariable.Constant;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Base64;
import javax.imageio.ImageIO;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Service;

@Service
public class DowloadService implements Serializable {
    public String dowloadImgTypeBase64( WebDriver webDriver) throws IOException {
        OutputStream outStream = null;
        try {
            byte[] imageInByte;
            BufferedImage originalImage = getBufferedImage(webDriver);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(originalImage, "png", baos);
            baos.flush();
            imageInByte = baos.toByteArray();
            // chuyen tu byte[] sang string 64
            String base64Encoded = Base64.getEncoder().encodeToString(imageInByte);
            // chuyen tu string 64 sang byte[]
//            byte[] decoded = Base64.getDecoder().decode(base64Encoded);
            
            baos.close();
            return base64Encoded;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    // tra ve byte array cua img
    public byte[] dowloadImgTypeByte( WebDriver webDriver) throws IOException {
        OutputStream outStream = null;
        try {
            byte[] imageInByte;
            BufferedImage originalImage = getBufferedImage(webDriver);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(originalImage, "png", baos);
            baos.flush();
            imageInByte = baos.toByteArray();
            baos.close();
            return imageInByte;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public BufferedImage getBufferedImage(WebDriver webDriver) throws IOException {
        WebElement ele = webDriver.findElement(By.xpath(Constant.xpathCapcha));
        File screenshot = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
        BufferedImage fullImg = ImageIO.read(screenshot);

// Get the location of element on the page
        Point point = ele.getLocation();

// Get width and height of the element
        int eleWidth = ele.getSize().getWidth();
        int eleHeight = ele.getSize().getHeight();

// Crop the entire page screenshot to get only element screenshot
        return fullImg.getSubimage(point.getX(), point.getY(),
                eleWidth, eleHeight);

    }

//    public void getImgFile() throws IOException {
//        webDriver.get("http://www.google.com");
//        WebElement ele = webDriver.findElement(By.id("hplogo"));
//
//// Get entire page screenshot
//        File screenshot = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
//        BufferedImage fullImg = ImageIO.read(screenshot);
//
//// Get the location of element on the page
//        Point point = ele.getLocation();
//
//// Get width and height of the element
//        int eleWidth = ele.getSize().getWidth();
//        int eleHeight = ele.getSize().getHeight();
//
//// Crop the entire page screenshot to get only element screenshot
//        BufferedImage eleScreenshot = fullImg.getSubimage(point.getX(), point.getY(),
//                eleWidth, eleHeight);
//        ImageIO.write(eleScreenshot, "png", screenshot);
//
//// Copy the element screenshot to disk
//        File screenshotLocation = new File("D:\\GoogleLogo_screenshot.png");
//        FileUtils.copyFile(screenshot, screenshotLocation);
//    }
}
