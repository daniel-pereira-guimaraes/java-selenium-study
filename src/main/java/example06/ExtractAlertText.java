package example06;

import org.openqa.selenium.By;
import org.openqa.selenium.edge.EdgeDriver;

public class ExtractAlertText {

    public static void main(String[] args) {
        var webDriver = new EdgeDriver();
        try {
            webDriver.get("file:///" + System.getProperty("user.dir") + "/src/main/resources/page.html");
            webDriver.findElement(By.id("showAlert")).click();
            var alert = webDriver.switchTo().alert();
            var message = alert.getText();
            System.out.println("Alert text: " + message);
        } finally {
            webDriver.quit();
        }
    }
}
