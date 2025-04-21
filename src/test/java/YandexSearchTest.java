import helpers.ResultOutput;
import helpers.drivers.WebDriverManager;
import org.openqa.selenium.By;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class YandexSearchTest {
    private WebDriverManager webDriverManager;

    @BeforeClass
    public void setUp() {
        webDriverManager = new WebDriverManager();
    }

    @DataProvider(name = "searchData")
    public Object[][] searchData() {
        return new Object[][] {
                {"руддщ цкщдв"},
                {"hello world"},
        };
    }

    @Test(dataProvider = "searchData")
    public void testSearch(String searchText) {
        String nameMethod = "testSearch";
        ResultOutput.printTestStart(nameMethod);

        webDriverManager.loadPage("https://yandex.ru");
        webDriverManager.waitForElementVisible(By.xpath("//input[@name='text']"), 10);
        webDriverManager.performSearch(searchText);
        webDriverManager.checkSearchResult(searchText);

        ResultOutput.printTestEnd(nameMethod);
    }


    @AfterClass
    public void tearDown() {
        webDriverManager.quitDriver();
    }
}
