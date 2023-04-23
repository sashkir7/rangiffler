package jupiter.annotation;

import jupiter.extension.WithPhotoExtension;
import model.CountryEnum;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.ANNOTATION_TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(WithPhotoExtension.class)
public @interface WithPhoto {
    String username() default "";
    String description() default "";
    String imageClasspath() default "img/girl.jpeg";
    CountryEnum country() default CountryEnum.RUSSIA;
}
