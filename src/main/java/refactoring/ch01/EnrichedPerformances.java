package refactoring.ch01;

public record EnrichedPerformances(
        Performance performance,
        Play play,
        double amount,
        int volumeCredits
) {
}
