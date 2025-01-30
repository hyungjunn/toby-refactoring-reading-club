package refactoring.ch01;

import java.util.List;

public record StatementData(String customer, List<EnrichedPerformances> performances) {
    public double getTotalAmount() {
        return performances.stream()
                .mapToDouble(EnrichedPerformances::amount)
                .sum();
    }

    public int getVolumeCredits() {
        return performances.stream()
                .mapToInt(EnrichedPerformances::volumeCredits)
                .sum();
    }
}
