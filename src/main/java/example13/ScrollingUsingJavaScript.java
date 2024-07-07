package example13;

import org.openqa.selenium.By;
import org.openqa.selenium.firefox.FirefoxDriver;

import javax.swing.*;

public class ScrollingUsingJavaScript {

    public static void main(String[] args) {
        var webDriver = new FirefoxDriver();
        try {
            webDriver.get("file:///" + System.getProperty("user.dir") + "/src/main/resources/page.html");

            JOptionPane.showMessageDialog(null, "Click OK to scroll to [Bottom Button]");
            var button = webDriver.findElement(By.id("bottomButton"));
            webDriver.executeScript("window.scrollBy(0, arguments[0])", button.getLocation().y);

            JOptionPane.showMessageDialog(null, "Click OK to scroll to perform [Bottom Button] click");
            button.click();

            JOptionPane.showMessageDialog(null, "Click OK to close all");
        } finally {
            webDriver.quit();
        }

    }
}
