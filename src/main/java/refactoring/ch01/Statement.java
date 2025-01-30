package refactoring.ch01;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Statement {
    private final Invoice invoice;
    private final PerformanceCalculator calculator;

    public Statement(Invoice invoice, Map<String, Play> plays) {
        this.invoice = invoice;
        this.calculator = new PerformanceCalculator(plays);
    }

    public String generate() {
        StatementData data = calculator.createStatementData(invoice);
        return renderPlainText(data);
    }

    private String renderPlainText(StatementData data) {
        StringBuilder result = new StringBuilder(String.format("청구 내역 (고객명: %s)\n", data.customer));
        for (EnrichedPerformances perf : data.performances) {
            result.append(String.format(" %s: %s (%d석)\n",
                    perf.play().name(),
                    formatUSD(perf.amount()),
                    perf.performance().audience()));
        }

        double totalAmount = data.performances.stream()
                .mapToDouble(EnrichedPerformances::amount)
                .sum();

        int volumeCredits = data.performances.stream()
                .mapToInt(EnrichedPerformances::volumeCredits)
                .sum();

        result.append(String.format("총액: %s\n", formatUSD(totalAmount)));
        result.append(String.format("적립 포인트: %d점\n", volumeCredits));
        return result.toString();
    }

    private String formatUSD(double number) {
        return NumberFormat.getCurrencyInstance(Locale.US).format(number / 100);
    }

    record StatementData(String customer, List<EnrichedPerformances> performances) {
    }

    record EnrichedPerformances(
            Performance performance,
            Play play,
            double amount,
            int volumeCredits
    ) {
    }
}
