package rs.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class ExceptionLogHelper {
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final Logger LOGGER = LogManager.getLogger(ExceptionLogHelper.class);

    private String httpResponseText;
    private Class<?> origin;
    private Exception exception;
    private Map<String, String> map;
    private Logger logger;

    static class InnerHelper {
        String errorMessage;

        InnerHelper(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        // it's important to have public getter for Jackson serialization
        public String getErrorMessage() {
            return errorMessage;
        }
    }

    public ExceptionLogHelper(Class<?> origin, Exception exception) {
        this.origin = origin;
        this.exception = exception;
        logger = LogManager.getLogger(origin);
        httpResponseText = exception.toString();
    }

    public static String jsonifyString(String src) {
        String retValue = null;

        try {
            retValue = mapper.writerWithDefaultPrettyPrinter().
                    writeValueAsString(new InnerHelper(src));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
        }

        return retValue;
    }

    public String getHttpResponseText() {
        return httpResponseText;
    }

    public void logException() {
        StackTraceElement[] stackTraceElements = exception.getStackTrace();
        StringBuilder builder = new StringBuilder();
        for (StackTraceElement element : stackTraceElements) {
            builder.append("\t");
            builder.append(element.toString());
            builder.append("\n");
        }

        exception.printStackTrace();
        logger.error(exception.toString());
        logger.info(builder.toString());
    }
}
