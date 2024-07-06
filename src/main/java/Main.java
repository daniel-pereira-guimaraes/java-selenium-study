import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class Main {
    private static final WebDriver webDriver = new FirefoxDriver();

    public static void main(String[] args) {
        webDriver.get("https://www.google.com/");
        showTitle();
    }

    private static void showTitle() {
        System.out.println("title: " + webDriver.getTitle());
    }
}
