package userdata.service;

import jakarta.annotation.Nonnull;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import userdata.data.UserEntity;
import userdata.data.repository.UserRepository;
import userdata.model.UserModel;

import javax.management.InstanceAlreadyExistsException;

@Component
public class UserRegistrationService {

    private final UserRepository userRepository;

    @Autowired
    public UserRegistrationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @RabbitListener(queues = {"q.userdata-registration"})
    public void saveRegistrationUser(@Nonnull UserModel userModel) throws InstanceAlreadyExistsException {
        // ToDo Если возникает ошибка, то сообщение перекладывается в эту же самую очередь
        //  ---> Тем самым получаем бесконечный цикл и кабзду на выходе.
        //  ---> Реализовать, чтобы в случае ошибки сообщение перекладывалось в ошибочную очередь
        if (userRepository.findByUsername(userModel.getUsername()) != null) {
            throw new InstanceAlreadyExistsException(userModel.getUsername());
        }

        UserEntity entity = UserEntity.builder()
                .username(userModel.getUsername())
                .firstname(userModel.getFirstname())
                .lastname(userModel.getLastname())
                .build();
        userRepository.save(entity);
    }

}
