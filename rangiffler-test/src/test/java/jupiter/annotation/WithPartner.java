package jupiter.annotation;

import model.PartnerStatus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface WithPartner {
    WithUser user() default @WithUser;
    WithPhoto[] photos() default {};
    PartnerStatus status() default PartnerStatus.FRIEND;
}
