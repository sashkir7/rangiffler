package jupiter.annotation;

import jupiter.extension.WithUserExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.ANNOTATION_TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(WithUserExtension.class)
public @interface WithUser {
    String username() default "";
    String password() default "";
    String firstname() default "";
    String lastname() default "";
    String avatarClasspath() default "";
    WithPhoto[] photos() default {};
}
