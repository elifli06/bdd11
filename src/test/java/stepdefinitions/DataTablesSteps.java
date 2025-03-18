package stepdefinitions;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.tr.Diyelimki;
import io.cucumber.java.tr.Ve;
import io.cucumber.java.tr.Ozaman;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class DataTablesSteps {
    private WebDriver driver;
    private WebDriverWait wait;

    @Before
    public void setup() {
        try {
            WebDriverManager.chromedriver().setup();
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--remote-allow-origins=*");
            options.addArguments("--start-maximized");
            options.addArguments("--disable-popup-blocking");
            options.addArguments("--disable-notifications");
            
            driver = new ChromeDriver(options);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
            wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        } catch (Exception e) {
            System.err.println("Chrome sürücüsü başlatılırken hata oluştu: " + e.getMessage());
            throw e;
        }
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Diyelimki("DataTables Editor sayfasına gittim")
    public void dataTablesEditorSayfasinaGittim() {
        driver.get("https://editor.datatables.net/examples/inline-editing/simple");
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        
        // Çerez popup'ını kontrol et ve kapat
        try {
            WebElement cookieButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button#accept")));
            cookieButton.click();
        } catch (Exception e) {
            System.out.println("Çerez popup'ı bulunamadı veya zaten kapalı.");
        }
        
        // Ana iframe'e geç
        try {
            WebElement mainIframe = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("iframe.demo-frame")));
            driver.switchTo().frame(mainIframe);
        } catch (Exception e) {
            System.out.println("Ana iframe bulunamadı, devam ediliyor.");
        }
    }

    @Ve("yeni bir kayıt oluşturdum")
    public void yeniBirKayitOlusturdum() {
        try {
            // New butonunu JavaScript ile tıkla
            WebElement newButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("button.dt-button.buttons-create")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", newButton);
            
            // Form görünür olana kadar bekle
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.DTE")));
            
            // Form alanlarını doldur
            WebElement firstNameInput = wait.until(ExpectedConditions.elementToBeClickable(By.id("DTE_Field_first_name")));
            firstNameInput.sendKeys("John");
            
            WebElement lastNameInput = wait.until(ExpectedConditions.elementToBeClickable(By.id("DTE_Field_last_name")));
            lastNameInput.sendKeys("Doe");
            
            WebElement positionInput = wait.until(ExpectedConditions.elementToBeClickable(By.id("DTE_Field_position")));
            positionInput.sendKeys("Developer");
            
            WebElement officeInput = wait.until(ExpectedConditions.elementToBeClickable(By.id("DTE_Field_office")));
            officeInput.sendKeys("London");
            
            WebElement extnInput = wait.until(ExpectedConditions.elementToBeClickable(By.id("DTE_Field_extn")));
            extnInput.sendKeys("1234");
            
            WebElement startDateInput = wait.until(ExpectedConditions.elementToBeClickable(By.id("DTE_Field_start_date")));
            startDateInput.sendKeys("2024-03-18");
            
            WebElement salaryInput = wait.until(ExpectedConditions.elementToBeClickable(By.id("DTE_Field_salary")));
            salaryInput.sendKeys("50000");
            
            // Create butonunu JavaScript ile tıkla
            WebElement createButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("button.btn")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", createButton);
            
            // Kayıt işleminin tamamlanmasını bekle
            Thread.sleep(2000);
            
        } catch (Exception e) {
            throw new RuntimeException("Yeni kayıt oluşturulurken hata oluştu: " + e.getMessage());
        }
    }

    @Ve("oluşturduğum kaydı aradım")
    public void olusturdugunKaydiAradim() {
        try {
            // Ana frame'e geri dön
            driver.switchTo().defaultContent();
            try {
                WebElement mainIframe = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("iframe.demo-frame")));
                driver.switchTo().frame(mainIframe);
            } catch (Exception e) {
                System.out.println("Ana iframe bulunamadı, devam ediliyor.");
            }
            
            // Arama kutusunu bul ve JavaScript ile değer gir
            WebElement searchBox = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input[type='search']")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].value = 'John Doe';", searchBox);
            ((JavascriptExecutor) driver).executeScript("arguments[0].dispatchEvent(new Event('input'));", searchBox);
            
            // Aramanın tamamlanmasını bekle
            Thread.sleep(2000);
        } catch (Exception e) {
            throw new RuntimeException("Kayıt aranırken hata oluştu: " + e.getMessage());
        }
    }

    @Ozaman("aradığım kayıt listede görünmeli")
    public void zamanAradigimKayitListedeGorunmeli() {
        try {
            // Beklenen tam isim
            String expectedFullName = "John Doe Developer";
            
            // Tabloda arama sonucunu bekle ve doğrula
            WebElement firstRow = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//table[@id='example']//tbody//tr[1]/td[1]")));
            
            String actualName = firstRow.getText();
            Assertions.assertEquals(expectedFullName, actualName, 
                "Oluşturulan kayıt listede bulunamadı veya beklenen isimle eşleşmiyor!");

            // Diğer alanları da kontrol et
            WebElement position = driver.findElement(
                By.xpath("//table[@id='example']//tbody//tr[1]/td[2]"));
            Assertions.assertEquals("Developer", position.getText(), 
                "Position alanı beklenen değerle eşleşmiyor!");

            WebElement office = driver.findElement(
                By.xpath("//table[@id='example']//tbody//tr[1]/td[3]"));
            Assertions.assertEquals("London", office.getText(), 
                "Office alanı beklenen değerle eşleşmiyor!");

            WebElement extn = driver.findElement(
                By.xpath("//table[@id='example']//tbody//tr[1]/td[4]"));
            Assertions.assertEquals("1234", extn.getText(), 
                "Extension alanı beklenen değerle eşleşmiyor!");
        } catch (Exception e) {
            throw new RuntimeException("Kayıt doğrulanırken hata oluştu: " + e.getMessage());
        }
    }
} 