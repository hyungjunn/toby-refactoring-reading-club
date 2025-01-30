package refactoring.ch01;

import java.util.List;
import java.util.Map;

class StatementDataGenerator {
    private final Map<String, Play> plays;

    StatementDataGenerator(Map<String, Play> plays) {
        this.plays = plays;
    }

    public Statement.StatementData createStatementData(Invoice invoice) {
        List<Statement.EnrichedPerformances> enrichedPerformances = invoice.performances().stream()
                .map(p -> new Statement.EnrichedPerformances(p, playFor(p), amountFor(p), volumeCreditsFor(p)))
                .toList();
        return new Statement.StatementData(invoice.customer(), enrichedPerformances);
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
