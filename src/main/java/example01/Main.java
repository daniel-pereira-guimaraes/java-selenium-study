package example01;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class Main {
    private static final WebDriver webDriver = new FirefoxDriver();

    public static void main(String[] args) {
        webDriver.manage().window().setPosition(new Point(0,0));
        webDriver.manage().window().setSize(new Dimension(800,600));
        webDriver.get("https://www.google.com/");
        System.out.println("Page title: " + webDriver.getTitle());
        webDriver.quit();
    }
}
