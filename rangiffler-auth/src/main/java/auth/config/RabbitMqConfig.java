package auth.config;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    @Autowired
    public RabbitMqConfig() {}

    @Bean
    public Queue createUserdataRegistrationQueue() {
        return new Queue("q.userdata-registration");
    }

}
