package io.n0sense.examsystem.service;

import java.awt.image.BufferedImage;

public interface ICaptchaService {
    BufferedImage createCaptcha(String group, Long uid);
    int verifyCaptcha(String group, Long uid, String captcha);
}
