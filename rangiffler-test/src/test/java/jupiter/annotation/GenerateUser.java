package jupiter.annotation;

import jupiter.extension.GenerateUserExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(GenerateUserExtension.class)
public @interface GenerateUser {
    String username() default "";
    String password() default "";
    String firstname() default "";
    String lastname() default "";
    WithPhoto[] photos() default {};
    WithPartner[] partners() default {};
}
