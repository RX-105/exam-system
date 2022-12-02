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
    int ERR_MAJOR_EXISTS = 1004;
    int ERR_MAJOR_NOT_FOUND = 1005;
    int ERR_EXAM_EXISTS = 1006;
    int ERR_EXAM_NOT_FOUND = 1007;
}
