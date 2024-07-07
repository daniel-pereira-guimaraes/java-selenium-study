package example12;

import org.openqa.selenium.firefox.FirefoxDriver;

import javax.swing.*;

public class SetInputValueUsingJavaScript {

    public static void main(String[] args) {
        var webDriver = new FirefoxDriver();
        try {
            webDriver.get("file:///" + System.getProperty("user.dir") + "/src/main/resources/page.html");
            webDriver.executeScript("""
                    document.getElementById('email').value = 'hello@server.com';
            """);
            JOptionPane.showMessageDialog(null, "Click OK to close all");
        } finally {
            webDriver.quit();
        }
    }
}
