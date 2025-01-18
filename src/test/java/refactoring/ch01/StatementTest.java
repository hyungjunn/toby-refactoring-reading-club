package refactoring.ch01;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

class StatementTest {

    @Test
    void testStatement() {
        List<Invoice> invoices;
        Map<String, Play> plays;

        ObjectMapper objectMapper = new ObjectMapper();
        try (
                InputStream invoicesStream = new FileInputStream("src/main/resources/invoices.json");
                InputStream playsStream = new FileInputStream("src/main/resources/plays.json")
        ) {
            invoices = objectMapper.readValue(invoicesStream, new TypeReference<List<Invoice>>() {});
            plays = objectMapper.readValue(playsStream, new TypeReference<Map<String, Play>>() {});
        } catch (IOException e) {
            throw new RuntimeException("JSON 파일을 읽어오는데 실패하였습니다.", e);
        }

        String result = Statement.statement(invoices.get(0), plays);
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
