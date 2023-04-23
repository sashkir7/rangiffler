package pages.conditions;

import com.codeborne.selenide.CheckResult;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Driver;
import helper.DataHelper;
import org.openqa.selenium.WebElement;
import sashkir7.grpc.Photo;

import javax.annotation.Nonnull;
import java.util.Arrays;

import static java.nio.charset.StandardCharsets.UTF_8;

public final class PhotoCondition extends Condition {

    public static Condition photo(String expectedPhotoClasspath) {
        return photo(DataHelper.imageByClasspath(expectedPhotoClasspath).getBytes(UTF_8));
    }

    public static Condition photo(Photo expectedPhoto) {
        return photo(expectedPhoto.getPhoto().getBytes(UTF_8));
    }

    public static Condition photo(byte[] expectedBase64Photo) {
        return new PhotoCondition(expectedBase64Photo);
    }

    private final byte[] expectedBase64Photo;

    private PhotoCondition(byte[] expectedBase64Photo) {
        super("photo");
        this.expectedBase64Photo = expectedBase64Photo;
    }

    @Nonnull
    @Override
    public CheckResult check(@Nonnull Driver driver, WebElement element) {
        String actualPhoto = element.getAttribute("src");
        return new CheckResult(Arrays.equals(expectedBase64Photo, actualPhoto.getBytes()), actualPhoto);
    }

}