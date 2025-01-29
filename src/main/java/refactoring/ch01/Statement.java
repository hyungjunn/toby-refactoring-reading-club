package refactoring.ch01;

import java.text.NumberFormat;
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
        double totalAmount = 0;
        int volumeCredits = 0;
        StringBuilder result = new StringBuilder(String.format("청구 내역 (고객명: %s)\n", invoice.customer()));
        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.US);

        for (Performance perf : invoice.performances()) {
            Play play = playFor(perf);

            // 포인트를 적립한다.
            volumeCredits += Math.max(perf.audience() - 30, 0);
            // 희극 관객 5명마다 추가 포인트를 제공한다.
            if ("comedy".equals(play.type())) {
                volumeCredits += (int) Math.floor((double) perf.audience() / 5);
            }

            // 청구 내역을 출력한다.
            result.append(String.format(" %s: %s (%d석)\n", play.name(), format.format(amountFor(perf, play) / 100), perf.audience()));
            totalAmount += amountFor(perf, play);
        }
        result.append(String.format("총액: %s\n", format.format(totalAmount / 100)));
        result.append(String.format("적립 포인트: %d점\n", volumeCredits));
        return result.toString();
    }

    private Play playFor(Performance perf) {
        return plays.get(perf.playID());
    }

    private static double amountFor(Performance perf, Play play) {
        double result = 0;
        switch (play.type()) {
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
                throw new IllegalArgumentException(String.format("알 수 없는 장르: %s", play.type()));
        }
        return result;
    }
}
