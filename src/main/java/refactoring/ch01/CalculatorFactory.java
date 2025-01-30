package refactoring.ch01;

public class CalculatorFactory {
    public static PerformanceCalculator create(Performance p, Play play) {
        return switch (play.type()) {
            case "tragedy" -> new TragedyCalculator(p, play);
            case "comedy" -> new ComedyCalculator(p, play);
            default -> throw new IllegalArgumentException("Unknown type: " + play.type());
        };
    }
}
