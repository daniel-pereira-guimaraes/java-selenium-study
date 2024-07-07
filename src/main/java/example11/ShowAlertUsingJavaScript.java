package example11;

import org.openqa.selenium.firefox.FirefoxDriver;

import javax.swing.*;

public class ShowAlertUsingJavaScript {

    public static void main(String[] args) {
        var webDriver = new FirefoxDriver();
        try {
            webDriver.executeScript("alert('Hello JavaScript!');");
            JOptionPane.showMessageDialog(null, "Click OK to close all");
        } finally {
            webDriver.quit();
        }
    }
}
