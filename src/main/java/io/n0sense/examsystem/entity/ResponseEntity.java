package io.n0sense.examsystem.entity;

import java.util.Map;

/**
 * API调用返回的响应类。
 * code使用HttpStatus类中的响应类型代码，message为响应信息，如果有额外信息，需要放到data里面。
 * @author zgy
 * @since 2022.04.10
 */
public class ResponseEntity {

    private int code;
    private String message;
    private Map<String, Object> data;

    public ResponseEntity(int code, String message, Map<String, Object> data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }
}
