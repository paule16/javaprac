package com.javaprac.webforum;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class CheckDiscussionAccess {
  private WebDriver driver;
  private StringBuffer verificationErrors = new StringBuffer();

  @BeforeClass(alwaysRun = true)
  public void setUp() throws Exception {
    driver = new FirefoxDriver();
    driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
  }

  @Test
  public void testCheckDiscussionAccess() throws Exception {
    driver.get("http://localhost:8080/");
    driver.findElement(By.id("bar_auth_button")).click();
    driver.findElement(By.id("input_email")).clear();
    driver.findElement(By.id("input_email")).sendKeys("boss@webforum.net");
    driver.findElement(By.id("input_password")).clear();
    driver.findElement(By.id("input_password")).sendKeys("qwerty");
    driver.findElement(By.id("auth_button")).click();
    driver.findElement(By.linkText("+ создать раздел")).click();
    driver.findElement(By.id("section_name")).click();
    driver.findElement(By.id("section_name")).clear();
    driver.findElement(By.id("section_name")).sendKeys("test");
    driver.findElement(By.id("perm_tbl_create_button")).click();
    driver.findElement(By.id("create_discussion_button")).click();
    driver.findElement(By.id("section_name")).click();
    driver.findElement(By.id("section_name")).clear();
    driver.findElement(By.id("section_name")).sendKeys("Обсулждение без прав доступа");
    driver.findElement(By.id("create_button")).click();
    driver.findElement(By.xpath("//a[@id='bar_section']/h5")).click();
    driver.findElement(By.id("create_discussion_button")).click();
    driver.findElement(By.id("section_name")).click();
    driver.findElement(By.id("section_name")).clear();
    driver.findElement(By.id("section_name")).sendKeys("Обсуждение с публичными правами на запись");
    driver.findElement(By.id("add_button")).click();
    driver.findElement(By.id("perm_tbl_row_role_0")).click();
    driver.findElement(By.id("perm_tbl_row_role_0")).clear();
    driver.findElement(By.id("perm_tbl_row_role_0")).sendKeys("public");
    driver.findElement(By.id("perm_tbl_row_perm_0")).clear();
    driver.findElement(By.id("perm_tbl_row_perm_0")).sendKeys("WRITE");
    driver.findElement(By.id("create_button")).click();
    driver.findElement(By.id("bar_section")).click();
    driver.findElement(By.id("create_discussion_button")).click();
    driver.findElement(By.id("section_name")).click();
    driver.findElement(By.id("section_name")).clear();
    driver.findElement(By.id("section_name")).sendKeys("Закрытое обсуждение для пользователей роли role");
    driver.findElement(By.id("add_button")).click();
    driver.findElement(By.id("perm_tbl_row_role_0")).click();
    driver.findElement(By.id("perm_tbl_row_role_0")).clear();
    driver.findElement(By.id("perm_tbl_row_role_0")).sendKeys("role");
    driver.findElement(By.id("perm_tbl_row_perm_0")).click();
    driver.findElement(By.id("perm_tbl_row_perm_0")).clear();
    driver.findElement(By.id("perm_tbl_row_perm_0")).sendKeys("WRITE");
    driver.findElement(By.id("create_button")).click();
    driver.findElement(By.xpath("//a[@id='bar_section']/h5")).click();
    driver.findElement(By.id("create_discussion_button")).click();
    driver.findElement(By.id("section_name")).click();
    driver.findElement(By.id("section_name")).clear();
    driver.findElement(By.id("section_name")).sendKeys("Обсуждение пользователей роли role с публичными правами на чтение");
    driver.findElement(By.id("add_button")).click();
    driver.findElement(By.id("perm_tbl_row_role_0")).click();
    driver.findElement(By.id("perm_tbl_row_role_0")).clear();
    driver.findElement(By.id("perm_tbl_row_role_0")).sendKeys("role");
    driver.findElement(By.id("perm_tbl_row_perm_0")).clear();
    driver.findElement(By.id("perm_tbl_row_perm_0")).sendKeys("WRITE");
    driver.findElement(By.id("add_button")).click();
    driver.findElement(By.id("perm_tbl_row_role_1")).click();
    driver.findElement(By.id("perm_tbl_row_role_1")).clear();
    driver.findElement(By.id("perm_tbl_row_role_1")).sendKeys("public");
    driver.findElement(By.id("perm_tbl_row_perm_1")).clear();
    driver.findElement(By.id("perm_tbl_row_perm_1")).sendKeys("READ");
    driver.findElement(By.id("create_button")).click();
    driver.findElement(By.xpath("//a[@id='bar_main_title']/h3")).click();
    driver.findElement(By.xpath("//a[@id='bar_users']/h5")).click();
    driver.findElement(By.id("collapseButton")).click();
    driver.findElement(By.id("new_user_nick")).click();
    driver.findElement(By.id("new_user_nick")).clear();
    driver.findElement(By.id("new_user_nick")).sendKeys("public_user");
    driver.findElement(By.id("new_user_login")).click();
    driver.findElement(By.id("new_user_login")).clear();
    driver.findElement(By.id("new_user_login")).sendKeys("public@webforum.net");
    driver.findElement(By.id("new_user_password")).click();
    driver.findElement(By.id("new_user_password")).clear();
    driver.findElement(By.id("new_user_password")).sendKeys("123");
    driver.findElement(By.id("new_user_create")).click();
    driver.findElement(By.id("collapseButton")).click();
    driver.findElement(By.id("new_user_nick")).click();
    driver.findElement(By.id("new_user_nick")).clear();
    driver.findElement(By.id("new_user_nick")).sendKeys("role");
    driver.findElement(By.id("new_user_login")).click();
    driver.findElement(By.id("new_user_login")).clear();
    driver.findElement(By.id("new_user_login")).sendKeys("role@webforum.net");
    driver.findElement(By.id("new_user_password")).click();
    driver.findElement(By.id("new_user_password")).clear();
    driver.findElement(By.id("new_user_password")).sendKeys("123");
    driver.findElement(By.id("add_role_link")).click();
    driver.findElement(By.id("role_row_0")).click();
    driver.findElement(By.id("role_row_0")).clear();
    driver.findElement(By.id("role_row_0")).sendKeys("role");
    driver.findElement(By.id("new_user_create")).click();
    driver.findElement(By.id("bar_auth_button")).click();
    driver.findElement(By.linkText("Выйти")).click();
    driver.findElement(By.xpath("//a[@id='bar_main_title']/h3")).click();
    driver.findElement(By.id("section_0_link")).click();
    driver.findElement(By.xpath("//a[contains(text(),'Обсулждение без прав доступа')]")).click();
    assertEquals(driver.getTitle(), "Обсулждение без прав доступа");
    assertFalse(isElementPresent(By.id("new_msg_input")));
    driver.findElement(By.xpath("//a[@id='bar_section']/h5")).click();
    driver.findElement(By.xpath("//a[contains(text(),'Обсуждение с публичными правами на запись')]")).click();
    assertEquals(driver.getTitle(), "Обсуждение с публичными правами на запись");
    assertFalse(isElementPresent(By.id("new_msg_input")));
    driver.findElement(By.id("bar_section")).click();
    driver.findElement(By.xpath("//a[contains(text(),'Закрытое обсуждение для пользователей роли role')]")).click();
    assertEquals(driver.getTitle(), "Нет доступа");
    driver.findElement(By.xpath("//a[@id='bar_main_title']/h3")).click();
    driver.findElement(By.id("section_0_link")).click();
    driver.findElement(By.xpath("//a[contains(text(),'Обсуждение пользователей роли role с публичными правами на чтение')]")).click();
    assertEquals(driver.getTitle(), "Обсуждение пользователей роли role с публичными правами на чтение");
    assertFalse(isElementPresent(By.id("new_msg_input")));
    driver.findElement(By.id("bar_auth_button")).click();
    driver.findElement(By.id("input_email")).click();
    driver.findElement(By.id("input_email")).clear();
    driver.findElement(By.id("input_email")).sendKeys("public@webforum.net");
    driver.findElement(By.id("input_password")).click();
    driver.findElement(By.id("input_password")).clear();
    driver.findElement(By.id("input_password")).sendKeys("123");
    driver.findElement(By.id("auth_button")).click();
    assertEquals(driver.findElement(By.id("bar_auth_button")).getText(), "public_user");
    driver.findElement(By.id("section_0_link")).click();
    driver.findElement(By.xpath("//a[contains(text(),'Обсулждение без прав доступа')]")).click();
    assertEquals(driver.getTitle(), "Обсулждение без прав доступа");
    assertFalse(isElementPresent(By.id("new_msg_input")));
    driver.findElement(By.xpath("//a[@id='bar_section']/h5")).click();
    driver.findElement(By.xpath("//a[contains(text(),'Обсуждение с публичными правами на запись')]")).click();
    assertEquals(driver.getTitle(), "Обсуждение с публичными правами на запись");
    assertTrue(isElementPresent(By.id("new_msg_input")));
    driver.findElement(By.id("bar_section")).click();
    driver.findElement(By.xpath("//a[contains(text(),'Обсуждение пользователей роли role с публичными правами на чтение')]")).click();
    assertEquals(driver.getTitle(), "Обсуждение пользователей роли role с публичными правами на чтение");
    assertFalse(isElementPresent(By.id("new_msg_input")));
    driver.findElement(By.id("bar_section")).click();
    driver.findElement(By.xpath("//a[contains(text(),'Закрытое обсуждение для пользователей роли role')]")).click();
    assertEquals(driver.getTitle(), "Нет доступа");
    driver.findElement(By.id("bar_auth_button")).click();
    driver.findElement(By.linkText("Выйти")).click();
    driver.findElement(By.id("input_email")).click();
    driver.findElement(By.id("input_email")).clear();
    driver.findElement(By.id("input_email")).sendKeys("role@webforum.net");
    driver.findElement(By.id("input_password")).clear();
    driver.findElement(By.id("input_password")).sendKeys("123");
    driver.findElement(By.id("auth_button")).click();
    assertEquals(driver.findElement(By.id("bar_auth_button")).getText(), "role");
    driver.findElement(By.id("section_0_link")).click();
    driver.findElement(By.xpath("//a[contains(text(),'Обсулждение без прав доступа')]")).click();
    assertEquals(driver.getTitle(), "Обсулждение без прав доступа");
    assertFalse(isElementPresent(By.id("new_msg_input")));
    driver.findElement(By.xpath("//a[@id='bar_section']/h5")).click();
    driver.findElement(By.xpath("//a[contains(text(),'Обсуждение с публичными правами на запись')]")).click();
    assertEquals(driver.getTitle(), "Обсуждение с публичными правами на запись");
    assertTrue(isElementPresent(By.id("new_msg_input")));
    driver.findElement(By.xpath("//a[@id='bar_section']/h5")).click();
    driver.findElement(By.xpath("//a[contains(text(),'Закрытое обсуждение для пользователей роли role')]")).click();
    assertEquals(driver.getTitle(), "Закрытое обсуждение для пользователей роли role");
    assertTrue(isElementPresent(By.id("new_msg_input")));
    driver.findElement(By.xpath("//a[@id='bar_section']/h5")).click();
    driver.findElement(By.xpath("//a[contains(text(),'Обсуждение пользователей роли role с публичными правами на чтение')]")).click();
    assertEquals(driver.getTitle(), "Обсуждение пользователей роли role с публичными правами на чтение");
    assertTrue(isElementPresent(By.id("new_msg_input")));
    driver.findElement(By.id("bar_auth_button")).click();
    driver.findElement(By.linkText("Выйти")).click();
    driver.findElement(By.id("input_email")).clear();
    driver.findElement(By.id("input_email")).sendKeys("boss@webforum.net");
    driver.findElement(By.id("input_password")).clear();
    driver.findElement(By.id("input_password")).sendKeys("qwerty");
    driver.findElement(By.id("auth_button")).click();
    driver.findElement(By.id("section_0_link")).click();
    driver.findElement(By.id("delete_discussion_button")).click();
    driver.findElement(By.xpath("//a[@id='bar_users']/h5")).click();
    driver.findElement(By.id("user_link_1")).click();
    driver.findElement(By.linkText("- удалить пользователя")).click();
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

  private boolean isElementPresent(By by) {
    try {
      driver.findElement(by);
      return true;
    } catch (NoSuchElementException e) {
      return false;
    }
  }
}

