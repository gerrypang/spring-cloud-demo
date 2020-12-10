package com.gerry.pang.utils.excel.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Target(FIELD)
@Retention(RUNTIME)
public @interface Label {
    /**
     * (Optional) The Label name. Defaults to the unqualified
     * name of the column. This name is used to refer to the
     * frontpage.
     */
    String name() default "";
}
