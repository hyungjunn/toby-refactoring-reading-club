package refactoring.ch01;

public abstract class PerformanceCalculator {
    protected final Performance performance;
    protected final Play play;

    public PerformanceCalculator(Performance performance, Play play) {
        this.performance = performance;
        this.play = play;
    }

    public abstract double getAmount();

    public int getVolumeCredits() {
        return Math.max(performance.audience() - 30, 0);
    }
}

class TragedyCalculator extends PerformanceCalculator {
    public TragedyCalculator(Performance performance, Play play) {
        super(performance, play);
    }

    @Override
    public double getAmount() {
        double result = 40000;
        if (performance.audience() > 30) {
            result += 1000 * (performance.audience() - 30);
        }
        return result;
    }
}

class ComedyCalculator extends PerformanceCalculator {
    public ComedyCalculator(Performance performance, Play play) {
        super(performance, play);
    }

    @Override
    public double getAmount() {
        double result = 30000;
        if (performance.audience() > 20) {
            result += 10000 + 500 * (performance.audience() - 20);
        }
        result += 300 * performance.audience();
        return result;
    }

    @Override
    public int getVolumeCredits() {
        return super.getVolumeCredits() + (performance.audience() / 5);
    }
}
