package com.javaprac.webforum;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class CheckSectionBan {
  private WebDriver driver;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();

  @BeforeClass(alwaysRun = true)
  public void setUp() throws Exception {
    driver = new FirefoxDriver();
    driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
  }

  @Test
  public void testCheckSectionBan() throws Exception {
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
    driver.findElement(By.id("section_name")).sendKeys("Раздел без настроек доступа");
    driver.findElement(By.id("perm_tbl_create_button")).click();
    driver.findElement(By.id("create_discussion_button")).click();
    driver.findElement(By.id("section_name")).click();
    driver.findElement(By.id("section_name")).clear();
    driver.findElement(By.id("section_name")).sendKeys("Обсуждение с публичными правами записи");
    driver.findElement(By.id("add_button")).click();
    driver.findElement(By.id("perm_tbl_row_role_0")).click();
    driver.findElement(By.id("perm_tbl_row_role_0")).clear();
    driver.findElement(By.id("perm_tbl_row_role_0")).sendKeys("public");
    driver.findElement(By.id("perm_tbl_row_perm_0")).clear();
    driver.findElement(By.id("perm_tbl_row_perm_0")).sendKeys("WRITE");
    driver.findElement(By.id("create_button")).click();
    driver.findElement(By.xpath("//a[@id='bar_section']/h5")).click();
    driver.findElement(By.id("create_discussion_button")).click();
    driver.findElement(By.id("section_name")).click();
    driver.findElement(By.id("section_name")).clear();
    driver.findElement(By.id("section_name")).sendKeys("Обсуждение пользоваетлей роли с публичными правами записи");
    driver.findElement(By.id("add_button")).click();
    driver.findElement(By.id("perm_tbl_row_role_0")).click();
    driver.findElement(By.id("perm_tbl_row_role_0")).clear();
    driver.findElement(By.id("perm_tbl_row_role_0")).sendKeys("public");
    driver.findElement(By.id("perm_tbl_row_perm_0")).clear();
    driver.findElement(By.id("perm_tbl_row_perm_0")).sendKeys("WRITE");
    driver.findElement(By.id("add_button")).click();
    driver.findElement(By.id("perm_tbl_row_role_1")).click();
    driver.findElement(By.id("perm_tbl_row_role_1")).clear();
    driver.findElement(By.id("perm_tbl_row_role_1")).sendKeys("role");
    driver.findElement(By.id("perm_tbl_row_perm_1")).clear();
    driver.findElement(By.id("perm_tbl_row_perm_1")).sendKeys("EDIT");
    driver.findElement(By.id("create_button")).click();
    driver.findElement(By.xpath("//a[@id='bar_main_title']/h3")).click();
    driver.findElement(By.linkText("+ создать раздел")).click();
    driver.findElement(By.id("section_name")).click();
    driver.findElement(By.id("section_name")).clear();
    driver.findElement(By.id("section_name")).sendKeys("Раздел с публичными правами записи");
    driver.findElement(By.id("perm_tbl_add_button")).click();
    driver.findElement(By.id("perm_tbl_role_0")).click();
    driver.findElement(By.id("perm_tbl_role_0")).clear();
    driver.findElement(By.id("perm_tbl_role_0")).sendKeys("public");
    driver.findElement(By.id("perm_tbl_perm_0")).clear();
    driver.findElement(By.id("perm_tbl_perm_0")).sendKeys("WRITE");
    driver.findElement(By.id("perm_tbl_add_button")).click();
    driver.findElement(By.id("perm_tbl_role_1")).click();
    driver.findElement(By.id("perm_tbl_role_1")).clear();
    driver.findElement(By.id("perm_tbl_role_1")).sendKeys("role");
    driver.findElement(By.id("perm_tbl_perm_1")).clear();
    driver.findElement(By.id("perm_tbl_perm_1")).sendKeys("WRITE");
    driver.findElement(By.id("perm_tbl_create_button")).click();
    driver.findElement(By.id("create_discussion_button")).click();
    driver.findElement(By.id("section_name")).click();
    driver.findElement(By.id("section_name")).clear();
    driver.findElement(By.id("section_name")).sendKeys("Обсуждение без правил доступа");
    driver.findElement(By.id("create_button")).click();
    driver.findElement(By.xpath("//a[@id='bar_section']/h5")).click();
    driver.findElement(By.id("create_discussion_button")).click();
    driver.findElement(By.id("section_name")).click();
    driver.findElement(By.id("section_name")).clear();
    driver.findElement(By.id("section_name")).sendKeys("Обсуждение с публичными правами на чтения");
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
    driver.findElement(By.id("section_name")).sendKeys("Обсуждение с публичными правами на запись");
    driver.findElement(By.id("add_button")).click();
    driver.findElement(By.id("perm_tbl_row_role_0")).click();
    driver.findElement(By.id("perm_tbl_row_role_0")).clear();
    driver.findElement(By.id("perm_tbl_row_role_0")).sendKeys("public");
    driver.findElement(By.id("perm_tbl_row_perm_0")).clear();
    driver.findElement(By.id("perm_tbl_row_perm_0")).sendKeys("WRITE");
    driver.findElement(By.id("add_button")).click();
    driver.findElement(By.id("perm_tbl_row_role_1")).click();
    driver.findElement(By.id("perm_tbl_row_role_1")).clear();
    driver.findElement(By.id("perm_tbl_row_role_1")).sendKeys("role");
    driver.findElement(By.id("perm_tbl_row_perm_1")).clear();
    driver.findElement(By.id("perm_tbl_row_perm_1")).sendKeys("EDIT");
    driver.findElement(By.id("create_button")).click();
    driver.findElement(By.xpath("//a[@id='bar_main_title']/h3")).click();
    driver.findElement(By.xpath("//a[@id='bar_users']/h5")).click();
    driver.findElement(By.id("collapseButton")).click();
    driver.findElement(By.id("new_user_nick")).click();
    driver.findElement(By.id("new_user_nick")).clear();
    driver.findElement(By.id("new_user_nick")).sendKeys("user");
    driver.findElement(By.id("new_user_login")).click();
    driver.findElement(By.id("new_user_login")).clear();
    driver.findElement(By.id("new_user_login")).sendKeys("user@webforum.net");
    driver.findElement(By.id("new_user_password")).click();
    driver.findElement(By.id("new_user_password")).click();
    driver.findElement(By.id("new_user_password")).clear();
    driver.findElement(By.id("new_user_password")).sendKeys("123");
    driver.findElement(By.id("add_role_link")).click();
    driver.findElement(By.id("role_row_0")).clear();
    driver.findElement(By.id("role_row_0")).sendKeys("role");
    driver.findElement(By.id("new_user_create")).click();
    driver.findElement(By.id("bar_auth_button")).click();
    driver.findElement(By.linkText("Выйти")).click();
    driver.findElement(By.id("input_email")).click();
    driver.findElement(By.id("input_email")).clear();
    driver.findElement(By.id("input_email")).sendKeys("user@webforum.net");
    driver.findElement(By.id("input_password")).clear();
    driver.findElement(By.id("input_password")).sendKeys("123");
    driver.findElement(By.id("auth_button")).click();
    assertEquals(driver.findElement(By.id("bar_auth_button")).getText(), "user");
    driver.findElement(By.xpath("//a[contains(text(),'Раздел без настроек доступа')]")).click();
    driver.findElement(By.xpath("//a[contains(text(),'Обсуждение с публичными правами записи')]")).click();
    assertEquals(driver.getTitle(), "Обсуждение с публичными правами записи");
    assertTrue(isElementPresent(By.id("new_msg_input")));
    driver.findElement(By.xpath("//a[@id='bar_section']/h5")).click();
    driver.findElement(By.xpath("//a[contains(text(),'Обсуждение пользоваетлей роли с публичными правами записи')]")).click();
    assertEquals(driver.getTitle(), "Обсуждение пользоваетлей роли с публичными правами записи");
    assertTrue(isElementPresent(By.id("new_msg_input")));
    driver.findElement(By.id("new_msg_input")).click();
    driver.findElement(By.id("new_msg_input")).clear();
    driver.findElement(By.id("new_msg_input")).sendKeys("test message");
    driver.findElement(By.id("msg_create_button")).click();
    driver.findElement(By.xpath("//a[@id='bar_main_title']/h3")).click();
    driver.findElement(By.xpath("//a[contains(text(),'Раздел с публичными правами записи')]")).click();
    driver.findElement(By.xpath("//a[contains(text(),'Обсуждение без правил доступа')]")).click();
    assertEquals(driver.getTitle(), "Обсуждение без правил доступа");
    assertTrue(isElementPresent(By.id("new_msg_input")));
    driver.findElement(By.id("bar_section")).click();
    driver.findElement(By.xpath("//a[contains(text(),'Обсуждение с публичными правами на чтения')]")).click();
    assertEquals(driver.getTitle(), "Обсуждение с публичными правами на чтения");
    assertTrue(isElementPresent(By.id("new_msg_input")));
    driver.findElement(By.id("bar_section")).click();
    driver.findElement(By.xpath("//a[contains(text(),'Обсуждение с публичными правами на запись')]")).click();
    assertEquals(driver.getTitle(), "Обсуждение с публичными правами на запись");
    assertTrue(isElementPresent(By.id("new_msg_input")));
    driver.findElement(By.id("new_msg_input")).click();
    driver.findElement(By.id("new_msg_input")).clear();
    driver.findElement(By.id("new_msg_input")).sendKeys("test message");
    driver.findElement(By.id("msg_create_button")).click();
    driver.findElement(By.id("bar_auth_button")).click();
    driver.findElement(By.linkText("Выйти")).click();
    driver.findElement(By.id("input_email")).clear();
    driver.findElement(By.id("input_email")).sendKeys("boss@webforum.net");
    driver.findElement(By.id("input_password")).clear();
    driver.findElement(By.id("input_password")).sendKeys("qwerty");
    driver.findElement(By.id("auth_button")).click();
    driver.findElement(By.xpath("//a[contains(text(),'Раздел без настроек доступа')]")).click();
    driver.findElement(By.id("participants_tab")).click();
    assertEquals(driver.findElement(By.xpath("//form[@id='participants_tab_pane']/table/tbody/tr/td")).getText(), "user");
    driver.findElement(By.xpath("//form[@id='participants_tab_pane']/table/tbody/tr/td[5]/input")).click();
    driver.findElement(By.id("section_table_save_button")).click();
    assertEquals(closeAlertAndGetItsText(), "Изменения применены!");
    driver.findElement(By.xpath("//a[@id='bar_main_title']/h3")).click();
    driver.findElement(By.xpath("//a[contains(text(),'Раздел с публичными правами записи')]")).click();
    driver.findElement(By.id("participants_tab")).click();
    assertEquals(driver.findElement(By.xpath("//form[@id='participants_tab_pane']/table/tbody/tr/td")).getText(), "user");
    driver.findElement(By.xpath("//form[@id='participants_tab_pane']/table/tbody/tr/td[5]/input")).click();
    driver.findElement(By.id("section_table_save_button")).click();
    assertEquals(closeAlertAndGetItsText(), "Изменения применены!");
    driver.findElement(By.id("bar_auth_button")).click();
    driver.findElement(By.linkText("Выйти")).click();
    driver.findElement(By.id("input_email")).click();
    driver.findElement(By.id("input_email")).clear();
    driver.findElement(By.id("input_email")).sendKeys("user@webforum.net");
    driver.findElement(By.id("input_password")).click();
    driver.findElement(By.id("input_password")).clear();
    driver.findElement(By.id("input_password")).sendKeys("123");
    driver.findElement(By.id("auth_button")).click();
    driver.findElement(By.xpath("//a[contains(text(),'Раздел без настроек доступа')]")).click();
    driver.findElement(By.xpath("//a[contains(text(),'Обсуждение с публичными правами записи')]")).click();
    assertEquals(driver.getTitle(), "Обсуждение с публичными правами записи");
    assertFalse(isElementPresent(By.id("new_msg_input")));
    driver.findElement(By.xpath("//a[@id='bar_section']/h5")).click();
    driver.findElement(By.xpath("//a[contains(text(),'Обсуждение пользоваетлей роли с публичными правами записи')]")).click();
    assertEquals(driver.getTitle(), "Обсуждение пользоваетлей роли с публичными правами записи");
    assertFalse(isElementPresent(By.id("new_msg_input")));
    driver.findElement(By.xpath("//a[@id='bar_main_title']/h3")).click();
    driver.findElement(By.xpath("//a[contains(text(),'Раздел с публичными правами записи')]")).click();
    driver.findElement(By.xpath("//a[contains(text(),'Обсуждение без правил доступа')]")).click();
    assertEquals(driver.getTitle(), "Обсуждение без правил доступа");
    assertFalse(isElementPresent(By.id("new_msg_input")));
    driver.findElement(By.id("bar_section")).click();
    driver.findElement(By.xpath("//a[contains(text(),'Обсуждение с публичными правами на чтения')]")).click();
    assertEquals(driver.getTitle(), "Обсуждение с публичными правами на чтения");
    assertFalse(isElementPresent(By.id("new_msg_input")));
    driver.findElement(By.xpath("//a[@id='bar_section']/h5")).click();
    driver.findElement(By.xpath("//a[contains(text(),'Обсуждение с публичными правами на запись')]")).click();
    assertEquals(driver.getTitle(), "Обсуждение с публичными правами на запись");
    assertFalse(isElementPresent(By.id("new_msg_input")));
    driver.findElement(By.id("bar_auth_button")).click();
    driver.findElement(By.linkText("Выйти")).click();
    driver.findElement(By.id("input_email")).clear();
    driver.findElement(By.id("input_email")).sendKeys("boss@webforum.net");
    driver.findElement(By.id("input_password")).clear();
    driver.findElement(By.id("input_password")).sendKeys("qwerty");
    driver.findElement(By.id("auth_button")).click();
    driver.findElement(By.id("section_0_link")).click();
    driver.findElement(By.id("delete_discussion_button")).click();
    driver.findElement(By.id("section_0_link")).click();
    driver.findElement(By.id("delete_discussion_button")).click();
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

  private String closeAlertAndGetItsText() {
    try {
      Alert alert = driver.switchTo().alert();
      String alertText = alert.getText();
      if (acceptNextAlert) {
        alert.accept();
      } else {
        alert.dismiss();
      }
      return alertText;
    } finally {
      acceptNextAlert = true;
    }
  }
}

