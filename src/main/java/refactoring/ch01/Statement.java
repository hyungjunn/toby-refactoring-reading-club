package refactoring.ch01;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;

public class Statement {
    private final Invoice invoice;
    private final StatementDataGenerator dataGenerator;

    public Statement(Invoice invoice, Map<String, Play> plays) {
        this.invoice = invoice;
        this.dataGenerator = new StatementDataGenerator(plays);
    }

    public String generate() {
        StatementData data = dataGenerator.createStatementData(invoice);
        return renderPlainText(data);
    }

    public String generateHtml() {
        StatementData data = dataGenerator.createStatementData(invoice);
        return renderHtml(data);
    }

    private String renderHtml(StatementData data) {
        StringBuilder result = new StringBuilder(String.format("청구 내역 (고객명: %s)\n", data.customer()));
        result.append("<table>\n");
        result.append("<tr><th>연극</th><th>좌석 수</th><th>금액</th></tr>\n");
        for (EnrichedPerformances perf : data.performances()) {
            result.append(String.format("<tr><td>%s</td><td>(%s석)</td>", perf.play().name(), perf.performance().audience()));
            result.append(String.format("<td>%s</td></tr>\n", formatUSD(perf.amount())));
        }
        result.append("</table>\n");
        result.append(String.format("<p>총액: <em>%s</em></p>\n", formatUSD(data.getTotalAmount())));
        result.append(String.format("<p>적립 포인트: <em>%d</em>점</p>\n", data.getVolumeCredits()));
        return result.toString();
    }

    private String renderPlainText(StatementData data) {
        StringBuilder result = new StringBuilder(String.format("청구 내역 (고객명: %s)\n", data.customer()));
        for (EnrichedPerformances perf : data.performances()) {
            result.append(String.format(" %s: %s (%d석)\n",
                    perf.play().name(),
                    formatUSD(perf.amount()),
                    perf.performance().audience()));
        }

        result.append(String.format("총액: %s\n", formatUSD(data.getTotalAmount())));
        result.append(String.format("적립 포인트: %d점\n", data.getVolumeCredits()));
        return result.toString();
    }

    private String formatUSD(double number) {
        return NumberFormat.getCurrencyInstance(Locale.US).format(number / 100);
    }

}
