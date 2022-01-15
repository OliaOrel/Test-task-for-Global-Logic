package com.tricentis.demowebshop;

import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class WebDriverSettings {
    public static ChromeDriver driver;

    @Before
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "/Users/Dell/Desktop/chromedriver.exe");
        driver = new ChromeDriver();
    }

    @After
    public void close() {
        driver.quit();
    }

//    public static WebElement findElement(By by)
//    {
//        return driver.findElement(by);
//    }

//    public static By ComputerCategory() {
//        return By.xpath("//div[@class='block-category-navigation']//a[@Text='Computers']");
//    }

//    public static void clickComputerCategory() {
//        findElement(ComputerCategory());
//    }
}
