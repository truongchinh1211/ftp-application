/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 *
 * @author lamanhhai
 */
public class Test {
    public static void main(String[] args) throws InterruptedException {
        WebDriver driver = new FirefoxDriver();
        driver.get("https:www.gmail.com");
        driver.findElement(By.id("identifierId")).sendKeys("ftp.project.demo@gmail.com");
        driver.findElement(By.xpath("//span[@jsname='V67aGc' and text()='Tiếp theo']")).click();
        driver.quit();     
    }
}

