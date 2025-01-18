package refactoring.ch01;

import com.fasterxml.jackson.core.type.TypeReference;
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
        invoices = DataLoader.loadJsonFile("src/main/resources/invoices.json", new TypeReference<List<Invoice>>() {});
        plays = DataLoader.loadJsonFile("src/main/resources/plays.json", new TypeReference<Map<String, Play>>() {});
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
}
