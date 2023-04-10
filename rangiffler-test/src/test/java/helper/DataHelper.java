package helper;

import com.github.javafaker.Faker;

public final class DataHelper {

    private final static Faker FAKER = new Faker();

    public static String randomUsername() {
        return FAKER.name().username();
    }

    public static String randomPassword() {
        return FAKER.internet().password(3, 12);
    }

    public static String randomFirstname() {
        return FAKER.name().firstName();
    }

    public static String randomLastname() {
        return FAKER.name().lastName();
    }

}
