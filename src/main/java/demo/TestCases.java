package demo;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import io.github.bonigarcia.wdm.WebDriverManager;

public class TestCases {
    ChromeDriver driver;
    WebDriverWait wait;

    public TestCases() {
        System.out.println("Constructor: TestCases");
        WebDriverManager.chromedriver().timeout(30).setup();
        ChromeOptions options = new ChromeOptions();
        LoggingPreferences logs = new LoggingPreferences();
        logs.enable(LogType.BROWSER, Level.ALL);
        logs.enable(LogType.DRIVER, Level.ALL);
        options.setCapability("goog:loggingPrefs", logs);
        System.setProperty(ChromeDriverService.CHROME_DRIVER_LOG_PROPERTY, "chromedriver.log");
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    public void endTest() {
        System.out.println("End Test: TestCases");
        driver.close();
        driver.quit();

    }

    public boolean searchKeyword(String word) {
        try {
            WebElement search = driver.findElement(By.className("search-input"));
            search.clear();
            search.sendKeys(word);
            Thread.sleep(3000);
            return true;
        } catch (Exception e) {
            System.out.println("Error" + e.getMessage());
            return false;
        }
    }

    public String getTitle(WebElement elem) {
        try {
            WebElement videoTitle = elem.findElement(By.className("video-title"));
            Thread.sleep(3000);
            return videoTitle.getText();
        } catch (Exception e) {
            System.out.println("Error" + e.getMessage());
            return "error";
        }
    }

    public List<WebElement> getSearchResults() {
        try {
            List<WebElement> results = driver.findElements(By.xpath("//div[@class='dashboard-grid']/div"));
            Thread.sleep(3000);
            return results;
        } catch (Exception e) {
            System.out.println("Error:" + e.getMessage());
            return null;
        }
    }

    public boolean noResultFound() {
        try {
            Thread.sleep(3000);
            return driver.findElement(By.className("no-search-txt")).isDisplayed();
        } catch (Exception e) {
            System.out.println("Error:" + e.getMessage());
            return false;
        }
    }

    public void testCase01() {
        System.out.println("Start Test case: testCase01");
        driver.get("https://xflix-fe.vercel.app/");
        try {
            Thread.sleep(10000);
        } catch (Exception e) {
        }
        if (driver.findElement(By.id("pillPopupModal")).isDisplayed()) {
            driver.findElement(By.xpath("//div[@id='pillPopupModal']//span")).click();
        }
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("dashboard-grid")));
        System.out.println("End Test case: testCase01");
    }

    public void testCase02() {
        boolean status = false;
        System.out.println("Start Test Case: testcase02");
        status = searchKeyword("frameworks");
        if (!status) {
            System.out.println("Test Case Failed: Not able to search for keyword");
        }
        List<WebElement> results = getSearchResults();
        if (results == null) {
            System.out.println("Test Case Failed: Not able to fetch the search results");
        }
        for (WebElement webElement : results) {
            String title = getTitle(webElement);
            if (!title.toLowerCase().contains("frameworks")) {
                System.out.println("Test Case Failed: Search Result contains invalid keyword");
            }
        }
        status = searchKeyword("random");
        if (!status) {
            System.out.println("Test Case Failed: Not able to search for keyword");
        }
        status = noResultFound();
        if (!status) {
            System.out.println("Invalid keywords return results");
        }
        System.out.println("End Test case: testCase02");
    }

    public void testCase03() {
        System.out.println("Start Test Case: testcase03");
        driver.get("https://xflix-fe.vercel.app/");
        List<WebElement> defaultRes = getSearchResults();
        Select sortDropdown = new Select(driver.findElement(By.id("sortBySelect")));
        sortDropdown.selectByVisibleText("Sort By: View Count");
        try {
            Thread.sleep(3000);
        } catch (Exception e) {
        }
        List<WebElement> sortedRes = getSearchResults();
        if (sortedRes.equals(defaultRes)) {
            System.out.println("Test Case Failed: Sorted results don't match");
        }
        System.out.println("End Test case: testCase03");
    }

    public void testCase04() {
        System.out.println("Start Test Case: testcase04");
        driver.findElement(By.xpath("//button[@class='header-btn btn-upload']")).click();
        try {
            Thread.sleep(3000);
        } catch (Exception e) {
        }
        if (!driver.findElement(By.className("modal-content")).isDisplayed()) {
            System.out.println("Test Case Failed: Upload modal is not visible");
        }
        driver.findElement(By.xpath("//button[normalize-space()='upload video']")).click();
        try {
            Alert alert = driver.switchTo().alert();
            System.out.println("Error Alert is Displayed");
            System.out.println(alert.getText());
            alert.accept();
        } catch (Exception e) {
            System.out.println("Alert is not displayed");
        }
        String iframeCode = "<iframe width=\"560\" height=\"315\" src=\"https://www.youtube.com/embed/hz10meosqeE?si=RoVQ_c1eHGKzSolr\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>";

        driver.findElement(By.xpath("//input[@placeholder='Video Link']")).sendKeys(iframeCode);

        driver.findElement(By.xpath("//input[@placeholder='Thumbnail Image Link']"))
                .sendKeys("https://img.freepik.com/free-photo/colorful-design-with-spiral-design_188544-9588.jpg");

        driver.findElement(By.xpath("//input[@placeholder='Title']")).sendKeys("Demo");
        Select genreDropdown = new Select(driver.findElement(By.id("genre-modal-dropdown")));
        genreDropdown.selectByValue("Comedy");

        Select ageDropdown = new Select(driver.findElement(By.id("age-modal-dropdown")));
        ageDropdown.selectByValue("16+");

        driver.findElement(By.xpath("//input[@name='releaseDate']")).sendKeys("05/10/2024");

        driver.findElement(By.xpath("//button[normalize-space()='upload video']")).click();
        try {
            Alert alert = driver.switchTo().alert();
            System.out.println("Success Alert is Displayed");
            if (alert.getText().equalsIgnoreCase("Video Posted Successfully!")) {
                System.out.println(alert.getText());
                alert.accept();
            } else {
                throw new Error("Alert text does not match");
            }
        } catch (Exception e) {
            System.out.println("Alert is not displayed");
        }
        System.out.println("End Test case: testCase04");
    }

    public void testCase05() {
        System.out.println("Start Test Case: testcase05");
        searchKeyword("reddit");
        List<WebElement> results = getSearchResults();
        results.get(0).click();
        WebElement buttonEle = driver.findElement(By.xpath("//button[@class='btn btn-like']"));
        System.out.println(buttonEle.getText());
        buttonEle.click();
        try {
            Thread.sleep(2000);
        } catch (Exception e) {
        }
        String currURL = driver.getCurrentUrl();
        String parentWindow = driver.getWindowHandle();
        driver.switchTo().newWindow(WindowType.TAB);
        ArrayList<String> windowHandlesList = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(windowHandlesList.get(1));
        driver.get(currURL);
        buttonEle = driver.findElement(By.xpath("//button[@class='btn btn-like']"));
        System.out.println(buttonEle.getText());
        driver.close();
        driver.switchTo().window(parentWindow);
        System.out.println("End Test case: testCase05");
    }
}
