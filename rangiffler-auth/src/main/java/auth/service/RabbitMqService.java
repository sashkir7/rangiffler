package auth.service;

import auth.model.RegistrationModel;
import jakarta.annotation.Nonnull;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RabbitMqService {

    private final RabbitTemplate rabbitTemplate;
    private final String queueName;

    @Autowired
    public RabbitMqService(RabbitTemplate rabbitTemplate,
                           @Value("${rabbit-mq.userdata-queue}") String queueName) {
        this.rabbitTemplate = rabbitTemplate;
        this.queueName = queueName;
    }

    public @Nonnull void sendToUserdata(@Nonnull RegistrationModel model) {
        rabbitTemplate.convertAndSend("", queueName, model.toJson());
    }

}
