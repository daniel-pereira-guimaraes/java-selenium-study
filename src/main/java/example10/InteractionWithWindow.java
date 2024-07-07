package example10;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.firefox.FirefoxDriver;

import javax.swing.*;

public class InteractionWithWindow {

    public static void main(String[] args) {
        // This example didn't work with Chrome and Edge!
        var webDriver = new FirefoxDriver();
        try {
            webDriver.get("file:///" + System.getProperty("user.dir") + "/src/main/resources/page.html");
            var mainWindowHandler = webDriver.getWindowHandle();

            JOptionPane.showMessageDialog(null, "Click OK to perform the [Show popup] click");
            webDriver.findElement(By.id("showPopup")).click();

            JOptionPane.showMessageDialog(null, "Click OK to perform the [Popup Button] click");
            for (var windowHandler : webDriver.getWindowHandles()) {
                if (!windowHandler.equals(mainWindowHandler)) {
                    webDriver.switchTo().window(windowHandler);
                    try {
                        webDriver.findElement(By.id("popupButton")).click();
                    } catch (NoSuchElementException ignored) {
                    }
                }
            }

            JOptionPane.showMessageDialog(null, "Click OK to perform [Add text] click on main window");
            webDriver.switchTo().window(mainWindowHandler);
            webDriver.findElement(By.id("addText")).click();

            JOptionPane.showMessageDialog(null, "Click OK to close all");
        } finally {
            webDriver.quit();
        }
    }
}
