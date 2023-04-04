package userdata.service;

import jakarta.annotation.Nonnull;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import userdata.data.UserEntity;
import userdata.data.repository.UserRepository;
import userdata.model.UserDto;

import javax.management.InstanceAlreadyExistsException;

@Component
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public @Nonnull UserDto getCurrentUser(@Nonnull String username) {
        // ToDo Обработать исключение, когда пользователь не найден
        UserEntity entity = userRepository.findByUsername(username);
        return UserDto.fromEntity(entity);
    }

    @RabbitListener(queues = {"q.userdata-registration"})
    public void saveRegistrationUser(@Nonnull String userJson) throws InstanceAlreadyExistsException {
        // ToDo Если возникает ошибка, то сообщение перекладывается в эту же самую очередь
        //  ---> Тем самым получаем бесконечный цикл и кабзду на выходе.
        //  ---> Реализовать, чтобы в случае ошибки сообщение перекладывалось в ошибочную очередь
        UserDto userDto = UserDto.fromJson(userJson);

        if (userRepository.findByUsername(userDto.getUsername()) != null) {
            throw new InstanceAlreadyExistsException(userDto.getUsername());
        }

        UserEntity entity = UserEntity.builder()
                .username(userDto.getUsername())
                .firstname(userDto.getFirstname())
                .lastname(userDto.getLastname())
                .build();
        userRepository.save(entity);
    }

}
