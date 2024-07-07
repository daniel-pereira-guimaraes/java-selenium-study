package example08;

import org.openqa.selenium.By;
import org.openqa.selenium.edge.EdgeDriver;

import javax.swing.*;

public class ConfirmPrompt {

    public static void main(String[] args) {
        var webDriver = new EdgeDriver();
        try {
            webDriver.get("file:///" + System.getProperty("user.dir") + "/src/main/resources/page.html");
            var showPrompt = webDriver.findElement(By.id("showPrompt"));
            showPrompt.click();
            var alert = webDriver.switchTo().alert();

            JOptionPane.showMessageDialog(null, "Click OK to send keys and confirm prompt");
            alert.sendKeys("I love Java!");
            alert.accept();

            JOptionPane.showMessageDialog(null, "Click OK to close all");
        } finally {
            webDriver.quit();
        }
    }

}
