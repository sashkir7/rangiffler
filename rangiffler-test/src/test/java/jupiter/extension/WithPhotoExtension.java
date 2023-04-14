package jupiter.extension;

import api.PhotoGrpcApi;
import helper.DataHelper;
import jupiter.annotation.WithPhoto;
import org.junit.jupiter.api.extension.*;
import sashkir7.grpc.Photo;

import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WithPhotoExtension extends BaseJUnitExtension implements BeforeEachCallback, ParameterResolver, AfterEachCallback {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(WithPhotoExtension.class);

    private final PhotoGrpcApi photoApi = new PhotoGrpcApi();

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        List<Parameter> parameters = Arrays.stream(context.getRequiredTestMethod().getParameters())
                .filter(p -> p.isAnnotationPresent(WithPhoto.class))
                .toList();

        Map<String, Photo> photos = new HashMap<>();
        for (Parameter parameter : parameters) {
            WithPhoto annotation = parameter.getAnnotation(WithPhoto.class);
            String username = "".equals(annotation.username()) ? DataHelper.randomUsername() : annotation.username();
            Photo photo = convertToPhoto(username, annotation);
            photos.put(parameter.getName(), photoApi.addPhoto(photo));
        }

        putToStore(context, NAMESPACE, photos);
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext context) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(Photo.class)
                && parameterContext.getParameter().isAnnotationPresent(WithPhoto.class);
    }

    @Override
    public Photo resolveParameter(ParameterContext parameterContext, ExtensionContext context) throws ParameterResolutionException {
        return (Photo) getFromStore(context, NAMESPACE, Map.class)
                .get(parameterContext.getParameter().getName());
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        Map<String, Photo> photos = getFromStore(context, NAMESPACE, Map.class);
        photos.values().forEach(photo -> photoApi.deletePhoto(photo.getId()));
    }

}
