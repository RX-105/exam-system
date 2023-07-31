package io.n0sense.examsystem.annotation;

import java.lang.annotation.*;

/**
 * 标记本Api的版本。
 */
@Target({
        ElementType.TYPE, ElementType.METHOD
})
@Retention(RetentionPolicy.CLASS)
@Documented
public @interface ApiVersion {
    String value();
}
