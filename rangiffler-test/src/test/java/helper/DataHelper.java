package helper;

import com.github.javafaker.Faker;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

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

    public static String imageByClasspath(String imageClasspath) {
        ClassLoader classLoader = DataHelper.class.getClassLoader();
        try (InputStream is = classLoader.getResourceAsStream(imageClasspath)) {
            String fileExtension = imageClasspath.substring(imageClasspath.lastIndexOf(".") + 1);
            assert is != null;
            byte[] base64Image = Base64.getEncoder().encode(is.readAllBytes());
            return "data:image/" + fileExtension + ";base64," + new String(base64Image);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
