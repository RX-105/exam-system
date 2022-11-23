package io.n0sense.examsystem.commons;

/**
 * 通用信息类。
 * @author kazuha
 * @since 2022/11/23
 */
public interface CommonStatus {
    int OK = 0;
    int ERR_USERNAME_IN_USE = 1001;
    int ERR_INCORRECT_PASSWORD = 1002;
    int ERR_USER_NOT_FOUND = 1003;
}
