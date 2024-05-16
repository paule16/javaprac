package com.javaprac.webforum;

import org.testng.annotations.*;
import static org.testng.Assert.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import java.time.Duration;

public class CreateMessage {
  private WebDriver driver;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();

  @BeforeClass(alwaysRun = true)
  public void setUp() throws Exception {
    System.setProperty("webdriver.chrome.driver", "");
    driver = new FirefoxDriver();
    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
  }

  @Test
  public void testCreateMessage() throws Exception {
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
    driver.findElement(By.id("section_name")).sendKeys("test");
    driver.findElement(By.id("create_button")).click();
    assertTrue(isElementPresent(By.id("new_msg_input")));
    driver.findElement(By.id("new_msg_input")).clear();
    driver.findElement(By.id("new_msg_input")).sendKeys("first message");
    driver.findElement(By.id("msg_create_button")).click();
    assertTrue(isElementPresent(By.xpath("//p[contains(.,'first message')]")));
    assertTrue(isElementPresent(By.xpath("(//a[contains(text(),'BOSS')])[3]")));
    driver.findElement(By.id("new_msg_input")).click();
    driver.findElement(By.id("new_msg_input")).clear();
    driver.findElement(By.id("new_msg_input")).sendKeys("second message");
    driver.findElement(By.id("msg_create_button")).click();
    assertTrue(isElementPresent(By.xpath("//p[contains(.,'second message')]")));
    driver.findElement(By.xpath("//div[1]/div/button[2]")).click();
    driver.findElement(By.xpath("//div[2]/div/button[3]")).click();
    driver.findElement(By.xpath("//div[1]/div/button[3]/span")).click();
    driver.findElement(By.xpath("//div[2]/div/button[2]/span")).click();
    driver.findElement(By.xpath("//div[1]/div/button[3]")).click();
    driver.findElement(By.xpath("//div[2]/div/button[2]/b")).click();
    assertEquals(driver.findElement(By.xpath("//div[1]/div/button[2]/span")).getText(), "1");
    assertEquals(driver.findElement(By.xpath("//div[1]/div/button[3]/span")).getText(), "0");
    assertEquals(driver.findElement(By.xpath("//div[2]/div/button[2]")).getText(), "0 +");
    assertEquals(driver.findElement(By.xpath("//div[2]/div/button[3]/span")).getText(), "1");
    driver.findElement(By.xpath("//div[3]/div/div/button")).click();
    assertEquals(driver.findElement(By.id("quote_text")).getText(), "second message");
    driver.findElement(By.xpath("//div[2]/div/button")).click();
    assertEquals(driver.findElement(By.id("quote_text")).getText(), "first message");
    driver.findElement(By.xpath("//div[3]/div/div/button")).click();
    assertEquals(driver.findElement(By.id("quote_text")).getText(), "second message");
    driver.findElement(By.xpath("//div[3]/div/div/button")).click();
    assertFalse(driver.findElement(By.id("quote_text")).isDisplayed());
    driver.findElement(By.xpath("//div[3]/div/div/button")).click();
    driver.findElement(By.id("new_msg_input")).click();
    driver.findElement(By.id("new_msg_input")).clear();
    driver.findElement(By.id("new_msg_input")).sendKeys("msg with quote");
    driver.findElement(By.id("msg_create_button")).click();
    assertTrue(isElementPresent(By.xpath("//p[contains(.,'msg with quote')]")));
    assertTrue(isElementPresent(By.xpath("//div[2]/a")));
    assertEquals(driver.findElement(By.xpath("//a/span")).getText(), "BOSS:");
    assertEquals(driver.findElement(By.xpath("//span[2]")).getText(), "second message");
    driver.findElement(By.cssSelector(".text-white-50")).click();
    driver.findElement(By.id("discussion_0")).click();
    assertTrue(isElementPresent(By.xpath("//p[contains(.,'msg with quote')]")));
    assertTrue(isElementPresent(By.xpath("//p[contains(.,'second message')]")));
    assertTrue(isElementPresent(By.xpath("//p[contains(.,'first message')]")));
    assertEquals(driver.findElement(By.xpath("//div[3]/div/button[3]")).getText(), "1 -");
    assertEquals(driver.findElement(By.xpath("//div[3]/div/button[2]")).getText(), "0 +");
    assertEquals(driver.findElement(By.xpath("//div[2]/div/button[3]")).getText(), "0 -");
    assertTrue(isElementPresent(By.xpath("//div[2]/a")));
    assertEquals(driver.findElement(By.xpath("//div[2]/div/button[2]")).getText(), "1 +");
    assertTrue(isElementPresent(By.xpath("//p[contains(.,'second message')]")));
    driver.findElement(By.cssSelector(".mx-4:nth-child(2) > .hstack > div > .p-2")).click();
    assertEquals(closeAlertAndGetItsText(), "Невозможно удалить: на сообщение имеются ссылки.");
    assertTrue(isElementPresent(By.xpath("//p[contains(.,'second message')]")));
    driver.findElement(By.xpath("//div[4]/a")).click();
    driver.findElement(By.xpath("//div[4]/a")).click();
    assertFalse(isElementPresent(By.xpath("//p[contains(.,'second message')]")));
    driver.findElement(By.cssSelector(".text-white-50")).click();
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
