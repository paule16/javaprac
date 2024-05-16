package com.javaprac.webforum;

import org.testng.annotations.*;
import static org.testng.Assert.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import java.time.Duration;

public class CreateUser {
  private WebDriver driver;
  private StringBuffer verificationErrors = new StringBuffer();

  @BeforeClass(alwaysRun = true)
  public void setUp() throws Exception {
    System.setProperty("webdriver.chrome.driver", "");
    driver = new FirefoxDriver();
    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
  }

  @Test
  public void testCreateUser() throws Exception {
    driver.get("localhost:8080");
    driver.findElement(By.id("bar_auth_button")).click();
    driver.findElement(By.id("input_email")).clear();
    driver.findElement(By.id("input_email")).sendKeys("boss@webforum.net");
    driver.findElement(By.id("input_password")).clear();
    driver.findElement(By.id("input_password")).sendKeys("qwerty");
    driver.findElement(By.id("auth_button")).click();
    driver.findElement(By.id("bar_users")).click();
    assertEquals(driver.getTitle(), "Управление пользователями");
    driver.findElement(By.id("collapseButton")).click();
    driver.findElement(By.id("new_user_login")).clear();
    driver.findElement(By.id("new_user_login")).sendKeys("1@webforum.net");
    driver.findElement(By.id("new_user_password")).clear();
    driver.findElement(By.id("new_user_password")).sendKeys("9Tp-fvA-nNF-jEB");
    driver.findElement(By.id("new_user_nick")).click();
    driver.findElement(By.id("new_user_nick")).clear();
    driver.findElement(By.id("new_user_nick")).sendKeys("moder");
    driver.findElement(By.id("add_role_link")).click();
    assertTrue(isElementPresent(By.id("role_row_0")));
    driver.findElement(By.id("role_row_0")).clear();
    driver.findElement(By.id("role_row_0")).sendKeys("admin");
    driver.findElement(By.id("add_role_link")).click();
    assertTrue(isElementPresent(By.id("role_row_1")));
    driver.findElement(By.id("role_row_1")).clear();
    driver.findElement(By.id("role_row_1")).sendKeys("d");
    driver.findElement(By.id("role_button_1")).click();
    driver.findElement(By.id("add_role_link")).click();
    driver.findElement(By.id("role_row_2")).click();
    assertTrue(isElementPresent(By.id("role_row_2")));
    driver.findElement(By.id("role_row_2")).clear();
    driver.findElement(By.id("role_row_2")).sendKeys("role1");
    driver.findElement(By.id("new_user_create")).click();
    assertEquals(driver.findElement(By.id("user_link_1")).getText(), "moder");
    assertTrue(isElementPresent(By.id("collapseButton")));
    driver.findElement(By.id("collapseButton")).click();
    driver.findElement(By.id("new_user_abort")).click();
    assertTrue(isElementPresent(By.id("collapseButton")));
    driver.findElement(By.id("user_link_1")).click();
    assertEquals(driver.findElement(By.cssSelector(".list-group-item:nth-child(2) > .px-3")).getText(), "admin");
    assertEquals(driver.findElement(By.cssSelector(".list-group-item:nth-child(3) > .px-3")).getText(), "role1");
    driver.findElement(By.id("bar_auth_button")).click();
    driver.findElement(By.linkText("Выйти")).click();
    driver.findElement(By.id("input_email")).clear();
    driver.findElement(By.id("input_email")).sendKeys("1@webforum.net");
    driver.findElement(By.id("input_password")).clear();
    driver.findElement(By.id("input_password")).sendKeys("9Tp-fvA-nNF-jEB");
    driver.findElement(By.id("auth_button")).click();
    assertEquals(driver.findElement(By.id("bar_auth_button")).getText(), "moder");
    driver.findElement(By.id("bar_auth_button")).click();
    driver.findElement(By.linkText("Выйти")).click();
    driver.findElement(By.id("input_email")).clear();
    driver.findElement(By.id("input_email")).sendKeys("boss@webforum.net");
    driver.findElement(By.id("input_password")).clear();
    driver.findElement(By.id("input_password")).sendKeys("qwerty");
    driver.findElement(By.id("auth_button")).click();
    driver.findElement(By.cssSelector("h5")).click();
    driver.findElement(By.id("user_link_1")).click();
    driver.findElement(By.linkText("- удалить пользователя")).click();
    driver.findElement(By.id("bar_auth_button")).click();
    driver.findElement(By.linkText("Выйти")).click();
    driver.findElement(By.id("input_email")).clear();
    driver.findElement(By.id("input_email")).sendKeys("1@webforum.net");
    driver.findElement(By.id("input_password")).clear();
    driver.findElement(By.id("input_password")).sendKeys("9Tp-fvA-nNF-jEB");
    driver.findElement(By.id("auth_button")).click();
    assertEquals(driver.findElement(By.cssSelector(".form-text")).getText(), "Неверный логин или пароль");
  }

  @AfterClass(alwaysRun = true)
  public void tearDown() throws Exception {
    driver.quit();
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
      fail(verificationErrorString);
    }
  }

  private boolean isElementPresent(By by) {
    try {
      driver.findElement(by);
      return true;
    } catch (NoSuchElementException e) {
      return false;
    }
  }
}
