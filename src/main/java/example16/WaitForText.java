package example16;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.v85.network.Network;
import org.openqa.selenium.devtools.v85.network.model.ConnectionType;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.swing.*;
import java.time.Duration;
import java.util.Optional;

public class WaitForText {
    private static final String MY_TEXT = "My text";

    private static ChromeDriver webDriver;

    public static void main(String[] args) throws InterruptedException {
        webDriver = new ChromeDriver();
        try {
            simulateSlowNetwork();
            loadPage();
            var input = webDriver.findElement(By.cssSelector("input[data-p-label='Name']"));
            inputText(input);
            submitClick(input);
            waitForResultText(input);
            System.out.println("Done!");
            JOptionPane.showMessageDialog(null, "Done! Click OK to close all");
        } finally {
            webDriver.quit();
        }
    }

    private static void loadPage() {
        System.out.println("Loading page...");
        webDriver.get("https://www.primefaces.org/showcase/ui/ajax/basic.xhtml");
    }

    private static void simulateSlowNetwork() {
        var devTools = webDriver.getDevTools();
        devTools.createSession();
        devTools.send(Network.emulateNetworkConditions(false, 1000, 20000, 10000,
                Optional.of(ConnectionType.CELLULAR2G)));
    }

    private static void inputText(WebElement input) {
        System.out.println("Changing input text...");
        input.sendKeys(MY_TEXT);
    }

    private static void submitClick(WebElement input) {
        System.out.println("Clicking submit button...");
        var form = input.findElement(By.xpath("./ancestor::form"));
        var submit = form.findElement(By.cssSelector("button[type='submit'], input[type='submit']"));
        submit.click();
    }

    private static void waitForResultText(WebElement input) {
        System.out.println("Waiting for result text...");
        var inputTd = input.findElement(By.xpath("./ancestor::td"));
        var nextTd = inputTd.findElement(By.xpath("following-sibling::td[1]"));
        var wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.textToBePresentInElement(nextTd, MY_TEXT));
    }

}
