package com.tricentis.demowebshop;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;
import org.junit.Assert;

public class Tests extends WebDriverSettings {
    @Test
    public void FirstTest() {
        driver.get("http://demowebshop.tricentis.com/");

        // find "Computers" category and "Desktops" subcategory
        driver.findElement(By.xpath("//div[@class='block block-category-navigation']//a[contains(.,'Computers')]")).click();
        driver.findElement(By.xpath("//div[@class='block block-category-navigation']//a[contains(.,'Desktops')]")).click();
        // set Display to "4" per page
        Select dropdown = new Select(driver.findElement(By.id("products-pagesize")));
        dropdown.selectByVisibleText("4");
        // count products
        long productsCounter = driver.findElements(By.className("product-item")).stream().count();

        Assert.assertEquals(productsCounter, 4);
    }

    @Test
    public void SecondTest() throws InterruptedException {
        driver.get("http://demowebshop.tricentis.com/build-your-own-expensive-computer-2");

        // Select processor and RAM
        driver.findElement(By.xpath("//label[contains(.,'Fast')]")).click();
        driver.findElement(By.xpath("//label[contains(.,'8GB')]")).click();

        // select software
        driver.findElement(By.xpath("//label[contains(.,'Image Viewer')]")).click();
        driver.findElement(By.xpath("//label[contains(.,'Office Suite')]")).click();
        driver.findElement(By.xpath("//label[contains(.,'Other Office Suite')]")).click();

        // check cart quantity before adding an item
        String str1 = driver.findElement(By.xpath("//span[@class='cart-qty']")).getText();
        int qty1 = Integer.parseInt(str1.substring(1, str1.length()-1));

        // add to card
        driver.findElement(By.xpath("//div[@class='add-to-cart-panel']//input[@value='Add to cart']")).click();
        // wait until alert appears
        Thread.sleep(1000);
        // close alert
        driver.findElement(By.className("close")).click();

        // check cart quantity after adding an item
        String str2 = driver.findElement(By.xpath("//span[@class='cart-qty']")).getText();
        int qty2 = Integer.parseInt(str2.substring(1, str2.length()-1));

        // go to shopping cart
        driver.findElement(By.xpath("//li[@id='topcartlink']//a[@class='ico-cart']")).click();
        // count products (should be 1)
        long productsCounter = driver.findElements(By.className("cart-item-row")).stream().count();

        // find price
        String price = driver.findElement(By.className("product-unit-price")).getText();
        // find subtotal price
        String subtotal = driver.findElement(By.className("product-subtotal")).getText();

        // check different parameters to know at what point the error
        Assert.assertEquals(qty2 - qty1, 1);
        Assert.assertEquals(productsCounter, 1);
        Assert.assertEquals(price, "2105.00");
        Assert.assertEquals(subtotal, "2105.00");

        // remove item from cart
        driver.findElement(By.xpath("//input[@name='removefromcart']")).click();
        driver.findElement(By.xpath("//input[@type='submit'][@name='updatecart']")).click();

        // check that item was deleted
        long productsCounterAfter = driver.findElements(By.className("cart-item-row")).stream().count();
        Assert.assertEquals(productsCounterAfter, 0);
    }
}
