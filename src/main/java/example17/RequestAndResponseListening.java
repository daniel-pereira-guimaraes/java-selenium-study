package example17;

import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v124.network.Network;
import org.openqa.selenium.devtools.v124.network.model.Headers;
import org.openqa.selenium.devtools.v124.network.model.RequestWillBeSent;
import org.openqa.selenium.devtools.v124.network.model.ResponseReceived;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class RequestAndResponseListening {
    private static final Map<String, Double> REQUEST_SENT_TIMES = new HashMap<>();
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
    private static final String OUTPUT_DIR = "c:\\tmp\\";
    private static final int MAX_FILE_NAME_LENGTH = 200;

    public static void main(String[] args) {
        var options = new ChromeOptions();
        var driver = new ChromeDriver(options);
        try {
            var devTools = driver.getDevTools();

            devTools.createSession();
            devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
            devTools.addListener(Network.requestWillBeSent(), RequestAndResponseListening::onRequest);
            devTools.addListener(Network.responseReceived(), responseReceived ->
                    onResponse(responseReceived, devTools)
            );

            driver.get("https://www.tecnobyte.com.br");
        } finally {
            driver.quit();
        }
    }

    private static void onRequest(RequestWillBeSent requestWillBeSent) {
        var request = requestWillBeSent.getRequest();
        var sb = new StringBuilder("\n*** REQUEST ***")
                .append("\nRequestId: ").append(requestWillBeSent.getRequestId())
                .append("\nURL: ").append(request.getUrl())
                .append("\nMethod: ").append(request.getMethod());
        appendHeaders(sb, request.getHeaders());
        REQUEST_SENT_TIMES.put(requestWillBeSent.getRequestId().toString(),
                requestWillBeSent.getTimestamp().toJson().doubleValue());
        System.out.println(sb);
    }

    private static void onResponse(ResponseReceived responseReceived, DevTools devTools) {
        var response = responseReceived.getResponse();
        var sb = new StringBuilder("\n*** RESPONSE ***")
                .append("\nRequestId: ").append(responseReceived.getRequestId())
                .append("\nURL: ").append(response.getUrl())
                .append("\nStatus: ").append(response.getStatus())
                .append("\nType: ").append(responseReceived.getType())
                .append("\nCharset: ").append(response.getCharset())
                .append("\nMimeType: ").append(response.getMimeType());
        tryAppendResponseTime(sb, responseReceived);
        tryProcessResponseBody(sb, responseReceived, devTools);
        appendHeaders(sb, response.getHeaders());
        System.out.println(sb);
    }

    private static void tryProcessResponseBody(
            StringBuilder sb, ResponseReceived responseReceived, DevTools devTools) {
        var bodyCommand = Network.getResponseBody(responseReceived.getRequestId());
        try {
            var responseBody = devTools.send(bodyCommand);
            var body = responseBody.getBody();
            if (body != null && !body.isEmpty()) {
                if (responseBody.getBase64Encoded()) {
                    processBody(sb, responseReceived, Base64.getDecoder().decode(body));
                } else {
                    processBody(sb, responseReceived, body.getBytes());
                }
            }
        } catch (RuntimeException e) {
            System.err.println("Process body error" +
                    "\nURL: " + responseReceived.getResponse().getUrl() +
                    "\nMessage: " + e.getMessage());
        }
    }

    private static void processBody(StringBuilder sb, ResponseReceived responseReceived, byte[] body) {
        sb.append("\nBody length: ").append(body.length);
        var response = responseReceived.getResponse();
        var url = response.getUrl();
        var mimeType = response.getMimeType();
        var path = Path.of(OUTPUT_DIR + buildFileName(url, mimeType));
        try {
            Files.write(path, body);
            sb.append("\nBody saved: ").append(path);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static void tryAppendResponseTime(StringBuilder sb, ResponseReceived responseReceived) {
        var startTime = REQUEST_SENT_TIMES.get(responseReceived.getRequestId().toString());
        if (startTime != null) {
            var endTime = responseReceived.getTimestamp().toJson().doubleValue();
            sb.append("\nTime: ")
                    .append(endTime - startTime)
                    .append(" seconds");
        }
    }

    private static void appendHeaders(StringBuilder sb, Headers headers) {
        sb.append("\nHeaders:");
        headers.forEach((name, value) ->
                sb.append("\n\t")
                        .append(name)
                        .append(" = ")
                        .append(value)
        );
    }

    private static String buildFileName(String url, String mimeType) {
        var fileName = LocalDateTime.now().format(DATE_TIME_FORMATTER) + "_" +
                url.replaceAll("[~#%&*{}:<>?/+|\"-]", "_");
        var extension = mimeTypeToExtension(mimeType);
        if (!fileName.endsWith(extension)) {
            fileName += extension;
        }
        return truncateFileName(fileName);
    }

    public static String truncateFileName(String fileName) {
        if (fileName.length() <= MAX_FILE_NAME_LENGTH) {
            return fileName;
        }
        var baseName = getBaseName(fileName);
        var extension = getExtension(fileName);
        var removeLength = fileName.length() - MAX_FILE_NAME_LENGTH;
        var maxBaseNameLength = baseName.length() - removeLength;
        return baseName.substring(0, maxBaseNameLength) + '.' + extension;
    }

    public static String getBaseName(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        return lastDotIndex >= 0 ? fileName.substring(0, lastDotIndex) : fileName;
    }

    public static String getExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        return lastDotIndex >= 0 ? fileName.substring(lastDotIndex + 1) : "";
    }

    private static String mimeTypeToExtension(String mimeType) {
        try {
            return MimeTypes.getDefaultMimeTypes().forName(mimeType).getExtension();
        } catch (MimeTypeException e) {
            return "";
        }
    }
}
