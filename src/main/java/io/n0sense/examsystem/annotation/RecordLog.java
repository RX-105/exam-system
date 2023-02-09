package io.n0sense.examsystem.annotation;

import java.lang.annotation.*;

/**
 * 如果为一个方法添加这个注释，与这个注解配套的AOP增强类将会在该方法执行结束后向数据库添加一条日志。这个注解的两个参数，
 * action和message，用于表明日志记录的行为以及附加信息，都将被写入数据库。<br>
 * 如果方法的返回类型是int，则只有返回值为{@code Status.OK}时才会记录日志；如果返回类型为{@link io.n0sense.examsystem.entity.R R}
 * ，则只有status字段为{@code Status.OK}时才会记录日志；对于其他返回类型，则会直接记录日志。<br>
 * 需要注意的是，日志记录的身份信息依赖于session存储，这意味着方法执行完成后，session存储中应当存在需要的身份数据。
 * 如果某个方法需要写入session存储，则应当在这里添加该注解。
 * @see io.n0sense.examsystem.commons.constants.Actions Actions
 * @see io.n0sense.examsystem.commons.constants.Status Status
 * @see io.n0sense.examsystem.entity.R R
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RecordLog {
    /**
     * action参数用于记录这条日志的行为，其指定的行为在{@link io.n0sense.examsystem.commons.constants.Actions Actions}
     * 已经预先定义好，任何情况下都只应该引用这个类里面的常量，以方便未来分类。如果不指定，默认为normal。
     */
    String[] action() default "normal";
    /**
     * message用于指明日志的附加信息，也可以不添加，默认为空字符串。
     */
    String[] message() default "";
}
