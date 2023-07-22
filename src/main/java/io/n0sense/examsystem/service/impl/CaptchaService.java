package io.n0sense.examsystem.service.impl;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import io.n0sense.examsystem.commons.constants.Status;
import io.n0sense.examsystem.repository.RedisRepository;
import io.n0sense.examsystem.service.ICaptchaService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.awt.image.BufferedImage;
import java.time.Duration;

@Service
@Log
@RequiredArgsConstructor
public class CaptchaService implements ICaptchaService {
    private final RedisRepository redisRepository;
    private final DefaultKaptcha kaptcha;
    @Override
    public BufferedImage createCaptcha(String group, Long uid) {
        String text = kaptcha.createText();
        log.warning(text);
        BufferedImage captchaImage = kaptcha.createImage(text);
        long unixTimeNow = System.currentTimeMillis() / 1000L;
        String content = "%s@%d".formatted(text, unixTimeNow);
        String key = "captcha-%d@%s".formatted(uid, group);
        redisRepository.set(key, content, Duration.ofMinutes(10));
        return captchaImage;
    }

    @Override
    public int verifyCaptcha(String group, Long uid, @NonNull String captchaToCheck) {
        String[] source = redisRepository.get("captcha-%d@%s".formatted(uid, group)).split("@");
        String captcha = source[0];
        long time = Long.parseLong(source[1]);
        long timeNow = System.currentTimeMillis() / 1000L;
        if (timeNow - time < 2) {
            return Status.ERR_BAD_REQUEST_INTERVAL;
        }
        if (StringUtils.hasLength(captcha) && captcha.equalsIgnoreCase(captchaToCheck)) {
            return Status.OK;
        } else {
            return Status.ERR_INCORRECT_CAPTCHA;
        }
    }
}
