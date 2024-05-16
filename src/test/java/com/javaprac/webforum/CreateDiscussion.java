package com.javaprac.webforum;

import org.testng.annotations.*;
import static org.testng.Assert.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import java.time.Duration;

public class CreateDiscussion {
  private WebDriver driver;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();

  @BeforeClass(alwaysRun = true)
  public void setUp() throws Exception {
    System.setProperty("webdriver.chrome.driver", "");
    driver = new FirefoxDriver();
    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
  }

  @Test
  public void testCreateDiscussion() throws Exception {
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
    driver.findElement(By.id("section_name")).sendKeys("AI generated discussions");
    driver.findElement(By.id("perm_tbl_create_button")).click();
    assertEquals(driver.getTitle(), "AI generated discussions");
    driver.findElement(By.id("create_discussion_button")).click();
    driver.findElement(By.id("section_name")).click();
    driver.findElement(By.id("section_name")).clear();
    driver.findElement(By.id("section_name")).sendKeys("Если бы домашние животные могли говорить");
    driver.findElement(By.id("description")).click();
    driver.findElement(By.id("description")).clear();
    driver.findElement(By.id("description")).sendKeys("Представьте, что ваш питомец может заговорить. Какие забавные истории он рассказал бы? Обсудите веселые анекдоты или серьезные \"разговоры\" с вашими персональными зверятами.");
    driver.findElement(By.id("create_button")).click();
    assertEquals(driver.getTitle(), "Если бы домашние животные могли говорить");
    assertEquals(driver.findElement(By.cssSelector(".border-bottom")).getText(), "Если бы домашние животные могли говорить");
    assertTrue(driver.findElement(By.cssSelector(".m-3")).getText().matches("^Представьте, что ваш питомец может заговорить\\. Какие забавные истории он рассказал бы[\\s\\S] Обсудите веселые анекдоты или серьезные \"разговоры\" с вашими персональными зверятами\\.$"));
    assertEquals(driver.findElement(By.cssSelector("h2")).getText(), "Нет сообщений");
    assertEquals(driver.findElement(By.cssSelector(".hstack > .p-2:nth-child(1)")).getText(), "BOSS");
    assertEquals(driver.findElement(By.id("bar_section")).getText(), "AI generated discussions");
    driver.findElement(By.id("options_button")).click();
    assertEquals(driver.findElement(By.cssSelector("h4")).getText(), "Правила доступа");
    driver.findElement(By.id("perm_tbl_add_button")).click();
    driver.findElement(By.id("perm_tbl_button0")).click();
    driver.findElement(By.id("perm_tbl_button0")).clear();
    driver.findElement(By.id("perm_tbl_button0")).sendKeys("role1");
    driver.findElement(By.cssSelector("td:nth-child(2)")).click();
    driver.findElement(By.name("perms")).click();
    driver.findElement(By.name("perms")).clear();
    driver.findElement(By.name("perms")).sendKeys("WRITE");
    driver.findElement(By.cssSelector(".btn-success")).click();
    driver.findElement(By.id("perm_tbl_add_button")).click();
    driver.findElement(By.id("perm_tbl_button1")).click();
    driver.findElement(By.id("perm_tbl_button1")).clear();
    driver.findElement(By.id("perm_tbl_button1")).sendKeys("role2");
    driver.findElement(By.cssSelector("#new_1 > td:nth-child(2) > .form-control")).clear();
    driver.findElement(By.cssSelector("#new_1 > td:nth-child(2) > .form-control")).sendKeys("EDIT");
    driver.findElement(By.cssSelector(".btn-success")).click();
    driver.findElement(By.cssSelector("#new_0 .btn")).click();
    driver.findElement(By.id("perm_tbl_accept_button")).click();
    assertEquals(closeAlertAndGetItsText(), "Изменения приняты!");
    assertEquals(driver.findElement(By.id("options_button")).getText(), "параметры ^");
    driver.findElement(By.id("options_button")).click();
    assertEquals(driver.findElement(By.id("options_button")).getText(), "параметры v");
    driver.findElement(By.cssSelector(".text-white-50")).click();
    assertEquals(driver.getTitle(), "AI generated discussions");
    driver.findElement(By.id("discussion_0")).click();
    assertEquals(driver.getTitle(), "Если бы домашние животные могли говорить");
    driver.findElement(By.id("options_button")).click();
    assertEquals(driver.findElement(By.id("perm_tbl_row_0_role")).getAttribute("value"), "role2");
    assertEquals(driver.findElement(By.id("perm_tbl_row_0_perm")).getAttribute("value"), "EDIT");
    driver.findElement(By.id("delete_discussion_button")).click();
    assertEquals(driver.getTitle(), "AI generated discussions");
    assertEquals(driver.findElement(By.cssSelector("h2")).getText(), "Нет обсуждений");
    driver.findElement(By.id("delete_discussion_button")).click();
  }

  @AfterClass(alwaysRun = true)
  public void tearDown() throws Exception {
    driver.quit();
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
      fail(verificationErrorString);
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
