package rs.util;

import org.apache.commons.io.input.BOMInputStream;

import java.io.*;
import java.util.ArrayList;
import java.util.Properties;

public class PropertiesReader {
    public static Properties read(Properties properties, String propertiesName) throws IOException {
        BOMInputStream bomInputStream = null;
        InputStreamReader inputStreamReader = null;
        InputStream inputStream = PropertiesReader.class.getClassLoader().getResourceAsStream(propertiesName);

        try {
            bomInputStream = new BOMInputStream(inputStream);
            boolean bl = bomInputStream.hasBOM();
            inputStreamReader = new InputStreamReader(new BufferedInputStream(bomInputStream), "UTF-8");
            properties.load(inputStreamReader);
        } finally {
            bomInputStream.close();
            inputStream.close();
            inputStreamReader.close();
        }

        return properties;
    }

    public static ArrayList<String> read(String fileName) throws IOException, FileNotFoundException {
        BOMInputStream bomInputStream = null;
        InputStreamReader inputStreamReader = null;
        InputStream inputStream = new FileInputStream(fileName);
        ArrayList<String> retValue = new ArrayList<>();

        try {
            bomInputStream = new BOMInputStream(inputStream);
            boolean bl = bomInputStream.hasBOM();
            inputStreamReader = new InputStreamReader(new BufferedInputStream(bomInputStream), "UTF-8");

            int data = 0;
            do {
                StringBuilder stringBuilder = new StringBuilder();

                data = 0;
                while (data != (int)'\n' && data != -1) {
                    data = inputStreamReader.read();
                    char theChar = (char) data;
                    stringBuilder.append(theChar);
                }

                retValue.add(stringBuilder.toString());
            } while (data != -1);
        } finally {
            bomInputStream.close();
            inputStream.close();
            inputStreamReader.close();
        }

        return retValue;
    }
}
