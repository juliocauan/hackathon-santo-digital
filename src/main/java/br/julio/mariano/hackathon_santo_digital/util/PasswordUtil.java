package br.julio.mariano.hackathon_santo_digital.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public final class PasswordUtil {

    private static PasswordEncoder encoder;

    @Autowired
    private void setPasswordEncoder(PasswordEncoder encoder) {
        PasswordUtil.encoder = encoder;
    }

    public static String encode(String password) {
        return encoder.encode(password);
    }

}
