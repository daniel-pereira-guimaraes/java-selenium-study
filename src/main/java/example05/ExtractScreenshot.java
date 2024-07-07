package example05;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.edge.EdgeDriver;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.util.Optional;

public class ExtractScreenshot {

    private static final String URL = "https://tecnobyte.com.br/";
    private static final String SAVE_DIALOG_TITLE = "Save screenshot";
    private static final String PNG_FILTER = "PNG images";
    private static final String PNG_EXTENSION = "png";
    private static final String PNG_SUFFIX = "." + PNG_EXTENSION;
    private static final String DEFAULT_FILE_NAME = "screenshot" + PNG_SUFFIX;
    private static final String SAVED_SUCCESSFULLY_MESSAGE = "Screenshot saved successfully!";

    public static void main(String[] args) throws IOException {
        byte[] screenshotBytes;
        var webDriver = new EdgeDriver();
        try {
            webDriver.get(URL);
            screenshotBytes = webDriver.getScreenshotAs(OutputType.BYTES);
        } finally {
            webDriver.quit();
        }
        if (trySaveScreenshot(screenshotBytes)) {
            JOptionPane.showMessageDialog(null, SAVED_SUCCESSFULLY_MESSAGE);
        }
    }

    private static boolean trySaveScreenshot(byte[] screenshotBytes) throws IOException {
        var filePath = getSaveFilePath();
        if (filePath.isEmpty()) {
            return false;
        }
        saveScreenshot(screenshotBytes, filePath.get());
        return true;
    }

    private static void saveScreenshot(byte[] screenshotBytes, String filePath) throws IOException {
        try (var inputStream = new ByteArrayInputStream(screenshotBytes);
             var outputStream = new FileOutputStream(filePath)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) >= 0) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }
    }

    private static Optional<String> getSaveFilePath() {
        var fileChooser = new JFileChooser();
        fileChooser.setDialogTitle(SAVE_DIALOG_TITLE);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setFileFilter(new FileNameExtensionFilter(PNG_FILTER, PNG_EXTENSION));
        fileChooser.setSelectedFile(new File(DEFAULT_FILE_NAME));
        int userSelection = fileChooser.showSaveDialog(null);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            var filePath = fileChooser.getSelectedFile().getAbsolutePath();
            if (!filePath.endsWith(PNG_SUFFIX)) {
                filePath += PNG_SUFFIX;
            }
            return Optional.of(filePath);
        }
        return Optional.empty();
    }
}
