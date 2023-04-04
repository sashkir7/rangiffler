package auth.service;

import jakarta.annotation.Nonnull;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static java.nio.charset.StandardCharsets.UTF_8;

@Component
public class RabbitMqService {

    private final RabbitTemplate rabbitTemplate;
    private final String queueName;

    @Autowired
    public RabbitMqService(RabbitTemplate rabbitTemplate,
                           @Value("${rabbit-mq.register-queue}") String queueName) {
        this.rabbitTemplate = rabbitTemplate;
        this.queueName = queueName;
    }

    public @Nonnull void sendRegisteredUsername(@Nonnull String username) {
        Message message = new Message(username.getBytes(UTF_8));
        rabbitTemplate.send("", queueName, message);
    }

}
