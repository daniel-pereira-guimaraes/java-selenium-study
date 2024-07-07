package example14;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import javax.swing.*;
import java.util.List;

public class ExtractTable {

    public static void main(String[] args) {
        var webDriver = new FirefoxDriver();
        try {
            webDriver.get("file:///" + System.getProperty("user.dir") + "/src/main/resources/page.html");

            var table = webDriver.findElement(By.xpath("//table"));
            extractTableHead(table);
            extractTableBody(table);

            JOptionPane.showMessageDialog(null, "Click OK to close all");
        } finally {
            webDriver.quit();
        }
    }

    private static void extractTableHead(WebElement table) {
        var head = table.findElement(By.tagName("thead"));
        var headRows = head.findElements(By.tagName("tr"));
        headRows.forEach(row -> printLine(row.findElements(By.tagName("th"))));
    }

    private static void extractTableBody(WebElement table) {
        var body = table.findElement(By.tagName("tbody"));
        var rows = body.findElements(By.tagName("tr"));
        rows.forEach(row -> printLine(row.findElements(By.tagName("td"))));
    }

    private static void printLine(List<WebElement> elements) {
        for (int i = 0; i < elements.size(); i++) {
            if (i > 0) {
                System.out.print(';');
            }
            System.out.print(elements.get(i).getText());
        }
        System.out.println();
    }

}
