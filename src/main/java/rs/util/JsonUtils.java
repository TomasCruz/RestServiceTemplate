package rs.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class JsonUtils {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static String mapToJson(Map<String, String> map) throws JsonProcessingException {
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(map);
    }
}
