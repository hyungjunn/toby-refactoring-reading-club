package refactoring.ch01;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class DataLoader {

    public static <T> T loadJsonFile(String fileName, TypeReference<T> typeReference) {
        ObjectMapper objectMapper = new ObjectMapper();
        try (InputStream inputStream = new FileInputStream(fileName)) {
            return objectMapper.readValue(inputStream, typeReference);
        } catch (IOException e) {
            throw new RuntimeException("JSON 파일을 읽어오는데 실패하였습니다.", e);
        }
    }
}
