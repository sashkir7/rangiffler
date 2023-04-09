package jupiter.annotation;

public @interface WithUser {
    String username() default "";
    String password() default "";
    WithPhoto[] photos() default {};
}
