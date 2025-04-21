package helpers.drivers;

import helpers.ResultOutput;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;
import java.util.Objects;

public class WebDriverManager {
    private WebDriver driver;
    private final RedirectHandler redirectHandler;

    public WebDriverManager(){
        driver = initializeDriver();
        redirectHandler = new RedirectHandler(driver);
    }

    private WebDriver initializeDriver() {
        System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver/chromedriver.exe");
        driver = new ChromeDriver();
        return driver;
    }

    /**
     * Загрузка страницы и обрабатка возможного перенаправления.
     *
     * @param url URL страницы для загрузки.
     */
    public void loadPage(String url) {
        driver.get(url);
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(
                webDriver -> Objects.equals(((JavascriptExecutor) webDriver).executeScript("return document.readyState"), "complete"));

        redirectHandler.handleRedirect();
    }

    /**
     * Ожидание, пока указанный элемент не станет видимым.
     *
     * @param locator локатор элемента, который нужно ожидать.
     * @param timeout время ожидания в секундах.
     */
    public void waitForElementVisible(By locator, int timeout) {
        closePopup();
        new WebDriverWait(driver, Duration.ofSeconds(timeout)).until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public void performSearch(String searchText) {
        ResultOutput.log("Вводим в строку поиска '" + searchText + "'.");
        driver.findElement(By.xpath("//input[@name='text']")).sendKeys(searchText);

        ResultOutput.log("Выполнение поискового запроса.");
        driver.findElement(By.xpath("//button[contains(text(), 'Найти')]")).click();
    }

    public void checkSearchResult(String searchText) {
        // Обновление строки поиска
        waitForElementVisible(By.xpath("//input[@aria-label='Запрос']"), 10);

        String searchValue = driver.findElement(By.xpath("//input[@name='text']")).getAttribute("value");

        if (!searchText.equals(searchValue)) {
            ResultOutput.log("Поисковое значение в строке запроса было преобразовано в '" + searchValue + "'.");
        }

        Assert.assertEquals(searchValue, "hello world", "Строка поиска не содержит ожидаемое значение.");

        String pageTitle = driver.getTitle();
        Assert.assertNotNull(pageTitle, "Название окна не должно быть пустым.");
        Assert.assertTrue(pageTitle.contains("hello world"), "Название окна не содержит ожидаемое значение.");
    }

    /**
     * Закрытие сплывающего окна, если оно есть.
     */
    private void closePopup() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[contains(text(), 'Закрыть')]"))).click();
            ResultOutput.log("Произошло закрытие сплывающего окна.");
        } catch (Exception e) {
            // Игнорируем, если окно не появилось
        }
    }

    public void quitDriver() {
        if (driver != null) {
            driver.quit();
        }
    }
}
