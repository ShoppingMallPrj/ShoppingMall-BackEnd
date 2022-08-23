package com.example.mall.Integration;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class LoginTest {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    public void peTest() {
        String pw = "123456";

        boolean result = passwordEncoder.matches(pw, passwordEncoder.encode(pw));

        Assert.assertEquals(true, result);
    }
}
