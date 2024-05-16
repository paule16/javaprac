package com.javaprac.webforum;

import org.testng.annotations.*;
import static org.testng.Assert.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import java.time.Duration;

public class CreateSection {
  private WebDriver driver;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();

  @BeforeClass(alwaysRun = true)
  public void setUp() throws Exception {
    System.setProperty("webdriver.chrome.driver", "");
    driver = new FirefoxDriver();
    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
  }

  @Test
  public void testCreateSection() throws Exception {
    driver.get("localhost:8080");
    driver.findElement(By.id("bar_auth_button")).click();
    driver.findElement(By.id("input_email")).clear();
    driver.findElement(By.id("input_email")).sendKeys("boss@webforum.net");
    driver.findElement(By.id("input_password")).clear();
    driver.findElement(By.id("input_password")).sendKeys("qwerty");
    driver.findElement(By.id("auth_button")).click();
    assertEquals(driver.findElement(By.cssSelector("h2")).getText(), "Нет результатов");
    driver.findElement(By.linkText("+ создать раздел")).click();
    driver.findElement(By.id("section_name")).click();
    driver.findElement(By.id("section_name")).clear();
    driver.findElement(By.id("section_name")).sendKeys("Сфера Творчества: Вдохновение и Реализация");
    driver.findElement(By.id("description")).click();
    driver.findElement(By.id("description")).clear();
    driver.findElement(By.id("description")).sendKeys("Добро пожаловать в \"Сферу Творчества\" – уникальное пространство нашего форума, где воплощаются мечты и воплощается вдохновение! Этот раздел призван стать оазисом для всех, кто стремится к творческому самовыражению в различных сферах жизни. Здесь вы найдете обсуждения, советы, идеи и вдохновение для развития своих талантов и искусств.\\n\\nОт искусства и литературы до ремесел и дизайна, здесь каждый найдет что-то по душе. Обменивайтесь опытом, обсуждайте новые тенденции, делись своими проектами или просто наслаждайтесь обществом единомышленников. Независимо от того, является ли ваше творчество хобби или профессией, здесь вы найдете поддержку и вдохновение, чтобы раскрыть свой потенциал полностью.\\n\\nПрисоединяйтесь к обсуждениям о творческом процессе, технике, инструментах и многом другом. Наше сообщество стремится к вдохновению и реализации, и мы приглашаем вас стать частью этого увлекательного путешествия творчества!");
    driver.findElement(By.id("perm_tbl_add_button")).click();
    driver.findElement(By.id("perm_tbl_role_0")).click();
    driver.findElement(By.id("perm_tbl_role_0")).clear();
    driver.findElement(By.id("perm_tbl_role_0")).sendKeys("public");
    driver.findElement(By.id("perm_tbl_perm_0")).click();
    driver.findElement(By.id("perm_tbl_perm_0")).clear();
    driver.findElement(By.id("perm_tbl_perm_0")).sendKeys("READ");
    driver.findElement(By.id("perm_tbl_add_button")).click();
    driver.findElement(By.id("perm_tbl_role_1")).click();
    driver.findElement(By.id("perm_tbl_role_1")).clear();
    driver.findElement(By.id("perm_tbl_role_1")).sendKeys("vip");
    driver.findElement(By.id("perm_tbl_perm_1")).click();
    driver.findElement(By.id("perm_tbl_perm_1")).clear();
    driver.findElement(By.id("perm_tbl_perm_1")).sendKeys("WRITE");
    driver.findElement(By.id("perm_tbl_create_button")).click();
    assertEquals(driver.getTitle(), "Сфера Творчества: Вдохновение и Реализация");
    assertEquals(driver.findElement(By.cssSelector(".text-uppercase")).getText(), "СФЕРА ТВОРЧЕСТВА: ВДОХНОВЕНИЕ И РЕАЛИЗАЦИЯ");
    assertEquals(driver.getTitle(), "Сфера Творчества: Вдохновение и Реализация");
    driver.findElement(By.id("options_tab")).click();
    assertEquals(driver.findElement(By.id("perm_tbl_row_0_role")).getAttribute("value"), "vip");
    assertEquals(driver.findElement(By.id("perm_tbl_row_0_perm")).getAttribute("value"), "WRITE");
    assertEquals(driver.findElement(By.id("perm_tbl_row_1_role")).getAttribute("value"), "public");
    assertEquals(driver.findElement(By.id("perm_tbl_row_1_perm")).getAttribute("value"), "READ");
    driver.findElement(By.id("perm_tbl_row_1_perm")).click();
    driver.findElement(By.id("perm_tbl_row_1_perm")).clear();
    driver.findElement(By.id("perm_tbl_row_1_perm")).sendKeys("WRITE");
    driver.findElement(By.id("options_tab_pane")).click();
    driver.findElement(By.id("perm_tbl_row_0_button")).click();
    driver.findElement(By.id("perm_tbl_accept_button")).click();
    assertEquals(closeAlertAndGetItsText(), "Изменения приняты!");
    driver.findElement(By.cssSelector("h3")).click();
    driver.findElement(By.id("section_0_link")).click();
    assertEquals(driver.getTitle(), "Сфера Творчества: Вдохновение и Реализация");
    driver.findElement(By.id("options_tab")).click();
    assertEquals(driver.findElement(By.id("perm_tbl_row_0_role")).getAttribute("value"), "public");
    assertEquals(driver.findElement(By.id("perm_tbl_row_0_perm")).getAttribute("value"), "WRITE");
    driver.findElement(By.id("delete_discussion_button")).click();
    assertEquals(driver.getTitle(), "Главная страница");
    assertEquals(driver.findElement(By.cssSelector("h2")).getText(), "Нет результатов");
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
