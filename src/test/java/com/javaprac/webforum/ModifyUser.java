package com.javaprac.webforum;

import java.util.concurrent.TimeUnit;
import org.testng.annotations.*;
import static org.testng.Assert.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;

public class ModifyUser {
  private WebDriver driver;
  private StringBuffer verificationErrors = new StringBuffer();

  @BeforeClass(alwaysRun = true)
  public void setUp() throws Exception {
    driver = new FirefoxDriver();
    driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
  }

  @Test
  public void testModifyUser() throws Exception {
    driver.get("http://localhost:8080/");
    driver.findElement(By.id("bar_auth_button")).click();
    driver.findElement(By.id("input_email")).clear();
    driver.findElement(By.id("input_email")).sendKeys("boss@webforum.net");
    driver.findElement(By.id("input_password")).clear();
    driver.findElement(By.id("input_password")).sendKeys("qwerty");
    driver.findElement(By.id("auth_button")).click();
    driver.findElement(By.xpath("//a[@id='bar_users']/h5")).click();
    driver.findElement(By.id("collapseButton")).click();
    driver.findElement(By.id("new_user_nick")).click();
    driver.findElement(By.id("new_user_nick")).clear();
    driver.findElement(By.id("new_user_nick")).sendKeys("user");
    driver.findElement(By.id("new_user_login")).click();
    driver.findElement(By.id("new_user_login")).clear();
    driver.findElement(By.id("new_user_login")).sendKeys("user@webforum.net");
    driver.findElement(By.id("new_user_password")).click();
    driver.findElement(By.id("new_user_password")).clear();
    driver.findElement(By.id("new_user_password")).sendKeys("123");
    driver.findElement(By.id("add_role_link")).click();
    driver.findElement(By.id("role_row_0")).click();
    driver.findElement(By.id("role_row_0")).clear();
    driver.findElement(By.id("role_row_0")).sendKeys("role1");
    driver.findElement(By.id("add_role_link")).click();
    driver.findElement(By.id("role_row_1")).click();
    driver.findElement(By.id("role_row_1")).clear();
    driver.findElement(By.id("role_row_1")).sendKeys("role2");
    driver.findElement(By.id("new_user_create")).click();
    driver.findElement(By.id("user_link_1")).click();
    assertEquals(driver.getTitle(), "Профиль");
    driver.findElement(By.id("roles_del_button_0")).click();
    driver.findElement(By.id("add_role_link")).click();
    driver.findElement(By.id("roles_role_0")).click();
    driver.findElement(By.id("roles_role_0")).clear();
    driver.findElement(By.id("roles_role_0")).sendKeys("role3");
    driver.findElement(By.id("roles_button_0")).click();
    driver.findElement(By.xpath("//a[@id='bar_main_title']/h3")).click();
    driver.findElement(By.xpath("//a[@id='bar_users']/h5")).click();
    driver.findElement(By.id("user_link_1")).click();
    assertEquals(driver.findElement(By.xpath("//div[@id='roles']/form/span")).getText(), "role2");
    assertEquals(driver.findElement(By.xpath("//div[@id='roles']/form[2]/span")).getText(), "role3");
    driver.findElement(By.id("bar_auth_button")).click();
    driver.findElement(By.linkText("Выйти")).click();
    driver.findElement(By.id("input_email")).click();
    driver.findElement(By.id("input_email")).clear();
    driver.findElement(By.id("input_email")).sendKeys("user@webforum.net");
    driver.findElement(By.id("input_password")).click();
    driver.findElement(By.id("input_password")).clear();
    driver.findElement(By.id("input_password")).sendKeys("123");
    driver.findElement(By.id("auth_button")).click();
    driver.findElement(By.id("bar_auth_button")).click();
    assertEquals(driver.getTitle(), "Профиль");
    driver.findElement(By.id("new_login_field")).click();
    driver.findElement(By.id("new_login_field")).clear();
    driver.findElement(By.id("new_login_field")).sendKeys("new_login@webforum.net");
    driver.findElement(By.id("change_login_button")).click();
    driver.findElement(By.id("new_pass_field")).click();
    driver.findElement(By.id("new_pass_field")).clear();
    driver.findElement(By.id("new_pass_field")).sendKeys("new pass");
    driver.findElement(By.id("new_pass_field_confirm")).click();
    driver.findElement(By.id("new_pass_field_confirm")).clear();
    driver.findElement(By.id("new_pass_field_confirm")).sendKeys("another symbols");
    driver.findElement(By.id("change_pass_button")).click();
    assertEquals(driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='подтверждение:'])[1]/following::span[1]")).getText(), "пароли не совпадают");
    driver.findElement(By.linkText("Выйти")).click();
    driver.findElement(By.id("input_email")).click();
    driver.findElement(By.id("input_email")).clear();
    driver.findElement(By.id("input_email")).sendKeys("new_login@webforum.net");
    driver.findElement(By.id("input_password")).click();
    driver.findElement(By.id("input_password")).clear();
    driver.findElement(By.id("input_password")).sendKeys("123");
    driver.findElement(By.id("auth_button")).click();
    assertEquals(driver.findElement(By.id("bar_auth_button")).getText(), "user");
    driver.findElement(By.id("bar_auth_button")).click();
    driver.findElement(By.id("new_pass_field")).click();
    driver.findElement(By.id("new_pass_field")).clear();
    driver.findElement(By.id("new_pass_field")).sendKeys("new pass");
    driver.findElement(By.id("new_pass_field_confirm")).click();
    driver.findElement(By.id("new_pass_field_confirm")).clear();
    driver.findElement(By.id("new_pass_field_confirm")).sendKeys("new pass");
    driver.findElement(By.id("change_pass_button")).click();
    driver.findElement(By.linkText("Выйти")).click();
    driver.findElement(By.id("input_email")).click();
    driver.findElement(By.id("input_email")).clear();
    driver.findElement(By.id("input_email")).sendKeys("user@webforum.net");
    driver.findElement(By.id("input_password")).click();
    driver.findElement(By.id("input_password")).clear();
    driver.findElement(By.id("input_password")).sendKeys("123");
    driver.findElement(By.id("auth_button")).click();
    assertEquals(driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Пароль:'])[1]/following::span[1]")).getText(), "Неверный логин или пароль");
    driver.findElement(By.id("input_password")).click();
    driver.findElement(By.id("input_password")).clear();
    driver.findElement(By.id("input_password")).sendKeys("123");
    driver.findElement(By.id("auth_button")).click();
    assertEquals(driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Пароль:'])[1]/following::span[1]")).getText(), "Неверный логин или пароль");
    driver.findElement(By.id("input_email")).click();
    driver.findElement(By.id("input_email")).clear();
    driver.findElement(By.id("input_email")).sendKeys("user@webforum.net");
    driver.findElement(By.id("input_password")).click();
    driver.findElement(By.id("input_password")).clear();
    driver.findElement(By.id("input_password")).sendKeys("new pass");
    driver.findElement(By.id("auth_button")).click();
    assertEquals(driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Пароль:'])[1]/following::span[1]")).getText(), "Неверный логин или пароль");
    driver.findElement(By.id("input_email")).click();
    driver.findElement(By.id("input_email")).clear();
    driver.findElement(By.id("input_email")).sendKeys("new_login@webforum.net");
    driver.findElement(By.id("input_password")).click();
    driver.findElement(By.id("input_password")).clear();
    driver.findElement(By.id("input_password")).sendKeys("new pass");
    driver.findElement(By.id("auth_button")).click();
    assertEquals(driver.getTitle(), "Главная страница");
    assertEquals(driver.findElement(By.id("bar_auth_button")).getText(), "user");
    driver.findElement(By.id("bar_auth_button")).click();
    driver.findElement(By.linkText("Выйти")).click();
    driver.findElement(By.id("input_email")).clear();
    driver.findElement(By.id("input_email")).sendKeys("boss@webforum.net");
    driver.findElement(By.id("input_password")).clear();
    driver.findElement(By.id("input_password")).sendKeys("qwerty");
    driver.findElement(By.id("auth_button")).click();
    driver.findElement(By.xpath("//a[@id='bar_users']/h5")).click();
    driver.findElement(By.id("user_link_1")).click();
    driver.findElement(By.linkText("- удалить пользователя")).click();
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
