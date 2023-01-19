package io.n0sense.examsystem.util;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestPasswordEncoder {
    @Test
    void testMD5(){
        System.out.println(PasswordEncoder.MD5Encrypt("123456"));
    }

    @Test
    void testSHA256(){
        System.out.println(PasswordEncoder.SHA256Encrypt("123456"));
    }
}
