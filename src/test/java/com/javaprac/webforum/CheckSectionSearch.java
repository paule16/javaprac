package com.javaprac.webforum;

import java.util.concurrent.TimeUnit;
import org.testng.annotations.*;
import static org.testng.Assert.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;

public class CheckSectionSearch {
  private WebDriver driver;
  private StringBuffer verificationErrors = new StringBuffer();

  @BeforeClass(alwaysRun = true)
  public void setUp() throws Exception {
    driver = new FirefoxDriver();
    driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
  }

  @Test
  public void testCheckSectionSearch() throws Exception {
    driver.get("http://localhost:8080/");
    driver.findElement(By.id("bar_auth_button")).click();
    driver.findElement(By.id("input_email")).clear();
    driver.findElement(By.id("input_email")).sendKeys("boss@webforum.net");
    driver.findElement(By.id("input_password")).clear();
    driver.findElement(By.id("input_password")).sendKeys("qwerty");
    driver.findElement(By.id("auth_button")).click();
    driver.findElement(By.id("bar_users")).click();
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
    driver.findElement(By.id("role_row_0")).sendKeys("role");
    driver.findElement(By.id("new_user_create")).click();
    driver.findElement(By.xpath("//a[@id='bar_main_title']/h3")).click();
    driver.findElement(By.linkText("+ создать раздел")).click();
    driver.findElement(By.id("section_name")).click();
    driver.findElement(By.id("section_name")).clear();
    driver.findElement(By.id("section_name")).sendKeys("Test section");
    driver.findElement(By.id("perm_tbl_create_button")).click();
    driver.findElement(By.id("create_discussion_button")).click();
    driver.findElement(By.id("section_name")).click();
    driver.findElement(By.id("section_name")).clear();
    driver.findElement(By.id("section_name")).sendKeys("Public discussion");
    driver.findElement(By.id("add_button")).click();
    driver.findElement(By.id("perm_tbl_row_role_0")).click();
    driver.findElement(By.id("perm_tbl_row_role_0")).clear();
    driver.findElement(By.id("perm_tbl_row_role_0")).sendKeys("public");
    driver.findElement(By.id("perm_tbl_row_perm_0")).clear();
    driver.findElement(By.id("perm_tbl_row_perm_0")).sendKeys("READ");
    driver.findElement(By.id("create_button")).click();
    driver.findElement(By.id("new_msg_input")).click();
    driver.findElement(By.id("new_msg_input")).clear();
    driver.findElement(By.id("new_msg_input")).sendKeys("accessible mesage (public discussion)");
    driver.findElement(By.id("msg_create_button")).click();
    driver.findElement(By.xpath("//a[@id='bar_section']/h5")).click();
    driver.findElement(By.id("create_discussion_button")).click();
    driver.findElement(By.id("section_name")).click();
    driver.findElement(By.id("section_name")).clear();
    driver.findElement(By.id("section_name")).sendKeys("Private discussion");
    driver.findElement(By.id("add_button")).click();
    driver.findElement(By.id("perm_tbl_row_role_0")).click();
    driver.findElement(By.id("perm_tbl_row_role_0")).clear();
    driver.findElement(By.id("perm_tbl_row_role_0")).sendKeys("role");
    driver.findElement(By.id("perm_tbl_row_perm_0")).clear();
    driver.findElement(By.id("perm_tbl_row_perm_0")).sendKeys("READ");
    driver.findElement(By.id("create_button")).click();
    driver.findElement(By.id("new_msg_input")).click();
    driver.findElement(By.id("new_msg_input")).clear();
    driver.findElement(By.id("new_msg_input")).sendKeys("accessible message (private discussion)");
    driver.findElement(By.id("msg_create_button")).click();
    driver.findElement(By.id("bar_section")).click();
    driver.findElement(By.id("create_discussion_button")).click();
    driver.findElement(By.id("section_name")).click();
    driver.findElement(By.id("section_name")).clear();
    driver.findElement(By.id("section_name")).sendKeys("Inaccessible discussion");
    driver.findElement(By.id("add_button")).click();
    driver.findElement(By.id("perm_tbl_row_role_0")).click();
    driver.findElement(By.id("perm_tbl_row_role_0")).clear();
    driver.findElement(By.id("perm_tbl_row_role_0")).sendKeys("another_role");
    driver.findElement(By.id("perm_tbl_row_perm_0")).clear();
    driver.findElement(By.id("perm_tbl_row_perm_0")).sendKeys("READ");
    driver.findElement(By.id("create_button")).click();
    driver.findElement(By.id("new_msg_input")).click();
    driver.findElement(By.id("new_msg_input")).clear();
    driver.findElement(By.id("new_msg_input")).sendKeys("inaccessible message (private discussion)");
    driver.findElement(By.id("msg_create_button")).click();
    driver.findElement(By.xpath("//a[@id='bar_section']/h5")).click();
    driver.findElement(By.id("bar_search")).click();
    driver.findElement(By.id("bar_search")).clear();
    driver.findElement(By.id("bar_search")).sendKeys("accessible");
    driver.findElement(By.id("bar_search_button")).click();
    assertEquals(driver.findElement(By.xpath("//*/text()[normalize-space(.)='accessible mesage (public discussion)']/parent::*")).getText(), "accessible mesage (public discussion)");
    assertEquals(driver.findElement(By.xpath("//*/text()[normalize-space(.)='accessible message (private discussion)']/parent::*")).getText(), "accessible message (private discussion)");
    driver.findElement(By.id("bar_search")).click();
    driver.findElement(By.id("bar_search")).clear();
    driver.findElement(By.id("bar_search")).sendKeys("inaccessible");
    driver.findElement(By.id("bar_search_button")).click();
    assertEquals(driver.findElement(By.xpath("//*/text()[normalize-space(.)='inaccessible message (private discussion)']/parent::*")).getText(), "inaccessible message (private discussion)");
    driver.findElement(By.id("bar_auth_button")).click();
    driver.findElement(By.linkText("Выйти")).click();
    driver.findElement(By.id("input_email")).click();
    driver.findElement(By.id("input_email")).clear();
    driver.findElement(By.id("input_email")).sendKeys("user@webforum.net");
    driver.findElement(By.id("input_password")).click();
    driver.findElement(By.id("input_password")).clear();
    driver.findElement(By.id("input_password")).sendKeys("123");
    driver.findElement(By.id("auth_button")).click();
    driver.findElement(By.id("section_0_link")).click();
    driver.findElement(By.id("bar_search")).click();
    driver.findElement(By.id("bar_search")).clear();
    driver.findElement(By.id("bar_search")).sendKeys("accessible");
    driver.findElement(By.id("bar_search_button")).click();
    assertEquals(driver.findElement(By.xpath("//*/text()[normalize-space(.)='accessible mesage (public discussion)']/parent::*")).getText(), "accessible mesage (public discussion)");
    assertEquals(driver.findElement(By.xpath("//*/text()[normalize-space(.)='accessible message (private discussion)']/parent::*")).getText(), "accessible message (private discussion)");
    driver.findElement(By.id("bar_search")).click();
    driver.findElement(By.id("bar_search")).clear();
    driver.findElement(By.id("bar_search")).sendKeys("inaccessible");
    driver.findElement(By.id("bar_search_button")).click();
    assertEquals(driver.findElement(By.xpath("//*/text()[normalize-space(.)='Нет результатов']/parent::*")).getText(), "Нет результатов");
    driver.findElement(By.id("bar_auth_button")).click();
    driver.findElement(By.linkText("Выйти")).click();
    driver.findElement(By.xpath("//a[@id='bar_main_title']/h3")).click();
    driver.findElement(By.id("section_0_link")).click();
    driver.findElement(By.id("bar_search")).click();
    driver.findElement(By.id("bar_search")).clear();
    driver.findElement(By.id("bar_search")).sendKeys("accessible");
    driver.findElement(By.id("bar_search_button")).click();
    assertEquals(driver.findElement(By.xpath("//*/text()[normalize-space(.)='accessible mesage (public discussion)']/parent::*")).getText(), "accessible mesage (public discussion)");
    assertFalse(isElementPresent(By.xpath("//*/text()[normalize-space(.)='accessible message (private discussion)']/parent::*")));
    driver.findElement(By.id("bar_auth_button")).click();
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

