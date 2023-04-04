package auth.service;

import auth.model.RegistrationModel;
import jakarta.annotation.Nonnull;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RabbitMqService {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public RabbitMqService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public @Nonnull void sendToUserdata(@Nonnull RegistrationModel model) {
        String queueName = "q.userdata-registration";
        rabbitTemplate.convertAndSend("", queueName, model.toJson());
    }

}
