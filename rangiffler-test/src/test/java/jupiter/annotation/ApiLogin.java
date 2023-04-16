package jupiter.annotation;

import jupiter.extension.ApiLoginExtension;
import jupiter.extension.GenerateUserExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@ExtendWith({GenerateUserExtension.class, ApiLoginExtension.class})
public @interface ApiLogin {
    String username() default "";
    String password() default "";
    GenerateUser user() default @GenerateUser(handleAnnotation = false);
}
