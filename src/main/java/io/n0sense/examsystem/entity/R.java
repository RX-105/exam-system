package io.n0sense.examsystem.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * R=Response
 * 用于Web请求中使用的响应实体类，这个类包含三个成员。
 * status - 用于表示本次响应的状态信息。在正常情况下，统一使用0作为状态值；非正常情况返回的状态值由使用这个类的方法定义，
 *      详细参考这个方法的JavaDoc。
 * message - 用于表示本次响应的结果信息。
 * data - 用于存储具体数据的键值对对象。
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class R {
    private Integer status;
    private String message;
    private Map<String, Object> data;
}
