package refactoring.ch01;

import java.util.List;
import java.util.Map;

public class StatementDataGenerator {
    private final Map<String, Play> plays;

    public StatementDataGenerator(Map<String, Play> plays) {
        this.plays = plays;
    }

    public StatementData createStatementData(Invoice invoice) {
        List<EnrichedPerformances> enriched = invoice.performances().stream()
                .map(this::enrichPerformance)
                .toList();

        return new StatementData(invoice.customer(), enriched);
    }

    private EnrichedPerformances enrichPerformance(Performance perf) {
        Play play = plays.get(perf.playID());
        PerformanceCalculator calculator = CalculatorFactory.create(perf, play);
        return new EnrichedPerformances(
                perf,
                play,
                calculator.getAmount(),
                calculator.getVolumeCredits()
        );
    }
}
