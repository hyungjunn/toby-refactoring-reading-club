package refactoring.ch01;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

class StatementTest {
    private List<Invoice> invoices;
    private Map<String, Play> plays;

    @BeforeEach
    void setUp() {
        invoices = DataLoader.loadInvoices("src/main/resources/invoices.json");
        plays = DataLoader.loadPlays("src/main/resources/plays.json");
    }

    @Test
    void testStatement() {
        Statement statement = new Statement(invoices.get(0), plays);
        String result = statement.generate();
        Assertions.assertThat(result).isEqualTo("""
                청구 내역 (고객명: BigCo)
                 Hamlet: $650.00 (55석)
                 As You Like It: $580.00 (35석)
                 Othello: $500.00 (40석)
                총액: $1,730.00
                적립 포인트: 47점
                """);
    }

    @Test
    void testHtmlStatement() {
        Statement statement = new Statement(invoices.get(0), plays);
        String result = statement.generateHtml();
        Assertions.assertThat(result).isEqualTo("""
        청구 내역 (고객명: BigCo)
        <table>
        <tr><th>연극</th><th>좌석 수</th><th>금액</th></tr>
        <tr><td>Hamlet</td><td>(55석)</td><td>$650.00</td></tr>
        <tr><td>As You Like It</td><td>(35석)</td><td>$580.00</td></tr>
        <tr><td>Othello</td><td>(40석)</td><td>$500.00</td></tr>
        </table>
        <p>총액: <em>$1,730.00</em></p>
        <p>적립 포인트: <em>47</em>점</p>
        """);
    }
}
