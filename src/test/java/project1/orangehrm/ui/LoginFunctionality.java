package project1.orangehrm.ui;


import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;
import org.testng.Assert;


import java.time.Duration;

public class LoginFunctionality {

    WebDriver driver;
    WebDriverWait wait;

    @DataProvider(name = "loginTestData")
    public Object[][] loginTestData() {
        return new Object[][] {
            {"Admin", "admin123"},
            {"abcd", "admin123"},
            {"Admin", "admin1234"},
            {"xyz", "1234"},
            {"", ""}
        };
    }

    @BeforeMethod
    public void browserSetup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
    }

    @Test(dataProvider = "loginTestData", priority=1)
    
    public void loginTest(String username, String password) throws InterruptedException {

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[@id=\"app\"]/div[1]/div/div[1]/div/div[2]/div[2]/form/div[1]/div/div[2]/input"))).sendKeys(username);

        driver.findElement(By.xpath("//*[@id=\"app\"]/div[1]/div/div[1]/div/div[2]/div[2]/form/div[2]/div/div[2]/input")).sendKeys(password);

        driver.findElement(By.xpath("//button[@type='submit']")).click();

        
     // Wait for dashboard or error message
        try {
            wait.until(ExpectedConditions.urlContains("dashboard"));
            System.out.println("Login successful: " + driver.getCurrentUrl());
        } catch (Exception e) {
            String errorMsg = driver.findElement(By.xpath("//p[contains(@class,'oxd-alert-content-text')]")).getText();
            System.out.println("Login failed: " + errorMsg);
        }

        // Pause to see result
        Thread.sleep(3000);  
    }
    
  
    @Test(priority=2) 
    public void changePassword() {
    	
        try {
        	wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[@id=\"app\"]/div[1]/div/div[1]/div/div[2]/div[2]/form/div[1]/div/div[2]/input"))).sendKeys("Admin");

            driver.findElement(By.xpath("//*[@id=\"app\"]/div[1]/div/div[1]/div/div[2]/div[2]/form/div[2]/div/div[2]/input")).sendKeys("admin123");

            driver.findElement(By.xpath("//button[@type='submit']")).click();
            //Click on Profile Dropdown
            wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//*[@id=\"app\"]/div[1]/div[1]/header/div[1]/div[3]/ul/li/span")
            )).click();

            wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//*[@id=\"app\"]/div[1]/div[1]/header/div[1]/div[3]/ul/li/ul/li[3]/a")
            )).click();

            
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[@id=\"app\"]/div[1]/div[2]/div[2]/div/div/form/div[1]/div/div[2]/div/div[2]/input")
            )).sendKeys("admin123"); // replace with current password

            
            driver.findElement(By.xpath("//*[@id=\"app\"]/div[1]/div[2]/div[2]/div/div/form/div[2]/div/div[1]/div/div[2]/input"))
                    .sendKeys("NewPass123!"); // replace with new password

            
            driver.findElement(By.xpath("//*[@id=\"app\"]/div[1]/div[2]/div[2]/div/div/form/div[2]/div/div[2]/div/div[2]/input"))
                    .sendKeys("NewPass123!"); // replace with new password again
            
            driver.findElement(By.xpath("//*[@id=\"app\"]/div[1]/div[2]/div[2]/div/div/form/div[3]/button[2]")).click();

            String successMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[@id=\"oxd-toaster_1\"]/div")
            )).getText();
            Thread.sleep(3000);
            System.out.println("Password change message: " + successMsg);

            
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Exception occurred during change password test");
        }
    }


    @AfterMethod
    public void closeBrowser() {
        if (driver != null) {
            driver.quit();
        }
    }
}

