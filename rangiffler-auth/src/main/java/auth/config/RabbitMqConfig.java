package auth.config;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    private final String userdataRegistrationQueueName;

    @Autowired
    public RabbitMqConfig(@Value("${rabbit-mq.userdata-registration}") String userdataRegistrationQueueName) {
        this.userdataRegistrationQueueName = userdataRegistrationQueueName;
    }

    @Bean
    public Queue createUserdataRegistrationQueue() {
        return new Queue(userdataRegistrationQueueName);
    }

}
