package example02;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.YearMonth;
import java.util.Locale;

public class Main {

    private static final Locale PT_BR = Locale.of("pt", "BR");
    private static final int PERCENT_DECIMALS = 2;
    private static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance(PT_BR);
    private static final NumberFormat PERCENT_FORMAT = percentFormat();

    public static void main(String[] args) {
        var calculator = new BrazilMonetaryCorrectionCalculator();
        var startDate = YearMonth.of(2023, 1);
        var endDate = YearMonth.of(2023, 12);
        var principalValue = BigDecimal.valueOf(100.0);
        var correctedValue = calculator.correct(startDate, endDate, principalValue);
        var rateOfChange  = correctedValue.doubleValue() / principalValue.doubleValue() * 100 - 100;
        System.out.println("Principal value: " + formattedCurrency(principalValue));
        System.out.println("Rate of change:  " + formattedPercent(rateOfChange));
        System.out.println("Corrected value: " + formattedCurrency(correctedValue));
    }

    private static String formattedCurrency(BigDecimal value) {
        return CURRENCY_FORMAT.format(value);
    }

    private static String formattedPercent(double percent) {
        return PERCENT_FORMAT.format(percent) + '%';
    }

    private static NumberFormat percentFormat() {
        var formatter = NumberFormat.getNumberInstance(PT_BR);
        formatter.setMaximumFractionDigits(PERCENT_DECIMALS);
        return formatter;
    }

}
