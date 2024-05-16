package com.javaprac.webforum;

import org.testng.annotations.*;
import static org.testng.Assert.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import java.time.Duration;

public class Authorize {
  private WebDriver driver;
  private StringBuffer verificationErrors = new StringBuffer();

  @BeforeClass(alwaysRun = true)
  public void setUp() throws Exception {
    System.setProperty("webdriver.chrome.driver", "");
    driver = new FirefoxDriver();
    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
  }

  @Test
  public void testAuthorize() throws Exception {
    driver.get("localhost:8080");
    driver.findElement(By.id("bar_auth_button")).click();
    driver.findElement(By.id("input_email")).clear();
    driver.findElement(By.id("input_email")).sendKeys("boss@webforum.net");
    driver.findElement(By.id("input_password")).clear();
    driver.findElement(By.id("input_password")).sendKeys("qwerty");
    driver.findElement(By.id("auth_button")).click();
    assertEquals(driver.getTitle(), "Главная страница");
    assertEquals(driver.findElement(By.id("bar_auth_button")).getText(), "BOSS");
    driver.findElement(By.id("bar_auth_button")).click();
    assertEquals(driver.getTitle(), "Профиль");
    driver.findElement(By.cssSelector("#bar_main_title > h3")).click();
    assertEquals(driver.getTitle(), "Главная страница");
  }

  @AfterClass(alwaysRun = true)
  public void tearDown() throws Exception {
    driver.quit();
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
      fail(verificationErrorString);
    }
  }
}
