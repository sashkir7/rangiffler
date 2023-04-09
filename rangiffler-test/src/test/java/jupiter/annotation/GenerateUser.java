package jupiter.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface GenerateUser {
    WithUser user();
    WithPartner[] partners() default {};
}
