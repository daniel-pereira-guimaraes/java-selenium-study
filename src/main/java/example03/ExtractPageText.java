package example03;

import org.openqa.selenium.By;
import org.openqa.selenium.edge.EdgeDriver;

import javax.swing.*;

public class ExtractPageText {

    public static void main(String[] args) {
        var webDriver = new EdgeDriver();
        try {
            webDriver.get("https://en.wikipedia.org/wiki/Java_(programming_language)");
            var text = webDriver.findElement(By.tagName ("body")).getText();
            showText(text);
        } finally {
            webDriver.quit();
        }
    }

    private static void showText(String text) {
        SwingUtilities.invokeLater(() -> {
            var frame = new JFrame("Page text");
            var textArea = new JTextArea(text);
            var scrollPane = new JScrollPane(textArea);

            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);

            frame.add(scrollPane);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}
