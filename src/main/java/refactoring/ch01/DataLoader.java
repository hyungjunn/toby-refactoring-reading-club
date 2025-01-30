package refactoring.ch01;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class DataLoader {
    private static <T> T loadJsonFile(String fileName, TypeReference<T> typeReference) {
        ObjectMapper objectMapper = new ObjectMapper();
        try (InputStream inputStream = new FileInputStream(fileName)) {
            return objectMapper.readValue(inputStream, typeReference);
        } catch (IOException e) {
            throw new RuntimeException("JSON 파일을 읽어오는데 실패하였습니다.", e);
        }
    }

    public static List<Invoice> loadInvoices(String filePath) {
        return loadJsonFile(filePath, new TypeReference<List<Invoice>>() {
        });
    }

    public static Map<String, Play> loadPlays(String filePath) {
        return loadJsonFile(filePath, new TypeReference<Map<String, Play>>() {
        });
    }
}
