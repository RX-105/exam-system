package io.n0sense.examsystem.annotation;

import java.lang.annotation.*;

/**
 * 如果为一个方法添加这个注释，与这个注解配套的AOP增强类将会在该方法执行结束后向数据库添加一条日志。这个注解的两个参数，
 * action和message，用于表明日志记录的行为以及附加信息，都将被写入数据库。
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RecordLog {
    /**
     * action参数用于记录这条日志的行为，其指定的行为在{@link io.n0sense.examsystem.commons.constants.Actions Actions}
     * 已经预先定义好，任何情况下都只应该引用这个类里面的常量，以方便未来分类。如果不指定，默认为normal。
     */
    String action() default "normal";
    /**
     * message用于指明日志的附加信息，也可以不添加，默认为空字符串。
     */
    String message() default "";
}
