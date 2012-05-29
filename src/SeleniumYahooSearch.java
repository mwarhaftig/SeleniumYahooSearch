
import java.io.File;
import java.io.IOException;
import org.openqa.selenium.By;
import org.openqa.selenium.HasInputDevices;
import org.openqa.selenium.Keyboard;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

public class SeleniumYahooSearch  {
    
    public static void main(String[] args) {
        // Create a new instance of the Chrome driver.
        ChromeDriverService service = new ChromeDriverService.Builder()
        .usingChromeDriverExecutable(new File("./chromedriver"))
        .usingAnyFreePort()
        .build();
        try {
            service.start();
            System.out.println("Started Service");
        } catch (IOException e) {
            e.printStackTrace();
        }
        WebDriver driver = new RemoteWebDriver(service.getUrl(),
                DesiredCapabilities.chrome());
        
        //Navigate to yahoo.
        driver.get("http://www.yahoo.com");
        
        // Find the search box, search and then clear results.
        WebElement element = driver.findElement(By.cssSelector("[id=\"p_13838465-p\"]"));
        element.sendKeys("Benchmark Solutions");
        element.clear();
        try {
           synchronized(service){
               service.wait(1000);
           }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }         
       
        // Key down search results and hit the link. 
        element.sendKeys(""+Keys.ARROW_DOWN+Keys.ENTER);
       
        // Wait for page to load.
        (new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.getTitle().toLowerCase().startsWith("benchmark solutions inc");
            }
        });

        element = driver.findElement(By.cssSelector("body"));
        element.sendKeys(""+Keys.ARROW_DOWN+Keys.ARROW_DOWN+Keys.ARROW_DOWN+Keys.ARROW_DOWN+Keys.ARROW_DOWN+Keys.ARROW_DOWN+Keys.ARROW_DOWN);
       
        // Click page to clear the Javascript alert popup.
        element.click();
       
        // Find the 7th search result and shift click it.
        element = driver.findElement(By.cssSelector("[id=\"link-7\"]:link"));
       
        Keyboard keyboard = ((HasInputDevices) driver).getKeyboard();
        keyboard.pressKey(Keys.SHIFT);
        element.click();
        keyboard.releaseKey(Keys.SHIFT);
       
        // Switch to newly opened window.
        driver.switchTo().window((String) driver.getWindowHandles().toArray()[1]);
        element = driver.findElement(By.cssSelector("body"));
        element.sendKeys(Keys.TAB); 
        driver.navigate().back();
        
        // Sleep just to let me check out the page.
        try {
            synchronized(service){
                service.wait(5000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }        
        
        //Close the browser and stop the service.
        driver.quit();
        service.stop();
        System.out.println("Done!");
    }
}