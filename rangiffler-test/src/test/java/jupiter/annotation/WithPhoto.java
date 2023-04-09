package jupiter.annotation;

public @interface WithPhoto {
    String img() default "";
    String country() default "";
    String description() default "";
}
