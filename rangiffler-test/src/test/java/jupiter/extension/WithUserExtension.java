package jupiter.extension;

import api.UserdataGrpcApi;
import io.qameta.allure.Step;
import jupiter.annotation.WithUser;
import model.UserModel;
import org.junit.jupiter.api.extension.*;
import sashkir7.grpc.User;

import java.lang.reflect.Parameter;
import java.util.*;

public class WithUserExtension extends BaseJUnitExtension implements BeforeEachCallback, ParameterResolver, AfterEachCallback {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(GenerateUserExtension.class);

    private final UserdataGrpcApi userdataApi = new UserdataGrpcApi();

    @Override
    @Step("Arrange user test data")
    public void beforeEach(ExtensionContext context) throws Exception {
        List<Parameter> parameters = Arrays.stream(context.getRequiredTestMethod().getParameters())
                .filter(p -> p.isAnnotationPresent(WithUser.class))
                .toList();

        Map<String, User> users = new HashMap<>();
        for (Parameter parameter : parameters) {
            WithUser annotation = parameter.getAnnotation(WithUser.class);
            UserModel userModel = convertToUserModel(annotation);
            users.put(parameter.getName(), userdataApi.addUser(userModel.toGrpc()));
        }

        putToStore(context, NAMESPACE, users);
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext context)
            throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(User.class)
                && parameterContext.getParameter().isAnnotationPresent(WithUser.class);
    }

    @Override
    public User resolveParameter(ParameterContext parameterContext, ExtensionContext context)
            throws ParameterResolutionException {
        return (User) getFromStore(context, NAMESPACE, Map.class)
                .get(parameterContext.getParameter().getName());
    }

    @Override
    @Step("Remove created photo")
    public void afterEach(ExtensionContext context) throws Exception {
        Map<String, User> users = getFromStore(context, NAMESPACE, Map.class);
        users.values().forEach(user -> userdataApi.deleteUser(user.getUsername()));
    }

}
