package refactoring.ch01;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Statement {
    private final Invoice invoice;
    private final Map<String, Play> plays;

    public Statement(Invoice invoice, Map<String, Play> plays) {
        this.invoice = invoice;
        this.plays = plays;
    }

    public String generate() {
        List<EnrichedPerformances> enrichedPerformances = invoice.performances().stream()
                .map(p -> new EnrichedPerformances(p, playFor(p), amountFor(p), volumeCreditsFor(p)))
                .toList();
        StatementData data = new StatementData(invoice.customer(), enrichedPerformances);
        return renderPlainText(data);
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

        result.append(String.format("총액: %s\n", formatUSD(totalAmount)));
        result.append(String.format("적립 포인트: %d점\n", calculateTotalVolumeCredits()));
        return result.toString();
    }

    private int calculateTotalVolumeCredits() {
        int result = 0;
        for (Performance perf : invoice.performances()) {
            result += volumeCreditsFor(perf);
        }
        return result;
    }

    private String formatUSD(double number) {
        return NumberFormat.getCurrencyInstance(Locale.US).format(number / 100);
    }

    private int volumeCreditsFor(Performance perf) {
        int result = 0;
        result += Math.max(perf.audience() - 30, 0);
        if ("comedy".equals(playFor(perf).type())) {
            result += (int) Math.floor((double) perf.audience() / 5);
        }
        return result;
    }

    private Play playFor(Performance perf) {
        return plays.get(perf.playID());
    }

    private double amountFor(Performance perf) {
        double result = 0;
        switch (playFor(perf).type()) {
            case "tragedy": // 비극
                result = 40000;
                if (perf.audience() > 30) {
                    result += 1000 * (perf.audience() - 30);
                }
                break;
            case "comedy": // 희극
                result = 30000;
                if (perf.audience() > 20) {
                    result += 10000 + 500 * (perf.audience() - 20);
                }
                result += 300 * perf.audience();
                break;
            default:
                throw new IllegalArgumentException(String.format("알 수 없는 장르: %s", playFor(perf).type()));
        }
        return result;
    }
}
