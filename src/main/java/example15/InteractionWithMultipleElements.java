package example15;

import org.openqa.selenium.By;
import org.openqa.selenium.firefox.FirefoxDriver;

import javax.swing.*;

public class InteractionWithMultipleElements {

    public static void main(String[] args) {
        var webDriver = new FirefoxDriver();
        try {
            webDriver.get("file:///" + System.getProperty("user.dir") + "/src/main/resources/page.html");

            JOptionPane.showMessageDialog(null, "Click OK to change the text for all buttons");
            var buttons = webDriver.findElements(By.tagName("button"));
            for (var button : buttons) {
                webDriver.executeScript("arguments[0].innerText = 'Changed';", button);
            }

            JOptionPane.showMessageDialog(null, "Click OK to close all");
        } finally {
            webDriver.quit();
        }

    }
}
