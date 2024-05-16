package com.javaprac.webforum;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class CheckGlobalBan {
  private WebDriver driver;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();

  @BeforeClass(alwaysRun = true)
  public void setUp() throws Exception {
    driver = new FirefoxDriver();
    driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
  }

  @Test
  public void testCheckGlobalBan() throws Exception {
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
    driver.findElement(By.id("new_user_login")).sendKeys("user@webforum.com");
    driver.findElement(By.id("new_user_password")).clear();
    driver.findElement(By.id("new_user_password")).sendKeys("123");
    driver.findElement(By.id("add_role_link")).click();
    driver.findElement(By.id("role_row_0")).click();
    driver.findElement(By.id("role_row_0")).clear();
    driver.findElement(By.id("role_row_0")).sendKeys("role");
    driver.findElement(By.id("new_user_create")).click();
    driver.findElement(By.xpath("//a[@id='bar_main_title']/h3")).click();
    driver.findElement(By.linkText("+ создать раздел")).click();
    driver.findElement(By.id("section_name")).click();
    driver.findElement(By.id("section_name")).clear();
    driver.findElement(By.id("section_name")).sendKeys("Раздел с правами на запись для всех");
    driver.findElement(By.id("perm_tbl_add_button")).click();
    driver.findElement(By.id("perm_tbl_role_0")).click();
    driver.findElement(By.id("perm_tbl_role_0")).clear();
    driver.findElement(By.id("perm_tbl_role_0")).sendKeys("public");
    driver.findElement(By.id("perm_tbl_perm_0")).clear();
    driver.findElement(By.id("perm_tbl_perm_0")).sendKeys("WRITE");
    driver.findElement(By.id("perm_tbl_create_button")).click();
    driver.findElement(By.xpath("//a[@id='bar_main_title']/h3")).click();
    driver.findElement(By.linkText("+ создать раздел")).click();
    driver.findElement(By.id("section_name")).click();
    driver.findElement(By.id("section_name")).clear();
    driver.findElement(By.id("section_name")).sendKeys("Раздел с правами на запись для всех и правами на редактирование для роли role");
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
    driver.findElement(By.id("perm_tbl_perm_1")).sendKeys("EDIT");
    driver.findElement(By.id("perm_tbl_create_button")).click();
    driver.findElement(By.xpath("//a[@id='bar_main_title']/h3")).click();
    driver.findElement(By.linkText("+ создать раздел")).click();
    driver.findElement(By.id("section_name")).click();
    driver.findElement(By.id("section_name")).clear();
    driver.findElement(By.id("section_name")).sendKeys("Раздел с правами по умолчанию");
    driver.findElement(By.id("perm_tbl_create_button")).click();
    driver.findElement(By.id("create_discussion_button")).click();
    driver.findElement(By.id("section_name")).click();
    driver.findElement(By.id("section_name")).clear();
    driver.findElement(By.id("section_name")).sendKeys("Обсуждение с правами по умолчанию");
    driver.findElement(By.id("create_button")).click();
    driver.findElement(By.xpath("//a[@id='bar_section']/h5")).click();
    driver.findElement(By.id("create_discussion_button")).click();
    driver.findElement(By.id("section_name")).click();
    driver.findElement(By.id("section_name")).click();
    driver.findElement(By.id("section_name")).clear();
    driver.findElement(By.id("section_name")).sendKeys("Раздел с публичными правами на запись");
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
    driver.findElement(By.id("section_name")).sendKeys("Обсуждение с публичными правами на запись и правами на редактирование для роли role");
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
    driver.findElement(By.xpath("//a[@id='bar_section']/h5")).click();
    driver.findElement(By.id("create_discussion_button")).click();
    driver.findElement(By.id("section_name")).click();
    driver.findElement(By.id("section_name")).clear();
    driver.findElement(By.id("section_name")).sendKeys("Закрытое обсуждение для пользователей другой роли");
    driver.findElement(By.id("add_button")).click();
    driver.findElement(By.id("perm_tbl_row_role_0")).click();
    driver.findElement(By.id("perm_tbl_row_role_0")).clear();
    driver.findElement(By.id("perm_tbl_row_role_0")).sendKeys("another_role");
    driver.findElement(By.id("perm_tbl_row_perm_0")).clear();
    driver.findElement(By.id("perm_tbl_row_perm_0")).sendKeys("WRITE");
    driver.findElement(By.id("create_button")).click();
    driver.findElement(By.xpath("//a[@id='bar_section']/h5")).click();
    driver.findElement(By.xpath("//a[@id='bar_main_title']/h3")).click();
    driver.findElement(By.linkText("+ создать раздел")).click();
    driver.findElement(By.id("section_name")).click();
    driver.findElement(By.id("section_name")).clear();
    driver.findElement(By.id("section_name")).sendKeys("Закрытый раздел для пользователей другой роли");
    driver.findElement(By.id("perm_tbl_add_button")).click();
    driver.findElement(By.id("perm_tbl_role_0")).click();
    driver.findElement(By.id("perm_tbl_role_0")).clear();
    driver.findElement(By.id("perm_tbl_role_0")).sendKeys("another_role");
    driver.findElement(By.id("perm_tbl_perm_0")).clear();
    driver.findElement(By.id("perm_tbl_perm_0")).sendKeys("WRITE");
    driver.findElement(By.id("perm_tbl_create_button")).click();
    driver.findElement(By.xpath("//a[@id='bar_main_title']/h3")).click();
    driver.findElement(By.xpath("//a[contains(text(),'Раздел с правами на запись для всех')]")).click();
    driver.findElement(By.id("create_discussion_button")).click();
    driver.findElement(By.id("section_name")).click();
    driver.findElement(By.id("section_name")).clear();
    driver.findElement(By.id("section_name")).sendKeys("test");
    driver.findElement(By.id("create_button")).click();
    driver.findElement(By.xpath("//a[@id='bar_main_title']/h3")).click();
    driver.findElement(By.xpath("//a[contains(text(),'Раздел с правами на запись для всех и правами на редактирование для роли role')]")).click();
    driver.findElement(By.id("create_discussion_button")).click();
    driver.findElement(By.id("section_name")).click();
    driver.findElement(By.id("section_name")).clear();
    driver.findElement(By.id("section_name")).sendKeys("test");
    driver.findElement(By.id("create_button")).click();
    driver.findElement(By.xpath("//a[@id='bar_main_title']/h3")).click();
    driver.findElement(By.xpath("//a[contains(text(),'Закрытый раздел для пользователей другой роли')]")).click();
    driver.findElement(By.id("create_discussion_button")).click();
    driver.findElement(By.id("section_name")).click();
    driver.findElement(By.id("section_name")).clear();
    driver.findElement(By.id("section_name")).sendKeys("test");
    driver.findElement(By.id("create_button")).click();
    driver.findElement(By.xpath("//a[@id='bar_main_title']/h3")).click();
    driver.findElement(By.xpath("//a[@id='bar_users']/h5")).click();
    driver.findElement(By.xpath("//a[contains(text(),'user')]")).click();
    driver.findElement(By.id("global_ban_check")).click();
    driver.findElement(By.id("global_ban_change_confirm")).click();
    driver.findElement(By.id("bar_auth_button")).click();
    driver.findElement(By.linkText("Выйти")).click();
    driver.findElement(By.id("input_email")).click();
    driver.findElement(By.id("input_email")).clear();
    driver.findElement(By.id("input_email")).sendKeys("user@webforum.com");
    driver.findElement(By.id("input_password")).clear();
    driver.findElement(By.id("input_password")).sendKeys("123");
    driver.findElement(By.id("auth_button")).click();
    assertEquals(driver.findElement(By.id("bar_auth_button")).getText(), "user");
    driver.findElement(By.xpath("//a[contains(text(),'Раздел с правами на запись для всех')]")).click();
    assertEquals(driver.getTitle(), "Раздел с правами на запись для всех");
    driver.findElement(By.xpath("//a[contains(text(),'test')]")).click();
    assertEquals(driver.getTitle(), "test");
    for (int second = 0;; second++) {
    	if (second >= 60) fail("timeout");
    	try { if (!isElementPresent(By.id("new_msg_input"))) break; } catch (Exception e) {}
    	Thread.sleep(1000);
    }

    driver.findElement(By.xpath("//a[@id='bar_main_title']/h3")).click();
    driver.findElement(By.xpath("//a[contains(text(),'Раздел с правами на запись для всех и правами на редактирование для роли role')]")).click();
    assertEquals(driver.getTitle(), "Раздел с правами на запись для всех и правами на редактирование для роли role");
    for (int second = 0;; second++) {
    	if (second >= 60) fail("timeout");
    	try { if (!isElementPresent(By.id("create_discussion_button"))) break; } catch (Exception e) {}
    	Thread.sleep(1000);
    }

    driver.findElement(By.xpath("//a[contains(text(),'test')]")).click();
    assertEquals(driver.getTitle(), "test");
    for (int second = 0;; second++) {
    	if (second >= 60) fail("timeout");
    	try { if (!isElementPresent(By.id("new_msg_input"))) break; } catch (Exception e) {}
    	Thread.sleep(1000);
    }

    driver.findElement(By.xpath("//a[@id='bar_main_title']/h3")).click();
    driver.findElement(By.xpath("//a[contains(text(),'Закрытый раздел для пользователей другой роли')]")).click();
    assertEquals(driver.getTitle(), "Нет доступа");
    driver.findElement(By.xpath("//a[@id='bar_main_title']/h3")).click();
    driver.findElement(By.xpath("//a[contains(text(),'Раздел с правами по умолчанию')]")).click();
    assertEquals(driver.getTitle(), "Раздел с правами по умолчанию");
    driver.findElement(By.xpath("//a[contains(text(),'Обсуждение с правами по умолчанию')]")).click();
    assertEquals(driver.getTitle(), "Обсуждение с правами по умолчанию");
    for (int second = 0;; second++) {
    	if (second >= 60) fail("timeout");
    	try { if (!isElementPresent(By.id("new_msg_input"))) break; } catch (Exception e) {}
    	Thread.sleep(1000);
    }

    driver.findElement(By.xpath("//a[@id='bar_section']/h5")).click();
    driver.findElement(By.xpath("//a[contains(text(),'Обсуждение с публичными правами на запись и правами на редактирование для роли role')]")).click();
    assertEquals(driver.getTitle(), "Обсуждение с публичными правами на запись и правами на редактирование для роли role");
    for (int second = 0;; second++) {
    	if (second >= 60) fail("timeout");
    	try { if (!isElementPresent(By.id("new_msg_input"))) break; } catch (Exception e) {}
    	Thread.sleep(1000);
    }

    driver.findElement(By.xpath("//a[@id='bar_section']/h5")).click();
    driver.findElement(By.xpath("//a[contains(text(),'Раздел с публичными правами на запись')]")).click();
    assertEquals(driver.getTitle(), "Раздел с публичными правами на запись");
    for (int second = 0;; second++) {
    	if (second >= 60) fail("timeout");
    	try { if (!isElementPresent(By.id("new_msg_input"))) break; } catch (Exception e) {}
    	Thread.sleep(1000);
    }

    driver.findElement(By.xpath("//a[@id='bar_section']/h5")).click();
    driver.findElement(By.xpath("//a[contains(text(),'Закрытое обсуждение для пользователей другой роли')]")).click();
    assertEquals(driver.getTitle(), "Нет доступа");
    driver.findElement(By.xpath("//a[@id='bar_main_title']/h3")).click();
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
    driver.findElement(By.id("section_1_link")).click();
    driver.findElement(By.id("delete_discussion_button")).click();
    driver.findElement(By.id("section_0_link")).click();
    driver.findElement(By.id("delete_discussion_button")).click();
    driver.findElement(By.id("bar_users")).click();
    driver.findElement(By.xpath("//a[contains(text(),'user')]")).click();
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

