/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.Random;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 *
 * @author Alex
 */
public class Utils {

    public void waitForUrlLoading(WebDriver driver, String targetXpath) {
        WebElement element = null;
        while (true) {
            try {
                element = driver.findElement(By.xpath(targetXpath));
            } catch (Exception e) {
                break;
            }
        }
    }

    public boolean isBrowserClosed(WebDriver driver) {
        boolean isClosed = false;
        try {
            driver.getTitle();
        } catch (UnreachableBrowserException ubex) {
            isClosed = true;
        }
        return isClosed;
    }

    // doi element load
    public boolean waitForPresence(WebDriver driver, int timeLimitInSeconds, String targetXpath) throws InterruptedException {
        if (isBrowserClosed(driver)) {
            driver.quit();
        }
        try {
            WebElement element = null;
            element = driver.findElement(By.xpath(targetXpath));
            WebDriverWait wait = new WebDriverWait(driver, timeLimitInSeconds);
            wait.until(ExpectedConditions.visibilityOf(element));
            boolean isElementPresent = element.isDisplayed();
            return isElementPresent;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Thread.sleep(500);
            return false;
        }
    }

    public boolean isClickable(WebElement el, WebDriver driver) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, 6);
            wait.until(ExpectedConditions.elementToBeClickable(el));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void waitForPageLoaded(WebDriver driver) {
        ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {
                return ((JavascriptExecutor) driver).executeScript("return document.readyState").toString().equals("complete");
            }
        };
        try {
            Thread.sleep(1000);
            WebDriverWait wait = new WebDriverWait(driver, 30);
            wait.until(expectation);
        } catch (Throwable error) {
        }
    }

    public void sendKeys(Robot robot, String keys) {
        for (char c : keys.toCharArray()) {
            int keyCode = KeyEvent.getExtendedKeyCodeForChar(c);
            if (KeyEvent.CHAR_UNDEFINED == keyCode) {
                throw new RuntimeException(
                        "Key code not found for character '" + c + "'");
            }
            robot.keyPress(keyCode);
            robot.delay(400);
            robot.keyRelease(keyCode);
            robot.delay(444);
        }
    }

    public void clearCookieFirefox(WebDriver webDriver) throws InterruptedException {
        webDriver.navigate().to("about:preferences#privacy");
        webDriver.findElement(By.cssSelector("#clearSiteDataButton")).click();
        Thread.sleep(100);
        ((JavascriptExecutor) webDriver).executeScript("const browser = document.querySelector('#dialogOverlay-0 > groupbox:nth-child(1) > browser:nth-child(2)'); browser.contentDocument.documentElement.querySelector('#clearButton').click();");
        Thread.sleep(250);
        webDriver.switchTo().alert().accept();
    }

    public String random() {
        String[] array = {"Dennis", "Grace", "Bjarne", "James",
            "Vaillancourt", "Anderson", "Danielson", "Lawrence",
            "William", "Carter", "Aguilera", "Kimberly", "Simmons",
            "Michael", "Eddings", "Naomi", "Worcester", "Delisle", "Augusta"};
        return array[getRandomNumberInRange(0, 18)];
    }

    private int getRandomNumberInRange(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    public String generateRandomString(int length) {
        Random random = new Random();
        char[] values = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
            'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
            'u', 'v', 'w', 'x', 'y', 'z'};
        String out = "";

        for (int i = 0; i < length; i++) {
            int idx = random.nextInt(values.length);
            out += values[idx];
        }
        return out;
    }

    public String createEmailRandom() {
        return random() + generateRandomString(4);
    }

    public boolean waitForPresenceCss(WebDriver driver, int timeLimitInSeconds, String targetXpath) throws InterruptedException {
        if (isBrowserClosed(driver)) {
            driver.quit();
        }
        try {
            WebElement element = null;
            element = driver.findElement(By.cssSelector(targetXpath));
            WebDriverWait wait = new WebDriverWait(driver, timeLimitInSeconds);
            wait.until(ExpectedConditions.visibilityOf(element));
            boolean isElementPresent = element.isDisplayed();
            return isElementPresent;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Thread.sleep(500);
            return false;
        }
    }
    
    public void clearCookie(WebDriver webDriver) throws InterruptedException{
        if(webDriver instanceof ChromeDriver){
            clearCookieGoogle(webDriver);
        }else if (webDriver instanceof FirefoxDriver){
            clearCookieFirefox(webDriver);
        }
    }

    public void clearCookieGoogle(WebDriver webDriver) throws InterruptedException {
        webDriver.navigate().to("chrome://settings/clearBrowserData");
        while (true) {
            if (waitForPresenceCss(webDriver, 0, "* /deep/ #clearBrowsingDataConfirm")) {
                break;
            }
        }
        webDriver.findElement(By.cssSelector("* /deep/ #clearBrowsingDataConfirm")).click();
    }
     public boolean isURLable(WebDriver driver, String url) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, 3);
            wait.until(ExpectedConditions.urlContains(url));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isTitleable(WebDriver driver, String tittle) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, 3);
            wait.until(ExpectedConditions.titleIs(tittle));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
