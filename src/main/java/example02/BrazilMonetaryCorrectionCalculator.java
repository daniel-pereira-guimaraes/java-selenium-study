package example02;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

public class BrazilMonetaryCorrectionCalculator {
    private static final String BCB_CALCULATOR_URL =
            "https://www3.bcb.gov.br/CALCIDADAO/publico/exibirFormCorrecaoValores.do?method=exibirFormCorrecaoValores";
    private static final String INDEX_SELECT_ID = "selIndice";
    private static final String INDEX_TEXT = "IPCA (IBGE) - a partir de 01/1980";
    private static final String START_DATE_INPUT_NAME = "dataInicial";
    private static final String END_DATE_INPUT_NAME = "dataFinal";
    private static final String PRINCIPAL_VALUE_INPUT_NAME = "valorCorrecao";
    private static final String CORRECT_VALUE_BUTTON_CLASS_NAME = "botao";
    private static final String CORRECTED_VALUE_CELL_XPATH =
            "//td[contains(text(), 'Valor corrigido')]" +
            "/following-sibling::td[contains(text(), 'R$')]";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter .ofPattern("MMyyyy");
    private static final BigDecimal HUNDRED = BigDecimal.valueOf(100);
    private static WebDriver webDriver = null;

    public BigDecimal correct(YearMonth startDate, YearMonth endDate, BigDecimal principalValue) {
        var startDateKeys = startDate.format(DATE_FORMATTER);
        var endDateKeys = endDate.format(DATE_FORMATTER);
        var principalValueKeys = String.valueOf(principalValue.multiply(HUNDRED).longValue());
        try {
            webDriver = new ChromeDriver();
            webDriver.get(BCB_CALCULATOR_URL);
            inputData(startDateKeys, endDateKeys, principalValueKeys);
            clickButton();
            return getResult();
        } finally {
            if (webDriver != null) {
                webDriver.quit();
                webDriver = null;
            }
        }
    }

    private static void inputData(String startDateString, String endDateString, String principalValueString) {
        new Select(webDriver.findElement(By.id(INDEX_SELECT_ID))).selectByVisibleText(INDEX_TEXT);
        webDriver.findElement(By.name(START_DATE_INPUT_NAME)).sendKeys(startDateString);
        webDriver.findElement(By.name(END_DATE_INPUT_NAME)).sendKeys(endDateString);
        webDriver.findElement(By.name(PRINCIPAL_VALUE_INPUT_NAME)).sendKeys(principalValueString);
    }

    private static void clickButton() {
        webDriver.findElement(By.className(CORRECT_VALUE_BUTTON_CLASS_NAME)).click();
    }

    private static BigDecimal getResult() {
        var correctedValueCell = webDriver.findElement(By.xpath(CORRECTED_VALUE_CELL_XPATH));
        var correctedValueText = correctedValueCell.getText().replaceAll("[^0-9]", "");
        var correctedValueLong = Long.parseLong(correctedValueText);
        return BigDecimal.valueOf(correctedValueLong).divide(HUNDRED, 2, RoundingMode.HALF_EVEN);
    }

}
