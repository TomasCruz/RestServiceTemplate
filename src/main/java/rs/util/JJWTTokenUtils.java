package rs.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.Key;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

// It would be best to extract this class to a separate JAR project,
// and add it as a dependency both in server and clients.
// It is exposed as an API instead for simplicity and demonstration
public class JJWTTokenUtils {

    private static final String UTF8 = "UTF-8";

    private byte[] secretKeyBytes;
    private SignatureAlgorithm signatureAlgorithm;

    public JJWTTokenUtils() throws IllegalArgumentException, IOException {
        Properties properties = new Properties();
        properties = PropertiesReader.read(properties, "App.properties");

        String secretKey = properties.getProperty("secretKey");
        if (secretKey == null)
            throw new IllegalArgumentException();

        secretKeyBytes = secretKey.getBytes(UTF8);
        signatureAlgorithm = SignatureAlgorithm.forName(properties.getProperty("algorithmName"));
    }

    public Map<String, String> decodeToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKeyBytes)
                .parseClaimsJws(token)
                .getBody();

        HashMap<String, String> retValue = new HashMap<>();
        for (Map.Entry<String, Object> entry : claims.entrySet())
            retValue.put(entry.getKey(), claims.get(entry.getKey(), String.class));

        return retValue;
    }

    public String encodeToken(Map<String, String> jsonMap) {
        JwtBuilder jwtsBuilder = Jwts.builder();

        for (Map.Entry<String, String> entry : jsonMap.entrySet())
            jwtsBuilder.claim(entry.getKey(), entry.getValue());

        Key signingKey = new SecretKeySpec(secretKeyBytes, signatureAlgorithm.getJcaName());
        jwtsBuilder.signWith(signatureAlgorithm, signingKey);

        return jwtsBuilder.compact();
    }
}
