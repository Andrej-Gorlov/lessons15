package helpers.drivers;

import helpers.ResultOutput;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Objects;

public class RedirectHandler {
    private final WebDriver driver;

    public RedirectHandler(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * Обрабатывает перенаправления на сайте.
     * Если текущий URL содержит "dzen.ru", происходит перенаправление на "https://ya.ru".
     */
    public void handleRedirect() {
        String currentUrl = driver.getCurrentUrl();
        assert currentUrl != null;
        RedirectType redirectType = getRedirectType(currentUrl);

        switch (redirectType) {
            case DZEN:
                ResultOutput.log("Произошло перенаправление на \"https://dzen.ru\". Возвращаемся на Yandex через \"https://ya.ru\".");
                driver.get("https://ya.ru");
                waitForPageLoad();
                break;
            case NONE:
                ResultOutput.log("Перенаправление не обнаружено.");
                break;
            // Можно добавить новые случаи в будущем
            default:
                ResultOutput.log("Неизвестное перенаправление.");
                break;
        }
    }

    /**
     * Получение типа перенаправления на основе текущего URL.
     *
     * @param url текущий URL для проверки.
     * @return тип перенаправления, если найден, иначе NONE.
     */
    private RedirectType getRedirectType(String url) {
        if (url.contains("dzen.ru")) {
            return RedirectType.DZEN;
        }
        return RedirectType.NONE;
    }

    /**
     * Ожидание полной загрузки страницы.
     * Использует WebDriverWait для ожидания, пока состояние документа не станет "complete".
     */
    private void waitForPageLoad() {
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(
                webDriver -> Objects.equals(((JavascriptExecutor) webDriver).executeScript("return document.readyState"), "complete"));
    }

    private enum RedirectType {
        DZEN,
        NONE
    }
}
