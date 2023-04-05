package userdata.service;

import jakarta.annotation.Nonnull;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import userdata.data.UserEntity;
import userdata.data.repository.UserRepository;
import userdata.model.UserDto;

@Component
public class RegUserListenerService {

    private final UserRepository userRepository;

    @Autowired
    public RegUserListenerService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // ToDo Если возникает ошибка, то сообщение перекладывается в эту же самую очередь
    //  ---> Тем самым получаем бесконечный цикл и кабзду на выходе.
    //  ---> Реализовать, чтобы в случае ошибки сообщение перекладывалось в ошибочную очередь
    @RabbitListener(queues = {"q.userdata-registration"})
    public void registerUser(@Nonnull String json) {
        UserDto userDto = UserDto.fromJson(json);
        userRepository.save(UserEntity.fromDto(userDto));
    }

}
