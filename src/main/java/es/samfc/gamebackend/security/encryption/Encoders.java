package es.samfc.gamebackend.security.encryption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.*;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class Encoders {

    @Value("${app.encryption.method}")
    private String encryptionMethod;

    private final Logger logger = LoggerFactory.getLogger(Encoders.class);

    private final HashMap<String, PasswordEncoder> passwordEncoders;

    public Encoders() {
        passwordEncoders = new HashMap<>();
        passwordEncoders.put("bcrypt", new BCryptPasswordEncoder());
        passwordEncoders.put("pbkdf2", Pbkdf2PasswordEncoder.defaultsForSpringSecurity_v5_8());
        passwordEncoders.put("scrypt", SCryptPasswordEncoder.defaultsForSpringSecurity_v5_8());
        passwordEncoders.put("argon2", Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8());
        passwordEncoders.put("sha256", new StandardPasswordEncoder());
        passwordEncoders.put("noop", NoOpPasswordEncoder.getInstance());
    }

    private PasswordEncoder passwordEncoder;

    public PasswordEncoder getPasswordEncoder() {
        if (passwordEncoder == null) {
            passwordEncoder = new DelegatingPasswordEncoder(encryptionMethod, passwordEncoders);
        }
        if (encryptionMethod.equals("noop") || encryptionMethod.equals("sha256")) {
            logger.info("Encryption method: {} is not recommended for production use. Consider using a more secure method.", encryptionMethod);
        }
        return passwordEncoder;
    }

    public String getEncryptionMethodFromEncoder(PasswordEncoder encoder){
        return passwordEncoders.entrySet().stream()
                .filter(entry -> entry.getValue().equals(encoder))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }

}
