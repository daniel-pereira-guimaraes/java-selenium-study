package example09;

import org.openqa.selenium.By;
import org.openqa.selenium.edge.EdgeDriver;

import javax.swing.*;

public class InteractionWithFrame {

    public static void main(String[] args) {
        var webDriver = new EdgeDriver();
        try {
            webDriver.get("file:///" + System.getProperty("user.dir") + "/src/main/resources/page.html");

            JOptionPane.showMessageDialog(null, "Click OK to perform the [Frame button] click");
            webDriver.switchTo().frame("frame1");
            webDriver.findElement(By.id("frameButton")).click();

            JOptionPane.showMessageDialog(null, "Click OK to perform the [Add text] click");
            webDriver.switchTo().defaultContent();
            webDriver.findElement(By.id("addText")).click();

            JOptionPane.showMessageDialog(null, "Click OK to close all");
        } finally {
            webDriver.quit();
        }
    }
}
