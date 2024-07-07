package example07;

import org.openqa.selenium.By;
import org.openqa.selenium.edge.EdgeDriver;

import javax.swing.*;

public class ConfirmAndCancelAlert {

    public static void main(String[] args) {
        var webDriver = new EdgeDriver();
        try {
            webDriver.get("file:///" + System.getProperty("user.dir") + "/src/main/resources/page.html");
            var showAlertButton = webDriver.findElement(By.id("showAlert"));

            showAlertButton.click();
            var alert = webDriver.switchTo().alert();
            JOptionPane.showMessageDialog(null, "Click OK to CONFIRM alert");
            alert.accept();

            showAlertButton.click();
            alert = webDriver.switchTo().alert();
            JOptionPane.showMessageDialog(null, "Click OK to CANCEL alert");
            alert.dismiss();

            JOptionPane.showMessageDialog(null, "Click OK to close all");
        } finally {
            webDriver.quit();
        }
    }

}
