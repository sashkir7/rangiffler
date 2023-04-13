package userdata.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import userdata.data.UserEntity;
import userdata.data.repository.UserRepository;

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
    public void registerUser(String json) {
        // ToDo Как быть то? Мне в этом же проекте нужно писать grpc клиента?
        //  который предоставит единственный метод addUser()
//        usersGrpcService.addUser(fromJson(userJson));

        // ToDo -- через grpc api add-user
        userRepository.save(fromJson(json));
    }

    private static UserEntity fromJson(String json) {
        try {
            return new ObjectMapper().readValue(json, UserEntity.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
